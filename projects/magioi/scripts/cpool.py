#!/usr/bin/env python3
"""Đọc/ghi constant pool của .class (sửa Utf8 / thêm hằng / đổi methodref) mà không đụng tới code.
Chỉ số cp giữ nguyên nên bytecode không phải dịch chuyển; thêm hằng mới thì nối vào cuối pool."""
import struct

class ClassFile:
    def __init__(self, data):
        self.data = data
        assert data[:4] == b'\xca\xfe\xba\xbe'
        self.minor, self.major = struct.unpack('>HH', data[4:8])
        n = struct.unpack('>H', data[8:10])[0]
        self.cp = [None]; p = 10; i = 1
        while i < n:
            t = data[p]
            if t == 1:
                L = struct.unpack('>H', data[p+1:p+3])[0]
                self.cp.append([1, data[p+3:p+3+L]]); p += 3 + L
            elif t in (3, 4):
                self.cp.append([t, data[p+1:p+5]]); p += 5
            elif t in (5, 6):
                self.cp.append([t, data[p+1:p+9]]); p += 9
                self.cp.append(None); i += 1
            elif t in (7, 8):
                self.cp.append([t, struct.unpack('>H', data[p+1:p+3])[0]]); p += 3
            elif t in (9, 10, 11, 12):
                self.cp.append([t, struct.unpack('>HH', data[p+1:p+5])]); p += 5
            else:
                raise ValueError('cp tag %d' % t)
            i += 1
        self.rest = data[p:]

    def utf(self, i):
        return self.cp[i][1].decode('utf-8', 'surrogatepass') if False else jutf_dec(self.cp[i][1])

    def strings(self):
        """[(cp_index_of_String, utf_index, text)]"""
        return [(i, e[1], jutf_dec(self.cp[e[1]][1])) for i, e in enumerate(self.cp)
                if e and e[0] == 8]

    def set_utf(self, i, s):
        self.cp[i] = [1, jutf_enc(s)]

    def add(self, entry):
        self.cp.append(entry); return len(self.cp) - 1

    def find_or_add_utf(self, s):
        b = jutf_enc(s)
        for i, e in enumerate(self.cp):
            if e and e[0] == 1 and e[1] == b: return i
        return self.add([1, b])

    def find_or_add_class(self, name):
        u = self.find_or_add_utf(name)
        for i, e in enumerate(self.cp):
            if e and e[0] == 7 and e[1] == u: return i
        return self.add([7, u])

    def find_or_add_nat(self, name, desc):
        a, b = self.find_or_add_utf(name), self.find_or_add_utf(desc)
        for i, e in enumerate(self.cp):
            if e and e[0] == 12 and e[1] == (a, b): return i
        return self.add([12, (a, b)])

    def find_or_add_ref(self, tag, cls, name, desc):
        c, nt = self.find_or_add_class(cls), self.find_or_add_nat(name, desc)
        for i, e in enumerate(self.cp):
            if e and e[0] == tag and e[1] == (c, nt): return i
        return self.add([tag, (c, nt)])

    def ref(self, i):
        """methodref/fieldref -> (class, name, desc)"""
        t, (c, nt) = self.cp[i]
        n, d = self.cp[nt][1]
        return jutf_dec(self.cp[self.cp[c][1]][1]), jutf_dec(self.cp[n][1]), jutf_dec(self.cp[d][1])

    def build(self):
        out = bytearray(self.data[:8]); out += struct.pack('>H', len(self.cp))
        for e in self.cp[1:]:
            if e is None: continue
            t, v = e
            if t == 1:
                assert len(v) < 65536; out += bytes([1]) + struct.pack('>H', len(v)) + v
            elif t in (3, 4, 5, 6): out += bytes([t]) + v
            elif t in (7, 8): out += bytes([t]) + struct.pack('>H', v)
            else: out += bytes([t]) + struct.pack('>HH', *v)
        return bytes(out + self.rest)

def jutf_enc(s):
    """Modified UTF-8 (như DataOutput.writeUTF)."""
    out = bytearray()
    for ch in s:
        c = ord(ch)
        if c > 0xFFFF:
            c -= 0x10000
            for u in (0xD800 + (c >> 10), 0xDC00 + (c & 0x3FF)):
                out += bytes([0xE0 | (u >> 12), 0x80 | ((u >> 6) & 0x3F), 0x80 | (u & 0x3F)])
        elif 0 < c < 0x80: out.append(c)
        elif c < 0x800: out += bytes([0xC0 | (c >> 6), 0x80 | (c & 0x3F)])
        else: out += bytes([0xE0 | (c >> 12), 0x80 | ((c >> 6) & 0x3F), 0x80 | (c & 0x3F)])
    return bytes(out)

def jutf_dec(b):
    out = []; i = 0
    while i < len(b):
        x = b[i]
        if x < 0x80: out.append(x); i += 1
        elif x & 0xE0 == 0xC0: out.append(((x & 0x1F) << 6) | (b[i+1] & 0x3F)); i += 2
        else: out.append(((x & 0x0F) << 12) | ((b[i+1] & 0x3F) << 6) | (b[i+2] & 0x3F)); i += 3
    return ''.join(map(chr, out))
