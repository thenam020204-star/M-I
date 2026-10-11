#!/bin/bash
# Tai tao jar Viet hoa tu jar goc: rename -> patch -> chen text -> preverify -> jar_check -> emu
set -e; cd "$(dirname "$0")"
bash scripts/build.sh
python3 skscripts/jar_check.py build/named.jar build/game.jar | tail -1
mkdir -p out; cp build/game.jar out/Heroes_Lore_Zero_VI.jar
python3 - <<'P'
import zipfile,os
m=zipfile.ZipFile('out/Heroes_Lore_Zero_VI.jar').read('META-INF/MANIFEST.MF').decode('latin1')
l=[x for x in m.replace('\r','').split('\n') if x.strip() and not x.startswith('Manifest-Version') and not x.startswith('Created')]
l+=['MIDlet-Jar-URL: Heroes_Lore_Zero_VI.jar','MIDlet-Jar-Size: %d'%os.path.getsize('out/Heroes_Lore_Zero_VI.jar')]
open('out/Heroes_Lore_Zero_VI.jad','w').write('\n'.join(l)+'\n')
P
[ -n "$NO_EMU" ] || python3 scripts/emu.py --fj tools/freej2me_plus.jar --jar out/Heroes_Lore_Zero_VI.jar --out out/shots --steps "wait 14; shot a; key Return; wait 6; shot b; key Return; wait 4; shot c; key Return; wait 4; shot d; key Return; wait 4; shot e; key Return; wait 6; shot f"
