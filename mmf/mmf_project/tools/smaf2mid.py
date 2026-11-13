"""SMAF -> Standard MIDI File.  Usage: python3 -I smaf2mid.py in.mmf out.mid [--two-pass]
Default: single full pass, 480 PPQ, tempo 1,920,000 us/beat => 1 MIDI tick = 1 SMAF tick = 4 ms.
--two-pass: layout of PLAN #4 (trimmed head, pass 1 + loop, no fade) for rendering by external synths."""
import sys, os
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import mido
from smaf_parse import *

PPQ = 480
TEMPO = 1920000  # 480 ticks * 4000 us

# SMAF program number (bank 7C/01 user FM voice) -> GM program (0-based). Composer's program numbers
# in this file look like GM numbers (0-based); we keep them identical. Table printed by main().
GM_MAP = {p: p for p in range(128)}

def build_midi(info, two_pass=False):
    ch = channel_setup(info)
    mid = mido.MidiFile(type=0, ticks_per_beat=PPQ)
    tr = mido.MidiTrack(); mid.tracks.append(tr)
    tr.append(mido.MetaMessage('set_tempo', tempo=TEMPO, time=0))
    tr.append(mido.MetaMessage('track_name', name='bgm_20 (SMAF MA-3)', time=0))
    abs_ev = []  # (tick, order, msg)
    for c in range(16):
        s = ch[c]
        if c == 9:
            abs_ev.append((0, 0, mido.Message('control_change', channel=9, control=0, value=0)))
            abs_ev.append((0, 0, mido.Message('program_change', channel=9, program=0)))
        else:
            abs_ev.append((0, 0, mido.Message('control_change', channel=c, control=0, value=0)))
            abs_ev.append((0, 0, mido.Message('control_change', channel=c, control=32, value=0)))
            abs_ev.append((0, 0, mido.Message('program_change', channel=c, program=GM_MAP[s['prog']])))
        abs_ev.append((0, 0, mido.Message('control_change', channel=c, control=7, value=s['vol'])))
        abs_ev.append((0, 0, mido.Message('control_change', channel=c, control=10, value=s['pan'])))
    if two_pass:
        lay, notes = two_pass_notes(info)
        end = int(round(lay['total_ms'] / TICK_MS))
        for n in notes:
            t = int(round(n['t_ms'] / TICK_MS)); d = int(round(n['dur_ms'] / TICK_MS))
            d = max(0, min(d, end - t))      # clamp note-off to the file end
            abs_ev.append((t, 2, mido.Message('note_on', channel=n['ch'], note=n['key'], velocity=n['vel'])))
            abs_ev.append((t + d, 1, mido.Message('note_off', channel=n['ch'], note=n['key'], velocity=0)))
        end = int(round(lay['total_ms'] / TICK_MS))
    else:
        for e in info['events']:
            if e['type'] == 'note':
                abs_ev.append((e['t'], 2, mido.Message('note_on', channel=e['ch'], note=e['key'], velocity=e['vel'])))
                abs_ev.append((e['t'] + e['gate'], 1, mido.Message('note_off', channel=e['ch'], note=e['key'], velocity=0)))
        end = info['events'][-1]['t']
    abs_ev.sort(key=lambda x: (x[0], x[1]))
    last = 0
    for t, _, m in abs_ev:
        tr.append(m.copy(time=t - last)); last = t
    tr.append(mido.MetaMessage('end_of_track', time=max(0, end - last)))
    return mid, end

if __name__ == '__main__':
    src, dst = sys.argv[1], sys.argv[2]
    info = parse_mmf(src)
    mid, end = build_midi(info, '--two-pass' in sys.argv)
    mid.save(dst)
    chs = channel_setup(info)
    notes = [e for e in info['events'] if e['type'] == 'note']
    print('wrote', dst, 'end tick', end, '= %.3f s' % (end * TICK_MS / 1000), 'notes', len(notes))
    print('ch  SMAF(bank,prog) -> GM prog   notes')
    for c in range(16):
        n = sum(1 for e in notes if e['ch'] == c)
        s = chs[c]
        print('%2d  %02x/%02x %3d -> %s  %d' % (c, s['bank'][0], s['bank'][1], s['prog'],
              'drum kit' if c == 9 else GM_MAP[s['prog']], n))
