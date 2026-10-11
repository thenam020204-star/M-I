#!/usr/bin/env bash
# chạy jar trên FreeJ2ME theo kịch bản phím, ghép ảnh thành lưới 4 cột: shots.sh <jar> <out_dir> "<steps>"
set -e
P=$(cd "$(dirname "$0")/.." && pwd)
[ -f "$P/tools/env.sh" ] && source "$P/tools/env.sh" || source "$P/work/tools/env.sh"
timeout 900 python3 -I "$P/scripts/run_emu.py" --fj "$FJ" --jar "$1" --out "$2" --size 240x320 --steps "$3" > "$2.json" 2>&1 || true
python3 -I - "$2" <<'P'
import sys, json, os
from PIL import Image
d = sys.argv[1]; j = json.load(open(d + '.json'))
ims = [Image.open(s).convert('RGB') for s in j['shots']]
c = 4; r = (len(ims) + c - 1) // c
o = Image.new('RGB', (240 * c, 320 * r), (40, 40, 40))
for k, im in enumerate(ims): o.paste(im, ((k % c) * 240, (k // c) * 320))
o.save(d + '/grid.png'); print('exceptions:', [e[:150] for e in j['exceptions']])
P
