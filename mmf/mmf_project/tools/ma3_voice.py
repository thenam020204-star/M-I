"""Decode MA-3 'VM3 exclusive' FM voices (SysEx 43 79 06 7F 01 ...) from Mtsu.
Layout reference: but80/smaf825 smaf/voice/vm35fm.go (VM3Exclusive) + smaf/subtypes/exclusive.go, read as documentation.
Header after 43 79 06 7F 01: bankMSB, bankLSB, PC, DrumNote, VoiceType(0=FM,1=PCM), then 36 bytes
(4 global + 4 ops x 8) with 7-bit MSB packing."""
ALG_OPS = {0: 2, 1: 2}

def unpack_fm(raw36):
    raw = bytearray(raw36)
    assert len(raw) == 36
    raw[2] |= (raw[0] << 2) & 0x80
    raw[3] |= (raw[0] << 3) & 0x80
    for op in range(4):
        raw[4 + op*8] |= (raw[op*8] << 4) & 0x80
        raw[5 + op*8] |= (raw[op*8] << 5) & 0x80
        raw[6 + op*8] |= (raw[op*8] << 6) & 0x80
        raw[7 + op*8] |= (raw[op*8] << 7) & 0x80
        raw[10 + op*8] |= (raw[8 + op*8] << 2) & 0x80
        raw[11 + op*8] |= (raw[8 + op*8] << 3) & 0x80
    fixed = bytes(raw[1:4])
    for op in range(4):
        fixed += bytes(raw[4 + op*8: 8 + op*8]) + bytes(raw[9 + op*8: 12 + op*8])
    return fixed   # 3 + 28 bytes

def decode_fm(raw36):
    f = unpack_fm(raw36)
    g = f[:3]
    v = dict(drumkey=g[0], panpot=g[1] >> 3, bo=g[1] & 3, lfo=g[2] >> 6 & 3, pe=bool(g[2] & 0x20), alg=g[2] & 7, ops=[])
    for i in range(4):
        d = f[3 + i*7: 10 + i*7]
        v['ops'].append(dict(
            sr=d[0] >> 4, xof=bool(d[0] & 8), sus=bool(d[0] & 2), ksr=bool(d[0] & 1),
            rr=d[1] >> 4, dr=d[1] & 15, ar=d[2] >> 4, sl=d[2] & 15,
            tl=d[3] >> 2, ksl=d[3] & 3,
            dam=d[4] >> 5 & 3, eam=bool(d[4] & 0x10), dvb=d[4] >> 1 & 3, evb=bool(d[4] & 1),
            multi=d[5] >> 4, dt=d[5] & 7, ws=d[6] >> 3, fb=d[6] & 7))
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

def decode_all(mtsu):
    out = []; p = 0
    while p < len(mtsu):
        assert mtsu[p] == 0xf0
        ln = mtsu[p+1]; body = mtsu[p+2:p+2+ln]; p += 2 + ln
        v = parse_sysex_voice(body)
        if v: out.append(v)
    return out

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
