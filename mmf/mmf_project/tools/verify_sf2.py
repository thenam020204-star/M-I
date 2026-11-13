"""Independent checks of a SoundFont written by make_sf2.py (T7 acceptance).
Usage: python3 -I verify_sf2.py sf2 song.mmf [--synth-compare] [--keys 48,60,72]
 1. own RIFF parser: structure, terminal records, 46-sample pads, loop ranges, per-voice loop / no loop / loop+decay table
 2. click metrics at every loop join (step and slope step relative to the local median)
 3. FluidSynth (pyfluidsynth) load with stderr captured
 4. --synth-compare: one note held 6 s through FluidSynth vs direct synth render_fm_note (RMS dB curves)"""
import sys, os, struct, math, io
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import numpy as np

def parse_sf2(path):
    d = open(path, 'rb').read()
    assert d[:4] == b'RIFF' and d[8:12] == b'sfbk'
    p = 12; ch = {}
    while p < len(d):
        cid, ln = d[p:p + 4], struct.unpack('<I', d[p + 4:p + 8])[0]
        if cid == b'LIST':
            kind = d[p + 8:p + 12]; q = p + 12
            while q < p + 8 + ln:
                sid, sl = d[q:q + 4], struct.unpack('<I', d[q + 4:q + 8])[0]
                ch[(kind, sid)] = d[q + 8:q + 8 + sl]; q += 8 + sl + (sl & 1)
        p += 8 + ln + (ln & 1)
    return ch

def recs(b, fmt):
    n = struct.calcsize(fmt); return [struct.unpack_from(fmt, b, i * n) for i in range(len(b) // n)]

def load(path):
    ch = parse_sf2(path); g = lambda k: ch[(b'pdta', k)]
    sf = dict(smpl=np.frombuffer(ch[(b'sdta', b'smpl')], dtype='<i2'),
              phdr=recs(g(b'phdr'), '<20sHHHIII'), pbag=recs(g(b'pbag'), '<HH'), pgen=recs(g(b'pgen'), '<Hh'),
              inst=recs(g(b'inst'), '<20sH'), ibag=recs(g(b'ibag'), '<HH'), igen=recs(g(b'igen'), '<Hh'),
              shdr=recs(g(b'shdr'), '<20sIIIIIBbHH'))
    return sf

nm = lambda b: b.split(b'\0')[0].decode()

def gens_of(sf, bag_i, kind):
    bag, gen = (sf['ibag'], sf['igen']) if kind == 'i' else (sf['pbag'], sf['pgen'])
    return gen[bag[bag_i][0]:bag[bag_i + 1][0]]

def structure(sf):
    errs = []
    ns = len(sf['shdr']) - 1
    assert nm(sf['shdr'][-1][0]) == 'EOS' and nm(sf['inst'][-1][0]) == 'EOI' and nm(sf['phdr'][-1][0]) == 'EOP'
    sm = sf['smpl']
    for i, h in enumerate(sf['shdr'][:-1]):
        name, st, en, ls, le, sr, rk, pc, lk, ty = h
        if not (0 <= st < en <= len(sm)): errs.append('sample %s bad range' % nm(name))
        if sm[en:en + 46].any() or en + 46 > len(sm): errs.append('sample %s: 46 zero pad missing' % nm(name))
        if not (st <= ls < le <= en): errs.append('sample %s loop outside' % nm(name))
    # zone summary per instrument
    inst = []
    for j in range(len(sf['inst']) - 1):
        zs = []
        for bi in range(sf['inst'][j][1], sf['inst'][j + 1][1]):
            gg = {o: v for o, v in gens_of(sf, bi, 'i')}
            if gg.get(53, -1) >= ns: errs.append('inst %d bad sampleID' % j)
            zs.append(gg)
        inst.append((nm(sf['inst'][j][0]), zs))
    pres = []
    for j in range(len(sf['phdr']) - 1):
        name, pn, bk, bi = sf['phdr'][j][:4]
        gg = {o: v for o, v in gens_of(sf, bi, 'p')}
        pres.append((bk, pn, nm(name), gg.get(41)))
    return errs, inst, pres

def click_table(sf, inst, verbose=False):
    rows = []
    sm = sf['smpl'].astype(np.float64)
    for iname, zs in inst:
        for gg in zs:
            if gg.get(54, 0) & 3 != 1: continue
            sid = gg[53]; name, st, en, ls, le, sr, rk, pc, lk, ty = sf['shdr'][sid]
            x = sm[st:en]; a, b = ls - st, le - st
            tail_ok = np.array_equal(x[b:b + 8], x[a:a + 8])
            n0 = min(4000, b - a)
            z = np.concatenate([x[b - n0:b], x[a:a + 4000]]); j = n0
            d = np.diff(z); dd = np.diff(d)
            step = abs(d[j - 1]); sl = abs(dd[j - 2])
            r1 = step / (np.median(np.abs(d)) + 1e-9); r2 = sl / (np.median(np.abs(dd)) + 1e-9)
            r1p = step / (np.percentile(np.abs(d), 99) + 1e-9)
            rows.append((iname, nm(name), r1, r2, r1p, tail_ok, abs(z[j] - z[j - 1]) / (np.sqrt(np.mean(z ** 2)) + 1e-9)))
    return rows

def fluid_load(path):
    import fluidsynth, tempfile
    err = tempfile.TemporaryFile(); old = os.dup(2); os.dup2(err.fileno(), 2)
    try:
        syn = fluidsynth.Synth(samplerate=44100.0)
        sfid = syn.sfload(path)
        ok = {}
        for bank, pn in [(0, 25), (128, 0)]:
            ok[(bank, pn)] = syn.program_select(0, sfid, bank, pn)
        syn.delete()
    finally:
        os.dup2(old, 2); os.close(old)
    err.seek(0); return sfid, err.read().decode(errors='replace')

def synth_compare(path, mmf, keys, gain_g):
    import fluidsynth, make_sf2 as M, ma3_synth as S
    song = M.load_song(mmf)
    out = []
    for pc, (h, fm, lo, hi) in sorted(song['melody'].items()):
        for key in keys:
            syn = fluidsynth.Synth(samplerate=44100.0, gain=0.5); sfid = syn.sfload(path)
            syn.set_reverb(0, 0, 0, 0); syn.set_chorus(0, 0.0, 0.3, 1.0, 0)
            syn.program_select(0, sfid, 0, pc); syn.noteon(0, key, 127)
            a = np.asarray(syn.get_samples(6 * 44100), dtype=np.float64).reshape(-1, 2)
            syn.noteoff(0, key)
            b = np.asarray(syn.get_samples(3 * 44100), dtype=np.float64).reshape(-1, 2)
            syn.delete()
            f = np.sqrt((np.concatenate([a, b]) ** 2).mean(axis=1)) / 32768.0
            ref = M.to_44k(S.render_fm_note(fm, key, int(6 * S.SR)))[:9 * 44100]
            ref = np.concatenate([ref, np.zeros(9 * 44100 - len(ref))])
            blk = 2205
            def db(v): return 20 * np.log10(np.sqrt((v[:len(v) // blk * blk].reshape(-1, blk) ** 2).mean(axis=1)) + 1e-12)
            df, dr = db(f), db(ref)
            # compare where both are above -75 dB of the reference peak
            valid = dr > dr.max() - 60
            off = np.median((df - dr)[valid][:20])
            diff = (df - dr - off)
            t = np.arange(len(diff)) * 0.05
            hold = valid & (t < 6); rel = valid & (t >= 6)
            out.append((pc, key, off, diff[hold].mean() if hold.any() else float('nan'), np.abs(diff[hold]).mean() if hold.any() else float('nan'),
                        diff[rel].mean() if rel.any() else float('nan'), np.abs(diff[rel]).mean() if rel.any() else float('nan')))
    return out

if __name__ == '__main__':
    path = sys.argv[1]; mmf = sys.argv[2]
    sf = load(path)
    errs, inst, pres = structure(sf)
    print('file %d B, samples %d (%d pts = %.1f MB), instruments %d, presets %d' % (os.path.getsize(path), len(sf['shdr']) - 1, len(sf['smpl']), len(sf['smpl']) * 2 / 1e6, len(inst), len(pres)))
    print('structure errors:', errs or 'none')
    print('presets:', [(b, p, n) for b, p, n, _ in pres])
    print('\nper instrument: zones, loop / one-shot / loop+decay(36), a(s) range, L(s) range')
    for iname, zs in inst:
        if iname.startswith('D0') or iname.startswith('MA3'): continue
        nl = sum(1 for g in zs if g.get(54, 0) & 3 == 1); nd = sum(1 for g in zs if g.get(54, 0) & 3 == 1 and 36 in g)
        a = [(sf['shdr'][g[53]][3] - sf['shdr'][g[53]][1]) / 44100 for g in zs if g.get(54, 0) & 3 == 1]
        L = [(sf['shdr'][g[53]][4] - sf['shdr'][g[53]][3]) / 44100 for g in zs if g.get(54, 0) & 3 == 1]
        n = sum(sf['shdr'][g[53]][2] - sf['shdr'][g[53]][1] for g in zs)
        print('  %-20s zones %2d loop %2d (of which +decay %2d) one-shot %2d  a=%.2f..%.2f L=%.2f..%.2f  %.1f MB' % (
            iname, len(zs), nl - nd, nd, len(zs) - nl, min(a, default=0), max(a, default=0), min(L, default=0), max(L, default=0), n * 2 / 1e6))
    rows = click_table(sf, inst)
    print('\nclick metrics over %d loop joins (step / median|dx|, slope-step / median|ddx|, step / p99|dx|, data after loop end == loop start: %s)' % (
        len(rows), all(r[5] for r in rows)))
    byv = {}
    for r in rows: byv.setdefault(r[0], []).append(r)
    print('  %-20s %5s %8s %8s %8s %8s %8s' % ('voice', 'n', 'step max', 'step avg', 'slope max', 'slope avg', 'step/p99 max'))
    for v, rr in byv.items():
        print('  %-20s %5d %8.2f %8.2f %8.2f %8.2f %8.2f' % (v, len(rr), max(r[2] for r in rr), np.mean([r[2] for r in rr]), max(r[3] for r in rr), np.mean([r[3] for r in rr]), max(r[4] for r in rr)))
    print('  ALL: step max %.2f avg %.2f | slope max %.2f avg %.2f | step/p99 max %.2f | step/rms max %.3f' % (
        max(r[2] for r in rows), np.mean([r[2] for r in rows]), max(r[3] for r in rows), np.mean([r[3] for r in rows]), max(r[4] for r in rows), max(r[6] for r in rows)))
    sfid, log = fluid_load(path)
    print('\nFluidSynth sfload id', sfid, '| stderr:', repr(log) if log.strip() else '(empty)')
    if '--synth-compare' in sys.argv:
        keys = [int(k) for k in (sys.argv[sys.argv.index('--keys') + 1] if '--keys' in sys.argv else '48,60,72').split(',')]
        res = synth_compare(path, mmf, keys, None)
        print('\nFluidSynth vs direct synth, note held 6 s then released 3 s; RMS dB in 50 ms blocks, offset removed (median of first 1 s)')
        print('  pc  key  offset  hold(0-6s) mean / mean|d|   release(6-9s) mean / mean|d|')
        for r in res: print('  %3d %3d %+6.1f   %+6.2f / %5.2f              %+6.2f / %5.2f' % r)
        print('  MEAN|d| hold %.2f dB  release %.2f dB' % (np.nanmean([r[4] for r in res]), np.nanmean([r[6] for r in res])))
