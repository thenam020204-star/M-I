"""Bộ đọc .class tối giản (thuần Python) dùng chung cho inventory.py và jar_check.py."""
import struct

# độ dài lệnh JVM (0 = đặc biệt: switch/wide); dùng để duyệt bytecode chính xác
_LEN = [1] * 256
for _op in (0x10, 0x12, 0x15, 0x16, 0x17, 0x18, 0x19, 0x36, 0x37, 0x38, 0x39, 0x3a, 0xa9, 0xbc):
    _LEN[_op] = 2
for _op in list(range(0x99, 0xa9)) + [0x11, 0x13, 0x14, 0x84, 0xb2, 0xb3, 0xb4, 0xb5, 0xb6, 0xb7,
                                      0xb8, 0xbb, 0xbd, 0xc0, 0xc1, 0xc6, 0xc7]:
    _LEN[_op] = 3
for _op in (0xc5,):
    _LEN[_op] = 4
for _op in (0xb9, 0xba, 0xc8, 0xc9):
    _LEN[_op] = 5
_BRANCH = set(range(0x99, 0xaa)) | {0xaa, 0xab, 0xc6, 0xc7, 0xc8, 0xc9}


def _has_branch(code):
    i = 0
    while i < len(code):
        op = code[i]
        if op in _BRANCH:
            return True
        if op == 0xc4:                      # wide
            i += 6 if code[i + 1] == 0x84 else 4
        else:
            i += _LEN[op]
    return False


class ClassInfo(object):
    def __init__(self, data):
        self.major = self.minor = 0
        self.name = ''
        self.strings = []          # hằng String trong constant pool
        self.refs = set()          # (owner, name, desc) của Fieldref/Methodref/InterfaceMethodref
        self.members = set()       # (kind, name, desc) public/protected
        self.attrs = set()         # tên attribute xuất hiện trong mọi Code
        self.branches = False      # có lệnh rẽ nhánh -> cần StackMap sau preverify
        self._parse(data)

    def _parse(self, d):
        u2 = lambda o: struct.unpack_from('>H', d, o)[0]
        u4 = lambda o: struct.unpack_from('>I', d, o)[0]
        if u4(0) != 0xCAFEBABE:
            raise ValueError('not a class file')
        self.minor, self.major = u2(4), u2(6)
        n = u2(8); cp = [None] * n; o = 10; i = 1
        while i < n:
            t = d[o]
            if t == 1:
                l = u2(o + 1); cp[i] = ('utf', d[o + 3:o + 3 + l].decode('utf-8', 'replace')); o += 3 + l
            elif t in (3, 4):
                o += 5
            elif t in (5, 6):
                o += 9; i += 1
            elif t in (7, 8, 16, 19, 20):
                cp[i] = (t, u2(o + 1)); o += 3
            elif t in (9, 10, 11, 12, 18, 17):
                cp[i] = (t, u2(o + 1), u2(o + 3)); o += 5
            elif t == 15:
                o += 4
            else:
                raise ValueError('bad cp tag %d' % t)
            i += 1
        utf = lambda k: cp[k][1] if cp[k] and cp[k][0] == 'utf' else ''
        cls = lambda k: utf(cp[k][1])
        for e in cp:
            if not e:
                continue
            if e[0] == 8:
                self.strings.append(utf(e[1]))
            elif e[0] in (9, 10, 11):
                nt = cp[e[2]]
                self.refs.add((cls(e[1]), utf(nt[1]), utf(nt[2])))
        self.name = cls(u2(o + 2))
        o += 6; o += 2 + 2 * u2(o)
        for kind in ('field', 'method'):
            cnt = u2(o); o += 2
            for _ in range(cnt):
                acc, nm, ds = u2(o), utf(u2(o + 2)), utf(u2(o + 4))
                if acc & 0x0005:
                    self.members.add((kind, nm, ds))
                ac = u2(o + 6); o += 8
                for _ in range(ac):
                    an, al = utf(u2(o)), u4(o + 2)
                    if an == 'Code':
                        self._code_attrs(d, o + 6, utf)
                    o += 6 + al

    def _code_attrs(self, d, c, utf):
        u2 = lambda o: struct.unpack_from('>H', d, o)[0]
        u4 = lambda o: struct.unpack_from('>I', d, o)[0]
        clen = u4(c + 4)
        if _has_branch(d[c + 8:c + 8 + clen]):
            self.branches = True
        c += 8 + clen
        c += 2 + 8 * u2(c)
        n = u2(c); c += 2
        for _ in range(n):
            self.attrs.add(utf(u2(c)))
            c += 6 + u4(c + 2)


# API được phép gọi ra ngoài jar. Thêm prefix hãng nếu máy đích có (Nokia/Siemens/...).
ALLOWED_PREFIXES = ('java/lang/', 'java/io/', 'java/util/', 'javax/microedition/', 'javax/wireless/',
                    'com/nokia/mid/', 'com/siemens/', 'com/samsung/', 'com/motorola/',
                    'com/sonyericsson/', 'com/vodafone/', 'com/mot/', 'mmpp/')
