# Báo cáo verifier CLDC (PLAN T08/T09)

Mã: `vm-core/src/main/java/jlkvm/verify/{Verifier,MethodVerifier,Frame,VType,ClassHierarchy,SourceHierarchy,VerifyException,TypeMaps}.java`.
Kiểm thử: `vm-core/src/test/java/jlkvm/verify/*` (chạy bằng `./gradlew --no-daemon :vm-core:test`), sinh case bằng
`tests/vmspec/gen_verify_cases.sh` (ASM, `tests/vmspec/gen/VerifyGen.java`) và `tests/vmspec/gen_stress_corpus.sh`.

**Nhãn nguồn:** `[JVMS2]` đặc tả JVM 2nd ed. §4.9 (verifier suy luận cũ), `[JVMS3]` đặc tả JVM SE 7 §4.10.1 (type-checking dùng frame),
`[CLDC]` đặc tả CLDC 1.1 (JSR-139) chương "Class File Verification" (số mục theo quản đốc: phần 5).
**UNVERIFIED** = chưa đối chiếu được với văn bản đặc tả hoặc VM thật.

## 0. Nguồn đặc tả: không đọc được trong môi trường này

Trang đặc tả CLDC 1.1 trên jcp.org dẫn tới `download.oracle.com/otndocs/jcp/7247-j2me_cldc-1.1-fr-spec-oth-JSpec/`; đó là trang chấp nhận
giấy phép (không có văn bản đặc tả tải trực tiếp được), phần Javadoc trên docs.oracle.com chỉ chứa API. Vì vậy **mọi trích dẫn CLDC dưới đây
là từ trí nhớ về nội dung, không phải trích nguyên văn**, và số mục "5" lấy theo chỉ dẫn của quản đốc. phoneME không có (V4). Hệ quả: các
quy tắc đặc thù CLDC được đánh UNVERIFIED trong bảng 3; phần kiểu dữ liệu thuần tuý được kiểm chéo với HotSpot (mục 4).

## 1. Thiết kế

* **Strict (mặc định, D6):** chỉ tin thuộc tính `StackMap` (định dạng CLDC: offset tuyệt đối u2, long/double chiếm **một** phần tử trong
  frame, tag 0..8 = Top, Int, Float, Double, Long, Null, UninitThis, Object, Uninit). `StackMapTable` (Java 6) bị bỏ qua như mọi attribute lạ.
  Duyệt tuyến tính từng lệnh; tại offset có frame: trạng thái hiện tại phải *gán được* cho frame rồi lấy frame làm trạng thái; sau
  `goto/return/athrow/switch` lệnh kế bắt buộc có frame; mọi đích nhảy phải có frame và trạng thái lúc nhảy gán được cho nó; mỗi lệnh nằm
  trong vùng `try` phải có locals gán được cho frame của handler (stack handler = đúng 1 phần tử, kiểu catch gán được cho kiểu khai trong frame;
  `catch_type=0` coi như `Throwable`); rơi khỏi cuối code → từ chối.
* **Lenient (chỉ debug):** thử strict; nếu hỏng thì ghi cảnh báo (`Verifier.warnings()`) và suy luận bằng data-flow (hợp nhất kiểu, tổ tiên chung
  gần nhất qua `ClassHierarchy`, interface → `Object`). Vẫn từ chối mã sai kiểu thật sự (stack hụt/tràn, kiểu sai, jsr/ret, opcode lạ…).
* **Mô hình từ:** stack và locals theo từ 32-bit; long/double = 2 từ (`LONG,LONG2` / `DOUBLE,DOUBLE2`); `dup*/pop2/swap` kiểm không cắt đôi
  long/double; ghi vào nửa locals của cặp làm nửa còn lại thành `Top`.
* **Uninitialized:** `new` đẩy `uninit@pc`; `invokespecial <init>` thay MỌI bản sao trong stack+locals bằng kiểu đã khởi tạo; `uninitializedThis`
  ở `<init>` (trừ `java/lang/Object`); `<init>` phải gọi đúng lớp của lệnh `new` (hoặc lớp này/lớp cha với uninitThis); `return` trong `<init>` khi
  `this` chưa khởi tạo → từ chối; `new` cùng pc khi bản cũ còn sống → từ chối; **uninit còn trong stack/locals khi nhảy lùi → từ chối** (xem bảng 3).
* **Kiểu:** mảng `[I`… gán được cho `Object/Cloneable/Serializable` và mảng ref theo phần tử; **interface coi như Object** (đích là interface →
  nhận mọi ref; nguồn là interface chỉ gán được cho `Object`/interface); `null` gán được cho mọi ref; `aaload` từ `null` ra `null`.
* **jsr/jsr_w/ret → từ chối** ngay ở bước quét (`jsr/ret not allowed in preverified code`): preverifier CLDC inline subroutine nên mã đã
  preverify không còn jsr/ret. `Verifier` không suy luận subroutine, cả ở lenient. **UNVERIFIED** (không đọc được văn bản CLDC; ProGuard
  `-microedition` không bao giờ phát jsr/ret).
* **CLDC 1.0 (`cldc11=false`):** từ chối opcode float/double, `ldc` float, `newarray` float/double, field/method có F/D trong descriptor. Hành vi
  của KVM CLDC 1.0 thật (VerifyError hay ClassFormatError) **UNVERIFIED**.
* **`ldc` lớp:** chỉ hợp lệ khi class file ≥ 49 (JVMS); version 45–48 → từ chối. (ProGuard `-target 1.3` để lại `ldc Class` trong mã v47; đó là lý
  do 78 method của corpus stress bị từ chối đúng, xem mục 2.)
* **Lỗi:** `VerifyException(class, method, descriptor, pc, reason)`; VM ánh xạ sang lớp guest `java/lang/VerifyError` (CLDC không định nghĩa
  `VerifyError`; ánh xạ thật nằm ở `docs/error-mapping.md`, T12; **UNVERIFIED**). pc = lệnh gây lỗi (lỗi tại đích nhảy thì pc của lệnh nhảy;
  lỗi cấp method/exception table thì `-1`). Lớp không tìm thấy khi cần kiểm gán → `cannot load class X` (thiết bị thật có thể ném
  `NoClassDefFoundError` lúc verify; **UNVERIFIED**).

## 2. Kết quả (mọi con số do `./gradlew :vm-core:test` in ra)

| Tập | Chấp nhận | Từ chối | Ghi chú |
|---|---|---|---|
| `tests/vmspec` (40 jar, preverify + raw ASM) | 92 class | 3 class | từ chối = neg01 (thiếu StackMap), neg02 (jsr/ret), neg03 (float, profile CLDC 1.0) — đúng cả ba; `m05` raw được thêm StackMap để chạy strict |
| `tests/midlets` hello+selftest, `tools/env/cache/out/hello.jar`, jarscan `clean.jar` | 7 class | 0 | |
| Stub CLDC 1.1 + MIDP 2.0 (MicroEmulator) | 147 class | 0 | **không có class nào chứa StackMap** (stub không có nhánh), vẫn qua |
| Corpus stress: microemu-{cldc,midp,javase,javase-swing}, ecj 3.12.3, gson 2.9.0 qua ProGuard `-microedition -target 1.3` (6069/12135 method có StackMap) | 12 052 method | 78 method `ldc Class` ở v47, 5 method thiếu thư viện phụ thuộc | 0 từ chối ngoài ý muốn; hierarchy lấy từ JVM chủ |
| Case sinh bằng ASM (`VerifyGen`) | 16/16 positive | 42/42 negative (41 `nXX` + `p17`) | kiểm cả pc và chuỗi lý do; lenient đúng kỳ vọng 58/58 |
| Fuzz | 33 391 mẫu đột biến bytecode/StackMap + 3 282 class đột biến byte | — | 0 crash máy chủ: chỉ có chấp nhận hoặc `VerifyException` |

Case negative tự sinh (`tests/vmspec/build/verify/cases.txt`): stack sai kiểu (n01), stack hụt/tràn (n02, n03), nhánh tới frame không khớp (n04, n37),
nhánh không có frame (n05), rơi khỏi cuối code (n06), dùng uninit làm receiver/giá trị trả về (n07, n08), `<init>` thiếu super call / sai lớp / gọi hai lần
(n09, n10, n30), cắt đôi long (n11), đọc nửa long như int, ghi đè nửa rồi `lload` (n12, n13), frame handler sai (n14, n34), catch type không phải Throwable
(n15), `areturn`/`putfield`/arg sai kiểu (n16–n18), `iaload`/`aastore` trên mảng sai (n19, n39), `athrow` không phải Throwable (n20), jsr/ret (n21),
StackMap offset giữa lệnh / local vượt max_locals (n22, n23), mã chết không frame (n24), float ở CLDC 1.0 (n25, n38), opcode không tồn tại (n26),
uninit qua nhánh lùi (n27), `monitorenter` int (n28), `getfield` sai receiver (n29), `multianewarray`/`newarray` sai (n31, n32), `iinc` trên ref (n33),
local ngoài max_locals (n35), lớp không tồn tại (n36), receiver sai lớp (n41), `ifeq` trên long (n40).

## 3. Quy tắc, nguồn và điểm chưa chắc

| Quy tắc / hành vi | Nguồn | Trạng thái |
|---|---|---|
| Kiểu dữ liệu từng opcode, độ sâu stack, `max_stack`/`max_locals`, long/double 2 từ | [JVMS2] 4.9.2, [JVMS3] 4.10.1.9 | khớp HotSpot-21 trên toàn bộ case lỗi kiểu (mục 4) |
| Frame tại đích nhảy / handler, gán được, fall-through vào frame | [JVMS3] 4.10.1.4–4.10.1.6, định dạng CLDC | đúng theo type-checking; việc KVM so sánh *gán được* hay *bằng nhau* ở fall-through: **UNVERIFIED** |
| Thiếu StackMap ở method có nhánh/handler → từ chối; method không cần frame thì không cần attribute | [CLDC] ch.5 | **UNVERIFIED** (nội dung nhớ) |
| jsr/jsr_w/ret bị từ chối | [CLDC] preverifier inline subroutine | **UNVERIFIED** |
| `uninit` trong stack/locals khi nhảy lùi bị từ chối | [JVMS2] 4.9.4 (giữ lại từ verifier cũ; CLDC suy ra từ nó) | **UNVERIFIED**; chặt hơn type-checking [JVMS3]; có thể từ chối thứ phoneME chấp nhận (ctor có vòng lặp trước `super()`: chưa gặp trong corpus) |
| `uninit` trong locals dưới vùng handler (JVMS2 4.9.4 gạch đầu dòng 3) | [JVMS2] | **chưa cài**; khung handler tự khai báo locals nên kiểu hệ thống cho phép |
| Truy cập `protected` (receiver phải là lớp hiện tại hoặc lớp con) | [JVMS3] 4.10.1.8 | **chưa cài** (không có trong CLDC verifier nhớ được; chấp nhận nhiều hơn) |
| Kiểm tồn tại/quyền truy cập field/method, final, abstract… | JVMS 5.4 | để T12 (liên kết lười, D7), không phải verifier |
| `invokespecial` thường: receiver gán được cho lớp hiện tại và lớp tham chiếu phải là lớp này hoặc tổ tiên | [JVMS3] 4.10.1.9 | cài |
| `lookupswitch` khoá phải tăng dần; padding switch không kiểm | [JVMS2] 4.9.1 | khoá: cài; padding: bỏ qua |
| `<init>` chỉ được gọi bằng `invokespecial`; `<clinit>` không được gọi | JVMS 4.9 | cài |
| Version class > 49 | T07 | do parser từ chối (ClassFormatError) |
| Ánh xạ lỗi sang lớp guest (VerifyError / ClassFormatError / NoClassDefFoundError) | CLDC chỉ có tập lỗi nhỏ | **UNVERIFIED** (T12) |

## 4. Đối chiếu độc lập

* **phoneME / KVM thật: không có (V4).** Không có tập chấp nhận/từ chối phoneME để so; không khẳng định "khớp phoneME".
* **HotSpot (JDK 21, `-Xverify:all`)** nạp 58 case sinh bằng ASM (`tests/vmspec/gen/HotspotVerify.java`). Khác biệt (HotSpot dùng verifier
  suy luận cho class < 50, không biết StackMap CLDC): HotSpot **chấp nhận** n04, n05, n14, n21, n22, n23, n24, n25, n34, n37, n38 và p17 (đều là
  quy tắc riêng CLDC: frame sai/thiếu, jsr/ret, float ở CLDC 1.0) — đúng như kỳ vọng. HotSpot **từ chối** mọi case còn lại, kể cả toàn bộ lỗi
  kiểu thuần tuý (n01–n03, n06–n13, n15–n20, n26–n33, n35, n36, n39–n41). `p08` báo `NoClassDefFoundError` vì HotSpot không có
  `javax.microedition.lcdui.CommandListener` (không liên quan verifier). Không có chiều "verifier này từ chối mà HotSpot chấp nhận" ngoài các quy tắc
  CLDC liệt kê ở trên.
* FreeJ2ME/MicroEmulator chỉ là JVM chủ, không kiểm chứng thêm gì về CLDC (D18).

## 5. Việc còn lại (không chặn T08/T09)

* Khi có phoneME hoặc thiết bị: chạy lại `tests/vmspec/build/verify/*.class` (và 3 case neg*) để kiểm các dòng UNVERIFIED, nhất là jsr/ret, fall-through
  vào frame, uninit nhảy lùi, và lớp lỗi.
* `TypeMaps` (T09): `at(cf,m,pc)` = trạng thái *trước* lệnh; `atCall(cf,m,pc)` = frame của caller *trong lúc* invoke chạy (đã bỏ receiver+tham số).
  Ref = `null`, object/array, `uninit`, `uninitializedThis`; kết quả được cache theo method (nhận dạng đối tượng `MethodInfo`) và dùng chung các bitmap
  trùng nhau. Interpreter (T11) phải dùng đúng quy ước "trước lệnh" khi chuyển luồng/GC.
