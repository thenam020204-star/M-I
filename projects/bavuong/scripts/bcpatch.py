#!/usr/bin/env python3
"""Sửa bytecode tĩnh (không cần ASM): đổi đích lời gọi (invokevirtual->invokestatic cùng dạng stack),
thay toàn bộ thân một method bằng mã tự lắp. StackMap cũ của method bị thay sẽ bị bỏ,
preverify.sh (ProGuard -microedition) sinh lại. Dùng chung cpool.ClassFile."""
import struct
from cpool import ClassFile, jutf_dec

# độ dài lệnh (không kể switch/wide); -1 = đặc biệt
L = [1]*256
for op in (0x10, 0x12, 0x15, 0x16, 0x17, 0x18, 0x19, 0x36, 0x37, 0x38, 0x39, 0x3a, 0xa9, 0xbc): L[op] = 2
for op in (0x11, 0x13, 0x14, 0x84, 0xb2, 0xb3, 0xb4, 0xb5, 0xb6, 0xb7, 0xb8, 0xbb, 0xbd, 0xc0, 0xc1, 0xc6, 0xc7) + tuple(range(0x99, 0xa9)): L[op] = 3
L[0xc5] = 4; L[0xb9] = 5; L[0xc8] = 5; L[0xc9] = 5
L[0xaa] = L[0xab] = L[0xc4] = -1

def walk(code):
    """yield (pc, opcode)"""
    pc = 0
    while pc < len(code):
        op = code[pc]
        yield pc, op
        n = L[op]
        if n > 0: pc += n; continue
        if op == 0xc4:
            pc += 6 if code[pc+1] == 0x84 else 4; continue
        q = (pc + 4) & ~3
        if op == 0xaa:
            lo, hi = struct.unpack('>ii', code[q+4:q+12]); pc = q + 12 + 4 * (hi - lo + 1)
        else:
            n = struct.unpack('>i', code[q+4:q+8])[0]; pc = q + 8 + 8 * n

class Klass(ClassFile):
    def __init__(self, data):
        ClassFile.__init__(self, data)
        r = self.rest; p = 0
        self.acc, self.this, self.sup, ni = struct.unpack('>HHHH', r[:8]); p = 8
        self.ifaces = r[p:p+2*ni]; p += 2 * ni
        def members(p):
            n = struct.unpack('>H', r[p:p+2])[0]; p += 2; out = []
            for _ in range(n):
                a, nm, ds, na = struct.unpack('>HHHH', r[p:p+8]); p += 8; attrs = []
                for _ in range(na):
                    an, al = struct.unpack('>HI', r[p:p+6]); attrs.append([an, bytearray(r[p+6:p+6+al])]); p += 6 + al
                out.append([a, nm, ds, attrs])
            return out, p
        self.fields, p = members(p)
        self.methods, p = members(p)
        self.cattrs = r[p:]

    def mname(self, m): return jutf_dec(self.cp[m[1]][1]), jutf_dec(self.cp[m[2]][1])

    def codes(self):
        """yield (method, attr) cho mọi Code attribute"""
        for m in self.methods:
            for a in m[3]:
                if jutf_dec(self.cp[a[0]][1]) == 'Code': yield m, a

    @staticmethod
    def split_code(a):
        b = a[1]
        ms, ml, cl = struct.unpack('>HHI', b[:8])
        return ms, ml, b[8:8+cl], b[8+cl:]

    def redirect(self, mapping):
        """mapping: {(cls,name,desc,opcode_cũ): (opcode_mới, cls,name,desc)} -> số chỗ đã đổi"""
        idx = {}
        for i, e in enumerate(self.cp):
            if e and e[0] in (10, 11):
                c, n, d = self.ref(i)
                for (oc, on, od, oop), new in mapping.items():
                    if (c, n, d) == (oc, on, od): idx[i] = (oop, new)
        if not idx: return 0
        cnt = 0; newref = {}
        for m, a in list(self.codes()):
            ms, ml, code, tail = self.split_code(a); code = bytearray(code)
            for pc, op in walk(bytes(code)):
                if op in (0xb6, 0xb8, 0xb7):
                    i = struct.unpack('>H', code[pc+1:pc+3])[0]
                    if i in idx and idx[i][0] == op:
                        nop, nc, nn, nd = idx[i][1]
                        if i not in newref: newref[i] = self.find_or_add_ref(10, nc, nn, nd)
                        code[pc] = nop; code[pc+1:pc+3] = struct.pack('>H', newref[i]); cnt += 1
            a[1] = bytearray(struct.pack('>HHI', ms, ml, len(code))) + code + tail
        return cnt

    def replace_body(self, name, desc, code, max_stack, max_locals):
        for m, a in self.codes():
            if self.mname(m) == (name, desc):
                a[1] = bytearray(struct.pack('>HHI', max_stack, max_locals, len(code)) + bytes(code)
                                 + b'\x00\x00' + b'\x00\x00')   # không exception table, không attribute con
                return
        raise KeyError((name, desc))

    def build(self):
        self.rest = self._rest()
        return ClassFile.build(self)

    def _rest(self):
        out = bytearray(struct.pack('>HHHH', self.acc, self.this, self.sup, len(self.ifaces) // 2)) + self.ifaces
        for grp in (self.fields, self.methods):
            out += struct.pack('>H', len(grp))
            for a, nm, ds, attrs in grp:
                out += struct.pack('>HHHH', a, nm, ds, len(attrs))
                for an, ab in attrs: out += struct.pack('>HI', an, len(ab)) + ab
        return bytes(out + self.cattrs)
