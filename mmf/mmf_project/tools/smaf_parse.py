"""Parse SMAF/MMF (MA-3 Mobile Standard, uncompressed) into absolute-time events.

Usage: python3 -I smaf_parse.py <file.mmf> [--varlen standard|smaf]
Library: parse_mmf(path) -> dict
"""
import struct, sys

TICK_MS = 4.0  # gate/dur code 0x02 -> 4 ms (PLAN)

def chunks(buf, off, end):
    while off + 8 <= end:
        cid = bytes(buf[off:off+4]); ln = struct.unpack('>I', buf[off+4:off+8])[0]
        yield cid, off + 8, ln
        off += 8 + ln

def rd_var(b, p, mode):
    """duration/gate. standard: MIDI varlen. smaf: 1 or 2 bytes, 2-byte value +128."""
    if mode == 'standard':
        v = 0
        while True:
            c = b[p]; p += 1
            v = (v << 7) | (c & 0x7f)
            if not c & 0x80:
                return v, p
    c = b[p]; p += 1
    if c & 0x80:
        c2 = b[p]; p += 1
        return (((c & 0x7f) << 7) | (c2 & 0x7f)) + 128, p
    return c, p

def parse_mtsq(b, mode='standard'):
    p = 0; t = 0; ev = []; n = len(b)
    last_vel = [64] * 16   # 8n (no velocity) re-uses the channel's last 9n velocity, default 64 (ref: smaf825 event.go)
    byte_off_time = {}   # byte offset of event start -> tick
    ended = False
    while p < n:
        dstart = p
        d, p = rd_var(b, p, mode)
        t += d
        start = p
        byte_off_time[dstart] = t   # st/sp point at the delta byte of an event; value = tick of that event
        s = b[p]; p += 1
        if 0x80 <= s <= 0x9f:
            ch = s & 15
            key = b[p]; p += 1
            if s >= 0x90:
                vel = b[p]; p += 1
                last_vel[ch] = vel
            else:
                vel = last_vel[ch]
            g, p = rd_var(b, p, mode)
            ev.append(dict(t=t, type='note', ch=ch, key=key, vel=vel, gate=g, off=start))
        elif 0xb0 <= s <= 0xbf:
            ev.append(dict(t=t, type='cc', ch=s & 15, cc=b[p], val=b[p+1], off=start)); p += 2
        elif 0xc0 <= s <= 0xcf:
            ev.append(dict(t=t, type='prog', ch=s & 15, prog=b[p], off=start)); p += 1
        elif 0xd0 <= s <= 0xdf:
            ev.append(dict(t=t, type='chpress', ch=s & 15, val=b[p], off=start)); p += 1
        elif 0xe0 <= s <= 0xef:
            ev.append(dict(t=t, type='bend', ch=s & 15, lsb=b[p], msb=b[p+1], off=start)); p += 2
        elif s == 0xf0:
            ln, p = rd_var(b, p, 'standard')
            data = bytes(b[p:p+ln]); p += ln
            if data[-1:] != b'\xf7':
                raise ValueError('sysex w/o F7 at %d' % start)
            ev.append(dict(t=t, type='sysex', data=data, off=start))
        elif s == 0xff:
            m = b[p]; p += 1
            if m == 0x00:
                ev.append(dict(t=t, type='nop', off=start))
            elif m == 0x2f:
                if b[p] != 0: raise ValueError('bad EOT')
                p += 1; ended = True
                ev.append(dict(t=t, type='end', off=start))
                break
            else:
                raise ValueError('unknown meta %02x at %d' % (m, start))
        else:
            raise ValueError('bad status %02x at %d' % (s, start))
    if p != n or not ended:
        raise ValueError('parse ended at %d of %d (ended=%s)' % (p, n, ended))
    byte_off_time[p] = t
    return ev, byte_off_time

def parse_mmf(path, mode='standard'):
    buf = open(path, 'rb').read()
    if buf[:4] != b'MMMD':
        raise ValueError('not MMMD')
    total = struct.unpack('>I', buf[4:8])[0]
    end = min(len(buf), 8 + total)
    out = dict(path=path, size=len(buf))
    for cid, off, ln in chunks(buf, 8, end):
        if cid[:3] == b'MTR':
            hdr = buf[off:off+20]
            out['mtr_hdr'] = bytes(hdr)
            out['format'], out['seqtype'], out['dur'], out['gate'] = hdr[0], hdr[1], hdr[2], hdr[3]
            out['ch_status'] = list(hdr[4:20])
            for c2, o2, l2 in chunks(buf, off + 20, off + ln):
                body = buf[o2:o2+l2]
                if c2 == b'Mtsu': out['mtsu'] = bytes(body)
                elif c2 == b'MspI':
                    # "st:" u32 , "sp:" u32 (text-ish tag: 'st:' + 4 bytes + ',' + 'sp:' + 4 bytes + ',')
                    i = bytes(body).find(b'st:'); j = bytes(body).find(b'sp:')
                    out['st'] = struct.unpack('>I', body[i+3:i+7])[0]
                    out['sp'] = struct.unpack('>I', body[j+3:j+7])[0]
                elif c2 == b'Mtsq': out['mtsq'] = bytes(body)
                elif c2 == b'Mtsp': out['mtsp'] = bytes(body)
    out['events'], out['off_time'] = parse_mtsq(out['mtsq'], mode)
    return out

def parse_voices(mtsu):
    """Split Mtsu into sysex messages; classify."""
    from ma3_voice import split_sysex
    voices = []
    for body in split_sysex(mtsu):
        if body[:5] == b'\x43\x79\x06\x7f\x01' and body[5] == 0x7c:
            voices.append(dict(kind='melody', bank=(body[5], body[6]), prog=body[7], data=body[8:-1], raw=body))
        elif body[:5] == b'\x43\x79\x06\x7f\x01' and body[5] == 0x7d:
            voices.append(dict(kind='drum', bank=(body[5], body[6]), key=body[8], data=body[9:-1], raw=body))
        else:
            voices.append(dict(kind='other', raw=body))
    return voices

def loop_times(info):
    """Tick of loop start/end from byte offsets st/sp in Mtsq."""
    ot = info['off_time']
    return ot.get(info['st']), ot.get(info['sp'])

if __name__ == '__main__':
    path = sys.argv[1]
    mode = sys.argv[sys.argv.index('--varlen') + 1] if '--varlen' in sys.argv else 'standard'
    info = parse_mmf(path, mode)
    ev = info['events']
    print('Mtsq bytes: %d parsed OK, mode=%s' % (len(info['mtsq']), mode))
    print('format=%02x seq=%02x dur=%02x gate=%02x' % (info['format'], info['seqtype'], info['dur'], info['gate']))
    print('ch_status:', ' '.join('%02x' % c for c in info['ch_status']))
    end_tick = ev[-1]['t']
    last_note_end = max((e['t'] + e['gate'] for e in ev if e['type'] == 'note'), default=0)
    print('end tick %d (%.2f s), last note end %d (%.2f s)' % (end_tick, end_tick*TICK_MS/1000, last_note_end, last_note_end*TICK_MS/1000))
    from collections import Counter
    nc = Counter(e['ch'] for e in ev if e['type'] == 'note')
    print('notes per channel:', dict(sorted(nc.items())), 'total', sum(nc.values()))
    st, sp = loop_times(info)
    print('loop st byte %d -> tick %s ; sp byte %d -> tick %s' % (info['st'], st, info['sp'], sp))
    vs = parse_voices(info['mtsu'])
    print('voices in Mtsu: %d' % len(vs))
    for v in vs:
        if v['kind'] == 'melody':
            print(' melody bank %02x/%02x prog %02x len(data)=%d' % (v['bank'][0], v['bank'][1], v['prog'], len(v['data'])))
        elif v['kind'] == 'drum':
            print(' drum   bank %02x/%02x key  %02x len(data)=%d' % (v['bank'][0], v['bank'][1], v['key'], len(v['data'])))
        else:
            print(' other  %s' % v['raw'].hex(' '))


# ---------------------------------------------------------------- shared layout
HEAD_LEAD_MS = 100.0   # silence kept before the first note
TAIL_MS = -200.0       # end = last note-off of pass 2 minus 200 ms (already deep inside the 8 s fade)
FADE_MS = 8000.0

def loop_ticks(info):
    """(st_tick, sp_tick) or (None, None) when the file has no usable MspI loop."""
    try:
        st, sp = loop_times(info)
    except Exception:
        return None, None
    if st is None or sp is None or sp <= st:
        return None, None
    return st, sp

def layout(info):
    """PLAN design #4: pass 1 whole (events before sp), then [st, sp) once more, 8 s fade at the end. No MspI loop => one pass only.
    Head silence trimmed to HEAD_LEAD_MS; tail trimmed to last note-off + TAIL_MS."""
    st_tick, sp_tick = loop_ticks(info)
    notes = [e for e in info['events'] if e['type'] == 'note']
    first = min(e['t'] for e in notes) * TICK_MS
    if st_tick is None:
        L = 0.0
        last_end = max(e['t'] + e['gate'] for e in notes) * TICK_MS
    else:
        L = (sp_tick - st_tick) * TICK_MS
        inl = [e for e in notes if st_tick <= e['t'] < sp_tick]
        last_end = max((e['t'] + e['gate'] for e in inl), default=0) * TICK_MS
    shift = first - HEAD_LEAD_MS
    total = L + last_end - shift + TAIL_MS
    return dict(st_tick=st_tick, sp_tick=sp_tick, L_ms=L, first_ms=first, last_end_ms=last_end,
                shift_ms=shift, total_ms=total, looped=st_tick is not None)

def two_pass_timeline(info):
    """-> (layout, notes, ctrl). Rendered-time (ms, trimmed) notes AND channel events of both passes, in order, plus the per-channel
    state (bank msb/lsb, prog, vol, pan) in force when each note starts (program/bank changes inside the song are honoured)."""
    lay = layout(info)
    st, sp = lay['st_tick'], lay['sp_tick']
    raw = []
    for p in (0, 1):
        if p == 1 and not lay['looped']:
            break
        for e in info['events']:
            if e['type'] not in ('note', 'cc', 'prog'):
                continue
            if lay['looped']:
                if p == 0 and e['t'] >= sp: continue
                if p == 1 and not (st <= e['t'] < sp): continue
            t0 = e['t'] * TICK_MS + p * lay['L_ms'] - lay['shift_ms']
            raw.append((t0, p, e))
    # controllers before notes at equal time; stable otherwise
    raw.sort(key=lambda r: (r[0], 0 if r[2]['type'] != 'note' else 1))
    state = {i: dict(vol=100, pan=64, bank=(0, 0), prog=0) for i in range(16)}
    notes = []; ctrl = []
    for t0, p, e in raw:
        c = state[e['ch']]
        if e['type'] == 'cc':
            if e['cc'] == 7: c['vol'] = e['val']
            elif e['cc'] == 10: c['pan'] = e['val']
            elif e['cc'] == 0: c['bank'] = (e['val'], c['bank'][1])
            elif e['cc'] == 32: c['bank'] = (c['bank'][0], e['val'])
            ctrl.append(dict(t_ms=t0, ch=e['ch'], type='cc', cc=e['cc'], val=e['val']))
        elif e['type'] == 'prog':
            c['prog'] = e['prog']
            ctrl.append(dict(t_ms=t0, ch=e['ch'], type='prog', prog=e['prog']))
        else:
            notes.append(dict(ch=e['ch'], key=e['key'], vel=e['vel'], t_ms=t0, dur_ms=e['gate'] * TICK_MS, pas=p,
                              bank=c['bank'], prog=c['prog'], vol=c['vol'], pan=c['pan']))
    notes.sort(key=lambda n: (n['t_ms'], n['ch'], n['key']))
    return lay, notes, ctrl

def two_pass_notes(info):
    lay, notes, ctrl = two_pass_timeline(info)
    return lay, notes

def channel_setup(info):
    """Final static per-channel state from tick-0 CC/PC: dict ch -> {vol, pan, bank(msb,lsb), prog}."""
    ch = {i: dict(vol=100, pan=64, bank=(0, 0), prog=0) for i in range(16)}
    for e in info['events']:
        if e['t'] > 0:
            break
        if e['type'] == 'cc':
            c = ch[e['ch']]
            if e['cc'] == 7: c['vol'] = e['val']
            elif e['cc'] == 10: c['pan'] = e['val']
            elif e['cc'] == 0: c['bank'] = (e['val'], c['bank'][1])
            elif e['cc'] == 32: c['bank'] = (c['bank'][0], e['val'])
        elif e['type'] == 'prog':
            ch[e['ch']]['prog'] = e['prog']
    return ch
