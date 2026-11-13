"""Survey of SMAF files: python3 -I survey.py file... (header, Mtsu sysex classes, channel/bank/prog actually played, mid-song CC/PC)"""
import sys, os, collections
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from smaf_parse import parse_mmf
from ma3_voice import split_sysex
for path in sys.argv[1:]:
    try:
        info = parse_mmf(path)
    except Exception as e:
        print(os.path.basename(path), 'PARSE FAIL', e); continue
    name = os.path.basename(path)
    ev = info['events']
    cls = collections.Counter()
    try:
        for b in split_sysex(info.get('mtsu', b'')):
            if b[:5] == b'\x43\x79\x06\x7f\x01':
                cls['v%02x/%02x/t%d/L%d' % (b[5], b[6], b[9], len(b) - 11)] += 1
            elif b[:5] == b'\x43\x79\x06\x7f\x03': cls['wave%d(%dB)' % (b[5], len(b))] += 1
            else: cls['other:' + b[:6].hex()] += 1
    except Exception as e: cls['SPLITFAIL ' + str(e)] += 1
    state = {c: [0, 0, 0] for c in range(16)}; used = collections.Counter(); late = 0
    for e in ev:
        if e['type'] == 'cc':
            if e['cc'] == 0: state[e['ch']][0] = e['val']
            if e['cc'] == 32: state[e['ch']][1] = e['val']
            if e['t'] > 0 and e['cc'] in (0, 32): late += 1
        elif e['type'] == 'prog':
            state[e['ch']][2] = e['prog']
            if e['t'] > 0: late += 1
        elif e['type'] == 'note': used[(e['ch'], tuple(state[e['ch']]))] += 1
    print(name, 'fmt%02x dur%02x gate%02x' % (info['format'], info['dur'], info['gate']), 'mtsp' if 'mtsp' in info else '', 'st/sp', info.get('st'), info.get('sp'), 'ticks', ev[-1]['t'], 'mid-song bank/prog changes', late)
    print('   voices', dict(cls))
    print('   played (ch,(msb,lsb,prog)):n', {k: v for k, v in sorted(used.items())})
    print('   ch_status', info['ch_status'])
