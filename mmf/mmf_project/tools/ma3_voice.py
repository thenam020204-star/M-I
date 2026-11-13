"""Decode MA-3 'VM3 exclusive' FM voices (SysEx 43 79 06 7F 01 ...) from Mtsu.
Layout reference: but80/smaf825 smaf/voice/vm35fm.go (VM3Exclusive) + smaf/subtypes/exclusive.go, read as documentation.
Header after 43 79 06 7F 01: bankMSB, bankLSB, PC, DrumNote, VoiceType(0=FM,1=PCM), then 36 bytes
(4 global + 4 ops x 8) with 7-bit MSB packing."""
ALG_OPS = {0: 2, 1: 2}

def unpack_fm(raw36):
    """36 B (4 global + 4 ops x 8) or 20 B (4 global + 2 ops x 8, HL4 / Y933 2-op voices)."""
    raw = bytearray(raw36)
    assert len(raw) in (20, 36)
    nop = (len(raw) - 4) // 8
    raw[2] |= (raw[0] << 2) & 0x80
    raw[3] |= (raw[0] << 3) & 0x80
    for op in range(nop):
        raw[4 + op*8] |= (raw[op*8] << 4) & 0x80
        raw[5 + op*8] |= (raw[op*8] << 5) & 0x80
        raw[6 + op*8] |= (raw[op*8] << 6) & 0x80
        raw[7 + op*8] |= (raw[op*8] << 7) & 0x80
        raw[10 + op*8] |= (raw[8 + op*8] << 2) & 0x80
        raw[11 + op*8] |= (raw[8 + op*8] << 3) & 0x80
    fixed = bytes(raw[1:4])
    for op in range(nop):
        fixed += bytes(raw[4 + op*8: 8 + op*8]) + bytes(raw[9 + op*8: 12 + op*8])
    return fixed   # 3 + 28 bytes

def decode_fm(raw36):
    f = unpack_fm(raw36)
    nop = (len(f) - 3) // 7
    g = f[:3]
    v = dict(drumkey=g[0], panpot=g[1] >> 3, bo=g[1] & 3, lfo=g[2] >> 6 & 3, pe=bool(g[2] & 0x20), alg=g[2] & 7, ops=[])
    for i in range(nop):
        d = f[3 + i*7: 10 + i*7]
        v['ops'].append(dict(
            sr=d[0] >> 4, xof=bool(d[0] & 8), sus=bool(d[0] & 2), ksr=bool(d[0] & 1),
            rr=d[1] >> 4, dr=d[1] & 15, ar=d[2] >> 4, sl=d[2] & 15,
            tl=d[3] >> 2, ksl=d[3] & 3,
            dam=d[4] >> 5 & 3, eam=bool(d[4] & 0x10), dvb=d[4] >> 1 & 3, evb=bool(d[4] & 1),
            multi=d[5] >> 4, dt=d[5] & 7, ws=d[6] >> 3, fb=d[6] & 7))
    v['nop'] = nop
    while len(v['ops']) < 4:                  # 2-op voices: pad with a silent op (ALG 0/1 never use ops 3/4)
        v['ops'].append(dict(v['ops'][-1], tl=63, ar=0))
    return v

def parse_sysex_voice(body):
    """body = sysex payload after F0 <len> (ends with F7). Returns dict or None."""
    if body[:5] != b'\x43\x79\x06\x7f\x01':
        return None
    d = dict(bank_msb=body[5], bank_lsb=body[6], pc=body[7], drumnote=body[8], vtype=body[9])
    pay = body[10:-1]
    if d['vtype'] == 0:
        d['fm'] = decode_fm(pay)
        d['len'] = len(pay)
    else:
        d['pcm_raw'] = bytes(pay)
    return d

def split_sysex(mtsu):
    """Mtsu = sequence of F0 <MIDI varlen length> <body ending in F7>. Yields bodies (length is a 7-bit varlen, >127 B possible)."""
    p = 0
    while p < len(mtsu):
        if mtsu[p] != 0xf0:
            raise ValueError('bad sysex start at %d' % p)
        p += 1; ln = 0
        while True:
            c = mtsu[p]; p += 1; ln = (ln << 7) | (c & 0x7f)
            if not c & 0x80: break
        body = mtsu[p:p + ln]; p += ln
        if len(body) != ln or not body or body[-1] != 0xf7:
            raise ValueError('sysex without F7 / truncated')
        yield bytes(body)

def decode_all(mtsu):
    out = []
    for body in split_sysex(mtsu):
        v = parse_sysex_voice(body)
        if v: out.append(v)
    return out

class VoiceBank:
    """All voices of one Mtsu, keyed by (bankMSB, bankLSB, program) for melody and (bankMSB, bankLSB, drumnote) for drums.
    Drum voices (bank 7D) carry a pc too (2 in every file seen); lookup prefers the channel's program, else any pc."""
    def __init__(self, mtsu):
        self.melody = {}; self.drum = {}; self.waves = {}; self.skipped = []
        for body in split_sysex(mtsu):
            if body[:5] == b'\x43\x79\x06\x7f\x03':
                self.waves[body[5]] = bytes(body[7:-1]); continue
            v = parse_sysex_voice(body)
            if not v:
                continue
            v['raw'] = bytes(body)
            if v['bank_msb'] == 0x7c and v['vtype'] == 0:
                self.melody[(0x7c, v['bank_lsb'], v['pc'])] = v
            elif v['bank_msb'] == 0x7d:
                self.drum.setdefault((0x7d, v['bank_lsb'], v['drumnote']), []).append(v)
            else:
                self.skipped.append((v['bank_msb'], v['bank_lsb'], v['pc'], v['vtype']))

    def find(self, bank, prog, key):
        """-> voice dict or None. Drum bank (MSB 7D): by key; melody: by program."""
        if bank[0] == 0x7d:
            vs = self.drum.get((0x7d, bank[1], key)) or []
            for v in vs:
                if v['pc'] == prog: return v
            return vs[0] if vs else None
        return self.melody.get((bank[0], bank[1], prog))

def pcm_wave_id(v):
    """WaveID of a PCM (type 1) voice: byte +15 of the 19-byte record (smaf825 vm35pcm.go layout; RM flag unknown, low 7 bits)."""
    return v['pcm_raw'][15] & 0x7f

if __name__ == '__main__':
    import sys, os
    sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
    from smaf_parse import parse_mmf
    info = parse_mmf(sys.argv[1])
    for v in decode_all(info['mtsu']):
        if 'fm' in v:
            f = v['fm']
            print('FM  bank %02x/%02x pc=%3d  BO=%d LFO=%d ALG=%d PAN=%d PE=%d drumkey=%d' % (v['bank_msb'], v['bank_lsb'], v['pc'], f['bo'], f['lfo'], f['alg'], f['panpot'], f['pe'], f['drumkey']))
            n = 2 if f['alg'] < 2 else 4
            for i in range(n):
                o = f['ops'][i]
                print('   op%d MUL=%2d DT=%d WS=%2d FB=%d | AR=%2d DR=%2d SR=%2d RR=%2d SL=%2d | TL=%2d KSL=%d | KSR=%d XOF=%d SUS=%d | EAM=%d DAM=%d EVB=%d DVB=%d' % (
                    i+1, o['multi'], o['dt'], o['ws'], o['fb'], o['ar'], o['dr'], o['sr'], o['rr'], o['sl'], o['tl'], o['ksl'], o['ksr'], o['xof'], o['sus'], o['eam'], o['dam'], o['evb'], o['dvb']))
        else:
            print('PCM bank %02x/%02x pc=%d key=%d raw=%s' % (v['bank_msb'], v['bank_lsb'], v['pc'], v['drumnote'], v['pcm_raw'].hex(' ')))
