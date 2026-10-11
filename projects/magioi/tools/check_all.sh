#!/bin/sh
# Kiem tra tung phan. Thoat != 0 neu co phan hong.
cd "$(dirname "$0")"; R=0
KIT=pixeltext_kit; [ -d "$KIT" ] || KIT=pixeltext-kit   # ten thu muc khi chay tu cay sach / tu setup_tools.sh
step() { printf '%-34s' "$1"; shift; if out=$("$@" 2>&1); then echo OK; else echo FAIL; echo "$out" | tail -5; R=1; fi; }
step "1 font .bin hop le (7 file)"   python3 - <<P
import sys,os; sys.path.insert(0,'$KIT')
from pixeltext import container as c
ref=c.decode('fonts/f18.bin')
assert sorted(os.listdir('fonts'))==['f13.bin','f13c.bin','f13s.bin','f15.bin','f15c.bin','f15s.bin','f18.bin'],os.listdir('fonts')
for n in ('18','15','13','15s','13s','15c','13c'):
    f=c.decode('fonts/f%s.bin'%n)
    assert f['height']==int(n.rstrip('sc')) and f['chars']==ref['chars'] and len(f['chars'])==227,(n,'header')
    assert len(f['trailing'])==0,(n,'trailing bytes')
    assert c.encode(f['chars'],f['height'],f['widths'],f['rows'])==open('fonts/f%s.bin'%n,'rb').read(),(n,'roundtrip')
P
step "1b f15/f13 = sinh lai tu f18"   sh -c 'T=$(mktemp -d) && cp -r fonts fontgen "$1" "$T"/ && python3 "$T"/fontgen/build_fonts.py >/dev/null && python3 "$T"/fontgen/build_fonts.py --check18 | grep -q "227/227" && for f in f15 f13 f15s f13s f15c f13c; do cmp fonts/$f.bin "$T"/fonts/$f.bin || exit 1; done' sh "$KIT"
step "2 pixeltext selftest"           python3 $KIT/selftest.py
step "3 pixeltext examples"           sh -c "cd /tmp && export PYTHONPATH=$PWD/$KIT && python3 $PWD/$KIT/examples/demo.py >/dev/null && python3 $PWD/$KIT/examples/game_menu.py >/dev/null; rc=\$?; rm -f $PWD/$KIT/examples/*.png; exit \$rc"
step "4 BitmapFont.class = version 47" sh -c '[ "$(od -An -tu1 -j7 -N1 java/BitmapFont.class | tr -d " ")" = 47 ]'
step "5 FontDemo 7 font (javac+java)" sh -c 'D=$(mktemp -d) && javac -encoding UTF-8 --release 8 -nowarn -d $D java/test/stubs/javax/microedition/lcdui/*.java java/BitmapFont.java java/test/FontDemo.java 2>/dev/null && java -Djava.awt.headless=true -cp $D FontDemo fonts $D/p.png | grep -q ok && [ -s $D/p.png ]'
step "6 freej2me jar nguyen ven"      sh -c 'n=0; for j in freej2me/*.jar freej2me_plus.jar; do [ -f "$j" ] || continue; unzip -tq $j >/dev/null || exit 1; n=$((n+1)); done; [ $n -gt 0 ]'
exit $R
