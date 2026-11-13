"""Render a MIDI file with FluidSynth (pyfluidsynth) to FLAC.
Usage: python3 -I render_fluid.py in.mid soundfont.sf2 out.flac [--reverb 0|1] [--chorus 0|1] [--gain G]"""
import sys, os
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import numpy as np, mido, fluidsynth
from finish import master, write_flac

def arg(name, default):
    return sys.argv[sys.argv.index(name) + 1] if name in sys.argv else default

def main():
    midp, sfp, outp = sys.argv[1:4]
    sr = 44100
    rev = arg('--reverb', '1') == '1'; cho = arg('--chorus', '1') == '1'
    gain = float(arg('--gain', '0.5'))
    syn = fluidsynth.Synth(samplerate=float(sr), gain=gain)
    sfid = syn.sfload(sfp)
    assert sfid >= 0
    for c in range(16):
        syn.program_select(c, sfid, 128 if c == 9 else 0, 0)
    if rev:
        syn.set_reverb(roomsize=0.5, damping=0.3, width=0.8, level=0.6)
    else:
        syn.set_reverb(0, 0, 0, 0)
    if cho:
        syn.set_chorus(nr=3, level=1.5, speed=0.3, depth=6.0, type=0)
    else:
        syn.set_chorus(0, 0.0, 0.3, 1.0, 0)
    mid = mido.MidiFile(midp)
    tempo = 500000; tick = 0; evs = []
    for m in mid.tracks[0]:
        tick += m.time
        if m.type == 'set_tempo': tempo = m.tempo
        elif not m.is_meta: evs.append((tick, m))
        elif m.type == 'end_of_track': end_tick = tick
    sec_per_tick = tempo / 1e6 / mid.ticks_per_beat
    total = int(round((end_tick * sec_per_tick + 4.0) * sr))   # +4 s release room, trimmed below
    out = []; pos = 0
    def advance(to):
        nonlocal pos
        n = to - pos
        if n > 0:
            out.append(np.asarray(syn.get_samples(n), dtype=np.int16).reshape(-1, 2)); pos = to
    for tick, m in evs:
        advance(int(round(tick * sec_per_tick * sr)))
        if m.type == 'note_on' and m.velocity > 0: syn.noteon(m.channel, m.note, m.velocity)
        elif m.type in ('note_off', 'note_on'): syn.noteoff(m.channel, m.note)
        elif m.type == 'control_change': syn.cc(m.channel, m.control, m.value)
        elif m.type == 'program_change': syn.program_change(m.channel, m.program)
    keep = int(round(end_tick * sec_per_tick * sr))
    advance(keep)
    x = np.concatenate(out).astype(np.float64) / 32768.0
    print('raw peak int16: %d' % int(np.max(np.abs(x)) * 32768), 'frames', len(x))
    x = master(x, sr)
    write_flac(x, sr, outp, 16)
    print('wrote', outp)

main()
