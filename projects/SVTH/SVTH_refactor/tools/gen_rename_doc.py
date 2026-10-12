#!/usr/bin/env python3
"""gen_rename_doc.py — sinh docs/RENAME_MAP.md từ mapping/part_*.map (python3 -I tools/gen_rename_doc.py).
Mỗi dòng map: <loại> <spec> <mới> [conf]  # bằng chứng.  Đầu ra: thống kê + bảng theo cụm/class."""
import glob, re, os, collections

def parse(path):
    rows = []
    for ln in open(path, encoding='utf-8'):
        ln = ln.rstrip('\n')
        if not ln.strip() or ln.lstrip().startswith('#'):
            continue
        body, _, ev = ln.partition('#')
        w = body.split()
        if len(w) < 3 or w[0] not in 'CFM':
            continue
        conf = w[3] if len(w) > 3 else 'MED'
        rows.append((w[0], w[1], w[2], conf, ev.strip()))
    return rows

root = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
os.chdir(root)
files = sorted(glob.glob('mapping/part_*.map'))
stat = collections.Counter(); per = {}
for f in files:
    cl = os.path.basename(f)[5:-4]
    rows = parse(f); per[cl] = rows
    for k, spec, new, conf, ev in rows:
        stat[(cl, k, conf)] += 1
out = ['# RENAME_MAP — bảng đặt tên khôi phục mã nguồn (Phiên 5)', '',
       'Sinh tự động bởi `tools/gen_rename_doc.py` từ `mapping/part_*.map` (viết tay, có bằng chứng từng dòng). '
       'Tên cũ ở đây theo không gian tên GỐC (như `restored/raw/*.java`: field/hàm trùng tên có đuôi kiểu `_Z`, `_AB`…). '
       'Tra ngược: `mapping/reverse.tsv`. Giải thích chung và rủi ro: `REFACTOR_LOG.md` (Phiên 5); ghi chú từng cụm: `docs/rename_notes/<cụm>.md`.', '',
       '## Thống kê', '', '| Cụm | Class | Field | Hàm | HIGH | MED | LOW |', '|---|---:|---:|---:|---:|---:|---:|']
tot = collections.Counter()
for cl, rows in per.items():
    c = collections.Counter(k for k, *_ in rows); cf = collections.Counter(r[3] for r in rows)
    out.append(f"| {cl} | {c['C']} | {c['F']} | {c['M']} | {cf['HIGH']} | {cf['MED']} | {cf['LOW']} |")
    tot.update({'C': c['C'], 'F': c['F'], 'M': c['M'], 'HIGH': cf['HIGH'], 'MED': cf['MED'], 'LOW': cf['LOW']})
out.append(f"| **Tổng** | {tot['C']} | {tot['F']} | {tot['M']} | {tot['HIGH']} | {tot['MED']} | {tot['LOW']} |")
out += ['', 'Thành viên không có dòng trong bảng giữ **tên giữ chỗ** `<cũ>_<kiểu>` (vd. `a_Z`, `b_AI`); hàm chỉ thêm `_<kiểu trả về>` khi cần phân biệt. '
        '`Main`, `vh`, `fontcus` vốn đã đọc được. Hàm ghi đè API CLDC/MIDP (27 nhóm) bị đóng băng.', '']
for cl, rows in per.items():
    out += [f'## Cụm `{cl}`', '']
    cur = None
    for k, spec, new, conf, ev in rows:
        if k == 'C':
            out += ['', f'### `{spec}` → **{new}** ({conf}) — {ev}', '', '| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |', '|---|---|---|---|---|']
        else:
            out.append(f"| {'field' if k=='F' else 'hàm'} | `{spec}` | `{new}` | {conf} | {ev.replace('|','/')} |")
    out.append('')
open('docs/RENAME_MAP.md', 'w', encoding='utf-8').write('\n'.join(out))
print('docs/RENAME_MAP.md', len(out), 'dòng;', dict(tot))
