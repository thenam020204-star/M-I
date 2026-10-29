# tests/vmspec (PLAN T06)

Edge-case corpus for the VM (M1/M2/M3). Every case is a tiny MIDlet (or a MIDlet plus generated raw bytecode) that prints
lines starting with `> `; `expected.txt` holds the lines the VM must print. Lines starting with `# ` are informational and
never compared. Where `expected.txt` comes from is written in `meta.txt` (`source:`) for every case.

    tests/vmspec/run_vmspec.sh              # build + run all cases on both oracles, compare with expected.txt
    tests/vmspec/run_vmspec.sh a01_div_zero # one case
    tests/vmspec/run_vmspec.sh --record     # (re)write expected.txt where the oracles agree; disagreements are flagged

Case layout: `cases/<id>/src/vmspec/*.java` (compiled `-source 1.3 -target 1.3`, preverified by `tools/env/j2mec.sh`),
optional `res/` (resources added to the jar), `MANIFEST.MF` (custom manifest), `build.sh` (custom jar build), `meta.txt`,
`expected.txt`. `gen/RawGen.java` (ASM) writes the raw-bytecode classes (old class file versions, no ACC_SUPER, ConstantValue
getstatic, monitor imbalance, jsr/ret, no StackMap, float) into `cases/*/raw/` on every run; they are not committed.

`meta.txt`: `mode: oracle|spec-only`, `source:`, `diverges:` (oracles known to deviate), `cldc:` (cldc10|cldc11), `notes:`.
`spec-only` cases cannot be run on the oracles (they accept what CLDC must reject); their `expected.txt` is the required
result for the project's own VM, taken from the cited spec text (the exact error class is marked UNVERIFIED).

**Honesty about the oracles (PLAN D18, R2):** MicroEmulator and FreeJ2ME are JVM-hosted, not CLDC VMs, so agreement
between them proves JVM semantics only. Anything CLDC-specific or device-specific is flagged UNVERIFIED in `meta.txt`.
phoneME could not be built (PLAN V4); once it can, re-run `--record` and review every difference.

## Coverage of "Ràng buộc và edge case" (PLAN)

| PLAN item | Cases |
|---|---|
| Số học: idiv/ldiv/irem/lrem /0, MIN/-1, dấu irem/lrem, shift mask, i2b/i2s/i2c, baload/caload | a01, a02, a03, a04, a05, a06 |
| Dấu phẩy động: f2i/d2i/f2l/d2l, fcmpl/fcmpg NaN; CLDC 1.0 không float | f01, f02, neg03 (spec-only) |
| Mảng: kích thước âm, aastore, multianewarray, chỉ số ngoài biên | r01, r02, r03, r04 |
| checkcast/instanceof mảng + interface; invokeinterface null | c01, c02 |
| Class file cũ không ACC_SUPER (45.x) | k01 (UNVERIFIED for KVM) |
| `<clinit>`: thứ tự, đệ quy, lỗi, ConstantValue | i01, i02, i03 (UNVERIFIED for CLDC), i04, k02 |
| String literal intern | s01 |
| Monitor: lệch enter/exit, synchronized method, wait timeout, interrupt | m01, m05, m02, m03, m04 |
| Tràn stack | t01 (error class UNVERIFIED) |
| Ngoại lệ trong paint()/callback | e01 (implementation-dependent) |
| Vòng lặp bận không sleep (preemption) | b01 |
| Native dài không đứng scheduler | n01 (the real test is T15) |
| Ảnh lớn tính vào heap (`imagesCountInHeap`) | img01 (device parameter, info only) |
| JAR: tên class khác hoa/thường, tên lạ | j03 |
| JAR: entry trùng, CRC hỏng | j04, j05 (oracle-only evidence) |
| JAR: manifest dòng nối | j02 (BOM case: TODO, no oracle accepts it reliably) |
| getResourceAsStream tương đối/tuyệt đối, phân biệt hoa thường | j01 |
| Mã hoá mặc định / UTF-8 / ISO-8859-1 (không viết lại bytecode) | enc01 |
| Verifier negative: thiếu StackMap, jsr/ret | neg01, neg02 (spec-only, used by T08) |
