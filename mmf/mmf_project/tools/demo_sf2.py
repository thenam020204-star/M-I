"""Demo + check for one song: MIDI (bank/program remapped through the sf2 map) -> FluidSynth + sf2  vs  direct MA-3 synth (V1 style).
Usage: python3 -I demo_sf2.py song.mmf game.sf2 game_map.csv out_dir [tmp_dir]
Writes out_dir/demo_<song>_sf2.flac (sf2 render, reverb/chorus off) and out_dir/demo_<song>_v1.flac (direct synth), prints spec_dist."""
import sys, os, subprocess
here = os.path.dirname(os.path.abspath(__file__))
song, sf2, mp, outd = sys.argv[1:5]
tmp = sys.argv[5] if len(sys.argv) > 5 else outd
name = os.path.splitext(os.path.basename(song))[0]
py = [sys.executable, '-I']
mid = os.path.join(tmp, 'demo_%s.mid' % name)
subprocess.run(py + [os.path.join(here, 'smaf2mid.py'), song, mid, '--two-pass', '--map', mp, '--song', name], check=True, stdout=subprocess.DEVNULL)
f7 = os.path.join(outd, 'demo_%s_sf2.flac' % name)
subprocess.run(py + [os.path.join(here, 'render_fluid.py'), mid, sf2, f7, '--reverb', '0', '--chorus', '0'], check=True, stdout=subprocess.DEVNULL)
subprocess.run(py + [os.path.join(here, 'ma3_synth.py'), song, tmp, 'v1', 'demo_' + name], check=True, stdout=subprocess.DEVNULL)
f1 = os.path.join(outd, 'demo_%s_v1.flac' % name)
os.replace(os.path.join(tmp, 'demo_%s_v1_ma3_fm_goc.flac' % name), f1)
subprocess.run(py + [os.path.join(here, 'spec_dist.py'), f1, f7], check=True)
subprocess.run(py + [os.path.join(here, 'check_audio.py'), f7, f1], check=True)
