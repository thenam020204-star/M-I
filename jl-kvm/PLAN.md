# PLAN: JL-KVM — chạy thẳng JAR J2ME (không dex hoá) trên nền JL-Mod, đủ Nokia API

## Mục tiêu
Fork JL-Mod, thay toàn bộ đường "JAR → dx → DEX → ART" bằng một máy ảo CLDC tự viết chạy bytecode gốc của JAR (interpreter + verifier + heap/GC riêng + green thread), để game chạy trên Android với hành vi gần KVM/Nokia thật nhất có thể. Toàn bộ API MIDP + Nokia UI API + các JSR có trên máy Nokia được dựng lại theo mô hình "lớp guest (bytecode CLDC) + native phía host (Android)". Kết quả phụ quan trọng: một môi trường kiểm thử nghiêm ngặt, bắt được patch "chỉ chạy trên J2ME Loader" (⚠️ NOT J2ME-SAFE).

---

## Hiện trạng JL-Mod (đã kiểm tra trực tiếp mã nguồn)
Bản khảo sát: `woesss/JL-Mod`, nhánh `dev`, commit `f723a19` (2026-04-11, "LCDUI: refactoring"), 634 file .java, ~73.6k dòng, Apache-2.0.

| Điểm | Bằng chứng | Hệ quả với mục tiêu "giống KVM thật" |
|---|---|---|
| Dex hoá lúc cài | `ru/woesss/j2me/installer/AppInstaller.java` gọi `com.android.dx.command.dexer.Main` với `--no-optimize`, ra `converted.zip`; JAR gốc chép thành `res.jar` | Phải bỏ bước này |
| Sửa bytecode lúc dex | `dexlib/.../dx/command/dexer/Main.java` gọi `AndroidProducer.instrument()`; `AndroidMethodVisitor` viết lại `Class.getResourceAsStream`, `Thread.yield→sleep`, `new String(byte[])`, `getBytes()`, `InputStreamReader`, `OutputStreamWriter`, `ByteArrayOutputStream.toString`, `PrintStream`… | Game không chạy đúng bytecode gốc → vi phạm binary integrity |
| Nạp class | `AppClassLoader extends DexClassLoader`; `CoreClassLoader` cho game thấy `java.*`, `javax.*`, `com.*` của **Android libcore** | Game gọi API ngoài CLDC (vd. `String.isEmpty()`, `ArrayList`) vẫn chạy → che lỗi |
| Bộ nhớ | `Runtime` là của Android, không có heap giả lập | `freeMemory()/totalMemory()` sai hoàn toàn; không có OOM như máy thật |
| Luồng | `MidletThread extends HandlerThread`; thread game = thread Android thật, preemptive, đa nhân | Race condition không bao giờ xảy ra trên KVM có thể xuất hiện và ngược lại |
| Thuộc tính hệ thống | `assets/defaults/system.props`: `microedition.configuration: CDLC-1.1` (gõ sai, phải là `CLDC-1.1`), `microedition.platform: Nokia 6233` | Cần profile thiết bị đúng |
| Nokia API có sẵn | `com/nokia/mid/ui`: FullCanvas, DirectGraphics(+Imp), DirectUtils, DeviceControl, SoftNotification(+Listener/Exception), Clipboard, FreeSizeFontInvoker; `com/nokia/mid/sound`: Sound, SoundListener, ToneVerifier; `com/nokia/mid/payment` (IAP); `com/nokia/mid/m3d` | Dùng làm backend, phải tách guest/host |
| JSR có sẵn | lcdui, game, media(+tone, control), rms, m3g, io.file, pim, bluetooth, obex, wireless.messaging, amms, sensor, pki | Tái dùng backend |
| JSR chưa có | `javax.microedition.m2g` (JSR-226), `location` (JSR-179), `khronos` (JSR-239), `javax.xml` (JSR-172), `global` (JSR-238), `content` (JSR-211), `apdu` (JSR-177 phần SATSA) | Bổ sung theo tầng ưu tiên |
| Native C/C++ | `cpp/m3g` (lõi M3G của Nokia, **EPL v1.0**, khoảng 323 khai báo native phía Java, có cả `m3g_kvm_api.inl`), `sonivox`, `mmapi_tsf` (TinySoundFont), `mmapi_eas`, `micro3d`, `gles_utils` | Giữ, viết lại lớp glue JNI |
| Đọc ZIP | `ru.woesss.util.zip.ZipFile` (chịu được JAR lỗi) | Tái dùng để đọc JAR trực tiếp |
| Thư viện kèm | ACRA, Gson, RxJava, Zip4j, PNGJ, MobileFFmpeg (**LGPL v3**), NoNonsense-FilePicker (**MPL 2.0**)… | Kiểm license khi phát hành |

---

## Phạm vi
- **Trong phạm vi**
  - VM CLDC 1.1 (có chế độ CLDC 1.0) viết bằng Java thuần, chạy bytecode từ JAR không giải nén, không dex.
  - Verifier CLDC (StackMap), heap/GC riêng có giới hạn theo thiết bị, green thread, monitor.
  - Thư viện guest: CLDC 1.1, MIDP 2.0/2.1, Nokia UI API (đầy đủ theo tầng), các JSR có trên máy Nokia.
  - Host Android (tái dùng backend JL-Mod) + host desktop headless (CI, so pixel).
  - Profile thiết bị Nokia, scanner tương thích, công cụ so sánh chéo (difftest).
- **Ngoài phạm vi (giai đoạn này)**
  - DoJa/i-appli, WIPI, BREW; API riêng Siemens/Samsung/Motorola/SE/Vodafone/KDDI (JL-Mod có, để đợt sau — kiến trúc cho phép thêm).
  - eSWT, MIDP 3.0, LWUIT ngoài phần game tự đóng gói.
  - JIT/AOT sinh mã. Không bao giờ quay lại dex hoá.
  - Kết nối mạng thật cho game online (chỉ chính sách cho phép/từ chối/offline).

---

## Quyết định thiết kế (KHÔNG tự đổi khi implement)
- **D1. Bỏ dex hoàn toàn; game chạy bằng interpreter.** — lý do: yêu cầu người dùng; dex + `AndroidProducer` thay đổi bytecode, không giữ hành vi gốc.
- **D2. Lõi VM viết bằng Java thuần (module `vm-core`, cấm `import android.*`).** — lý do: gọi thẳng backend Java của JL-Mod không qua JNI; cùng một lõi chạy được trên desktop JVM để test/CI/so sánh; ART JIT biên dịch vòng lặp interpreter. Hiệu năng được đo sớm (T13b). Nếu không đạt, phương án dự phòng là lõi C++ (NDK) cài cùng interface `ExecutionEngine` — vẫn là interpreter, không dex.
- **D3. Heap riêng: một mảng `int[]` (arena), tham chiếu là chỉ số `int`, `null = 0`.** Kích thước heap lấy từ profile thiết bị. Hết heap → GC → vẫn thiếu → `OutOfMemoryError` của guest. `Runtime.freeMemory/totalMemory` trả số liệu của arena. — lý do: tái hiện kỷ luật bộ nhớ và OOM như máy thật.
- **D4. GC chính xác (precise) mark–sweep + nén trượt (sliding compaction).** Bản đồ tham chiếu của từng frame lấy từ kết quả verifier tại pc đó (cache). Hỗ trợ `WeakReference` (CLDC 1.1). Hash định danh lưu trong header, sống qua nén. — lý do: không quét bảo thủ, không rò; giống cách KVM dùng stackmap.
- **D5. Green thread: toàn bộ thread guest chạy trên một thread host (`vm-thread`), chuyển thread sau N bytecode (N từ profile) hoặc khi block.** — lý do: tái hiện lập lịch KVM, xác định được (deterministic), GC dừng-thế-giới đơn giản.
- **D6. Verifier CLDC (type-checking dựa trên thuộc tính `StackMap`).** Chế độ `strict` (mặc định): class thiếu StackMap → `VerifyError` như máy thật. Chế độ `lenient` (chỉ debug): suy luận kiểu, chỉ cảnh báo. — lý do: preverify là bắt buộc trên thiết bị thật.
- **D7. Phân giải lười (lazy resolution) constant pool tại lần thực thi đầu.** Lỗi liên kết ném đúng thời điểm chạy tới, ánh xạ về lớp lỗi có trong CLDC (CLDC không có đủ cây Error của Java SE; cách ánh xạ cụ thể phải xác minh bằng oracle — xem T12).
- **D8. Mô hình API "guest + native host".** Mọi lớp game nhìn thấy (`java.*`, `javax.microedition.*`, `com.nokia.*`…) là **bytecode guest** trong ROM; phần cần nền tảng là phương thức `native` do host đăng ký theo khoá `"lớp.tên(descriptor)"`. Đối tượng có tài nguyên host giữ trường `int peer` → bảng handle phía host.
- **D9. Bề mặt API guest phải khớp 100% chữ ký stub chính thức** (lớp, superclass, interface, access flag, descriptor field/method, giá trị hằng, exception khai báo) của profile đang chạy. Không thừa thành viên public/protected nào. — lý do: game gọi API không có trên máy phải lỗi `NoSuchMethodError/NoClassDefFoundError` đúng như máy thật.
- **D10. API theo profile.** Thiết bị không có JSR nào thì lớp của JSR đó không tồn tại trong ROM khi chạy profile đó.
- **D11. Thư viện guest biên dịch `-source 1.3 -target 1.3`, `-bootclasspath` trỏ vào chính nó** (không bao giờ thấy rt.jar/android.jar), rồi chạy qua preverifier + verifier của chính dự án. — lý do: mã guest buộc phải là mã CLDC hợp lệ; tự kiểm verifier.
- **D12. Nguồn mã guest chỉ được lấy từ: viết mới; JL-Mod (Apache-2.0); MicroEmulator (song license LGPL/Apache — dùng nhánh Apache); Apache Harmony (Apache-2.0).** **Cấm chép mã phoneME (GPLv2)** vào repo. phoneME chỉ dùng làm oracle chạy cục bộ, không phân phối.
- **D13. Sự kiện MIDP chạy trong guest:** một thread guest hệ thống (`EventLoop`) nhận sự kiện từ host qua native chặn, đảm bảo tuần tự hoá `paint`/`keyPressed`/`commandAction`/`callSerially`/`serviceRepaints` theo MIDP. Host không bao giờ gọi vào guest từ thread Android khác.
- **D14. Chế độ xác định (deterministic) cho debug:** đồng hồ ảo, timeslice cố định, seed ngẫu nhiên cố định, ghi/phát lại input → tái hiện lỗi 100%.
- **D15. Debug hooks chỉ có trong bản debug** (cờ hằng biên dịch); bản release không ghi log nặng để không đổi timing/bộ nhớ.
- **D16. JAR đọc trực tiếp, không giải nén** (dùng `ru.woesss.util.zip.ZipFile` của JL-Mod). — lý do: tránh lỗi trùng tên class khác hoa/thường trên file system; giữ nguyên dữ liệu gốc.
- **D17. Engine dex cũ của JL-Mod chỉ giữ trong bản debug làm đối chứng A/B (T35), xoá khỏi release ở T42.**
- **D18. Thứ tự sự thật:** thiết bị thật > đặc tả CLDC/MIDP/Nokia > phoneME (RI của Sun) > KEmulator/MicroEmulator > JL-Mod. Lưu ý phoneME cũng **không phải** VM của Nokia; khác biệt giữa các oracle phải ghi lại, không tự chọn bừa.

### Cấu trúc thư mục
```
jl-kvm/                          (fork JL-Mod @ f723a19)
├─ app/                          UI JL-Mod (danh sách app, cài đặt, profile game, skin, bàn phím ảo)
├─ dexlib/                       giữ tạm cho A/B ở bản debug; xoá ở T42
├─ vm-core/                      Java thuần, KHÔNG android.*
│  └─ src/main/java/jlkvm/
│     ├─ classfile/   parser, constant pool, attribute
│     ├─ verify/      verifier CLDC + xuất bản đồ kiểu
│     ├─ link/        class table, phân giải, khởi tạo <clinit>
│     ├─ interp/      vòng lặp interpreter, frame, quickening
│     ├─ heap/        arena, bố cục object/array, cấp phát, temp roots
│     ├─ gc/          mark, sweep, compact, weak ref, hàng đợi dọn peer
│     ├─ thread/      scheduler green thread, monitor
│     ├─ natives/     registry, NativeContext, bảng handle, blocker
│     ├─ rom/         nạp ROM thư viện guest
│     └─ debug/       trace, profiler, chế độ xác định
├─ vm-classlib/                  MÃ GUEST: java.lang/io/util, javax.microedition.*, com.nokia.*, JSR…
├─ host-android/                 native trên Android (chuyển backend JL-Mod sang đây)
├─ host-desktop/                 native headless cho CI (raster phần mềm, audio null, bàn phím kịch bản)
├─ tools/
│  ├─ env/        kiểm toolchain
│  ├─ api-sigs/   tạo/so sánh CSDL chữ ký từ stub jar
│  ├─ jarscan/    scanner tương thích game JAR theo profile
│  ├─ romgen/     đóng gói ROM
│  └─ difftest/   chạy chéo phoneME / KEmulator / JL-Mod-dex / JL-KVM
├─ profiles/*.json               profile thiết bị
└─ tests/
   ├─ vmspec/     class edge-case (sinh bằng Jasmin/ASM) + kết quả mong đợi
   ├─ midlets/    MIDlet test API
   ├─ golden/     hash framebuffer chuẩn
   └─ games/      (KHÔNG commit) JAR của người dùng + kịch bản input
```

### Kiến trúc chạy
```
 Android UI thread                vm-thread (1 thread host)                         Guest (bytecode)
 ─────────────────                ──────────────────────────                        ────────────────
 input/touch ──postEvent()──▶  hàng đợi sự kiện (thread-safe) ──native nextEvent()──▶ EventLoop (MIDP)
 SurfaceView/GLES ◀─frame swap── Bitmap offscreen ◀── native Graphics.* ◀────────── game.paint(g)
 AudioTrack/Sonivox ◀────────── host media ◀──────── native Player.* ◀──────────── game logic
                                 Scheduler: chạy thread guest theo timeslice,
                                 GC dừng-thế-giới tại điểm chuyển thread
```

### Interface giữa module (chữ ký cố định)
```java
// vm-core — host cài đặt interface này
public interface HostPlatform {
    long hostNanoTime();
    void log(int level, String tag, String msg);
    void registerNatives(NativeRegistry r);      // host-android / host-desktop đăng ký native
    ClassSource rom();                           // ROM thư viện guest
}
public interface ClassSource {                   // JAR game hoặc ROM
    byte[] readClass(String internalName);       // null nếu không có
    byte[] readResource(String absPath);         // null nếu không có
}
public interface ExecutionEngine {               // cho phép thay lõi C++ sau này (D2)
    int runSlice(int maxBytecodes);              // trả RUNNING / IDLE_UNTIL / EXITED
    long idleUntilNanos();
}
public final class Vm implements ExecutionEngine {
    public static Vm create(VmConfig cfg, HostPlatform host, ClassSource app, DeviceProfile p);
    public void postEvent(int type, int a, int b, int c);   // gọi từ thread bất kỳ
    public void requestExit();
}
public interface NativeMethod { void invoke(NativeContext c); }
public interface NativeContext {
    int argInt(int i); long argLong(int i); float argFloat(int i); double argDouble(int i);
    int argRef(int i);                           // tham chiếu guest (chỉ số arena)
    void retInt(int v); void retLong(long v); void retFloat(float v); void retDouble(double v);
    void retRef(int ref); void retVoid();
    void throwNew(String guestClass, String msg);
    void block(Blocker b);                       // treo thread guest hiện tại, KHÔNG treo vm-thread
    Heap heap(); Handles handles(); TempRoots roots();
}
```
Quy ước khoá native: `"javax/microedition/lcdui/Graphics.drawLine(IIII)V"`. Thiếu native → lỗi được log ngay khi nạp lớp (bản debug) và ném Error guest khi gọi.

---

## Ràng buộc và edge case (bắt buộc có test trong `tests/vmspec`)
- Số học: `idiv/ldiv` chia 0 → `ArithmeticException`; `Integer.MIN_VALUE / -1`; dấu của `irem/lrem`; shift che `&31/&63`; `i2b/i2s` mở rộng dấu, `i2c` mở rộng không dấu; `baload` mở rộng dấu, `caload` không dấu.
- Dấu phẩy động: `f2i/d2i/f2l/d2l` NaN→0 và bão hoà; `fcmpl/fcmpg` với NaN; profile CLDC 1.0 không có float/double (hành vi khi gặp opcode float phải xác minh).
- Mảng: kích thước âm → `NegativeArraySizeException`; `aastore` sai kiểu → `ArrayStoreException`; `multianewarray`; chỉ số ngoài biên.
- `checkcast/instanceof` với mảng và interface; `invokeinterface` trên null.
- **Class file cũ không có cờ `ACC_SUPER`** (version 45.x): ngữ nghĩa `invokespecial` khác — game rất cũ có thể dính.
- `<clinit>`: thứ tự, khởi tạo đệ quy, khởi tạo lỗi (CLDC không có `ExceptionInInitializerError` → ánh xạ phải xác minh); hằng `ConstantValue`.
- String literal phải intern (game so sánh bằng `==`).
- Monitor: `monitorenter/exit` lệch → `IllegalMonitorStateException`; `synchronized` method; `wait` có timeout; `interrupt` (CLDC 1.1).
- Tràn stack: kích thước stack mỗi thread từ profile; lớp lỗi ném ra phải xác minh (CLDC không có `StackOverflowError` trong danh sách tối thiểu).
- Ngoại lệ trong `paint()`/callback: hành vi phụ thuộc hiện thực → xác minh bằng oracle, ghi vào profile.
- Game vòng lặp bận không `sleep` (dựa vào preemption) → scheduler phải cắt theo số bytecode.
- Native dài (giải mã ảnh lớn, I/O) không được làm đứng scheduler.
- Ảnh lớn: bộ nhớ pixel có tính vào heap guest hay không là tham số profile (`imagesCountInHeap`).
- JAR: tên class khác hoa/thường, tên lạ do obfuscate, entry trùng, CRC hỏng, manifest có dòng nối/BOM; resource phân biệt hoa thường; `getResourceAsStream` tương đối theo package, tuyệt đối khi bắt đầu bằng `/`.
- Mã hoá mặc định (`microedition.encoding`) theo profile cho `new String(byte[])`, `getBytes()`, Reader/Writer — **không viết lại bytecode như `AndroidProducer`**.

---

## Danh mục API cần dựng (đích "đầy đủ Nokia")
Cột "JL-Mod" = đã có backend để chuyển sang host. "[XM]" = cần xác minh với stub SDK Nokia trước khi làm (tên lớp, phiên bản giới thiệu).

| Tầng | API | JL-Mod | Ghi chú |
|---|---|---|---|
| P0 | CLDC 1.1 (`java.lang`, `java.io`, `java.util`, `java.lang.ref`, `javax.microedition.io`) | Không (dùng libcore Android) | Viết mới trong guest |
| P0 | MIDP 2.0/2.1: midlet, lcdui, lcdui.game, rms, media (subset MIDP), io (GCF), pki | Có | Tách guest/host |
| P0 | `com.nokia.mid.ui`: FullCanvas, DirectGraphics, DirectUtils, DeviceControl | Có | Đủ pixel format (byte 1-bit gray… int 8888 ARGB), phép lật/xoay, đa giác/tam giác |
| P0 | `com.nokia.mid.sound`: Sound, SoundListener | Có | Định dạng tone + ringing tone (OTT) + WAV, `getConcurrentSoundCount` |
| P0 | JSR-135 MMAPI (MIDI, WAV, AMR, tone) | Có | Sonivox/TinySoundFont |
| P1 | JSR-184 M3G | Có (lõi C Nokia, EPL) | Viết lại glue JNI theo handle guest |
| P1 | JSR-75 FileConnection + PIM | Có | Ánh xạ thư mục ảo theo profile |
| P1 | `com.nokia.mid.ui`: SoftNotification, Clipboard, FreeSizeFontInvoker | Có | |
| P1 | `com.nokia.mid.ui.gestures` (GestureInteractiveZone, GestureRegistrationManager, GestureEvent, GestureListener) | Không | Có từ S40 6th Edition FP1 |
| P1 | `com.nokia.mid.ui`: TextEditor, TextEditorListener | Không | [XM] phiên bản |
| P1 | `com.nokia.mid.ui.frameanimator`, `.multipointtouch`, `.orientation` | Không | [XM] |
| P1 | `com.nokia.mid.ui`: CategoryBar, IconCommand, ElementListener, VirtualKeyboard, KeyboardVisibilityListener, LCDUIUtil | Không | [XM] |
| P1 | JSR-256 Sensor, JSR-234 AMMS, JSR-120/205 WMA, JSR-82 Bluetooth/OBEX | Có | WMA/BT ở chế độ offline/stub có kiểm soát |
| P2 | JSR-179 Location | Không | Android location |
| P2 | JSR-226 SVG Tiny | Không | Lớn — làm sau |
| P2 | JSR-172 Web Services/XML, JSR-177 SATSA, JSR-238, JSR-211 | Một phần (pki) | Chỉ khi profile cần |
| P2 | `com.nokia.mid.payment`, `com.nokia.mid.m3d`, `com.nokia.mid.s40.*` | Có/Không | [XM] |
| P3 | S60: TactileFeedback, S60TextEditor, CanvasItem, iapinfo, JSR-239 OpenGL ES | Không | Chỉ khi thêm profile S60 |
| P3 | Mascot Capsule v3 (không phải Nokia nhưng nhiều game) | Có | Đợt sau |

Thuộc tính JAD/manifest riêng Nokia cần hỗ trợ [XM giá trị và ngữ nghĩa]: `Nokia-MIDlet-Original-Display-Size`, `Nokia-MIDlet-Target-Display-Size`, `Nokia-MIDlet-On-Screen-Keypad`, `Nokia-MIDlet-App-Orientation`, `Nokia-UI-Enhancement`, `Nokia-MIDlet-Category`.

---

## Mốc
| Mốc | Nội dung | Điều kiện qua mốc |
|---|---|---|
| M0 | Nền móng, toolchain, CSDL chữ ký, scanner, oracle, corpus | T01–T06 xong |
| M1 | Classfile + verifier | Verifier khớp oracle trên corpus |
| M2 | Runtime VM (heap, interpreter, GC, thread, native) | `tests/vmspec` 100%; **benchmark sớm T13b đạt ngưỡng** |
| M3 | Thư viện CLDC 1.1 guest | Diff chữ ký CLDC 1.1 = 0 |
| M4 | MIDP 2.x + host desktop & Android | MIDlet test chạy trên cả hai host, hash pixel khớp nhau |
| M5 | Nokia API + JSR theo tầng P0→P2 | Diff chữ ký từng profile = 0 |
| M6 | Tích hợp Android không dex | Cài game không sinh DEX; chạy được corpus |
| M7 | Hiệu năng + mô phỏng tốc độ máy gốc | Đạt ngưỡng benchmark |
| M8 | Kiểm chứng chéo + phát hành | Difftest sạch hoặc mọi khác biệt có giải thích |

---

## Checklist
Phân công đợt 1: **A1 chạy T01–T20 và T13b** (M0–M3, 21 task). **A2 chỉ đọc trước T21–T27** (M4), chỉ bắt đầu làm khi quản đốc xác nhận A1 ≥ 90% (≥ 19/21 task đã tick). T28 trở đi: quản đốc giao ở đợt sau.

**Phân công đợt 2 (người dùng xác nhận 2026-10-30):** A2 implement M4 (T21–T27) theo thứ tự T21 → T22 → T23 → T24 → T25 → T26 → T27, mỗi lần quản đốc giao một cụm; A1 không còn task (T13b chờ Android). Chỉ một công nhân chạy thật tại một thời điểm.

**Luật thay công nhân (người dùng, 2026-10-29):** công nhân nào có ngữ cảnh vượt **400k token** thì quản đốc cho dừng và thay bằng công nhân mới (cùng vai A1/A2, cùng model Sonnet 5.5). Trước khi bàn giao, công nhân cũ phải đưa file về trạng thái nhất quán và ghi vào Nhật ký dòng `BÀN GIAO: đang ở Txx, đã xong …, bước tiếp theo …, lưu ý …`. Công nhân mới bắt đầu bằng việc đọc lại toàn bộ PLAN.md, không dựa vào lịch sử chat của người cũ. Quản đốc theo dõi mức ngữ cảnh khi nhận báo cáo; công nhân tự thấy mình gần 400k thì chủ động ghi BÀN GIAO rồi dừng.

### M0 — Nền móng
- [x] T01. Fork JL-Mod @ `f723a19`, dựng khung module
  - Agent: A1
  - Đầu vào/đầu ra: repo JL-Mod → repo `jl-kvm` có module rỗng `vm-core`, `vm-classlib`, `host-android`, `host-desktop`, `tools/*`
  - File: `settings.gradle.kts`, `*/build.gradle.kts`
  - Hoàn thành khi: `./gradlew assembleDebug` và `./gradlew :vm-core:test` chạy thành công; `vm-core` có kiểm tra build-time cấm `import android.` (task Gradle grep, fail nếu vi phạm)
- [x] T02. Toolchain J2ME + reverse engineering
  - Agent: A1
  - Đầu vào/đầu ra: script `tools/env/check_toolchain.sh` in phiên bản: JDK 8 (hoặc ECJ còn hỗ trợ 1.3), JDK hiện đại cho Gradle, preverify (WTK 2.5.2 / Java ME SDK), CFR, FernFlower, Procyon, JADX, javap, ASM (+ asm-util Textifier), Jasmin
  - File: `tools/env/*`
  - Hoàn thành khi: script exit 0; biên dịch MIDlet hello `-source 1.3 -target 1.3` + preverify ra class có thuộc tính `StackMap` (kiểm bằng `javap -v` hoặc dump ASM, lưu kết quả vào `tools/env/proof.txt`)
- [x] T03. CSDL chữ ký API từ stub jar
  - Agent: A1
  - Đầu vào/đầu ra: stub jar do người dùng cung cấp (CLDC 1.0/1.1, MIDP 2.0/2.1, Nokia UI API từ các SDK S40, từng JSR) → `tools/api-sigs/db/<api>-<version>.json` (lớp, super, interface, access, field: descriptor/access/hằng, method: descriptor/access/exceptions)
  - File: `tools/api-sigs/*`
  - Hoàn thành khi: tạo được DB cho mọi stub có; lệnh `api-sigs diff A.json B.json` liệt kê khác biệt; test: số lớp trong DB CLDC 1.1 = số lớp public trong stub jar
- [x] T04. `jarscan` — scanner tương thích v1
  - Agent: A1 (phụ thuộc T03)
  - Đầu vào/đầu ra: game JAR + profile → báo cáo JSON + Markdown: tham chiếu lớp/method/field không có trong profile, version class, có/thiếu StackMap, dùng float/double, `jsr/ret`, cấp phát mảng lớn hằng số, đệ quy (SCC trên call graph), dùng Thread/Timer, `System.gc`, ép kiểu thu hẹp đáng ngờ
  - File: `tools/jarscan/*`
  - Hoàn thành khi: JAR test cố ý gọi `String.isEmpty()` và `java.util.ArrayList` bị đánh dấu **NOT J2ME-SAFE** (mức lỗi nghiêm trọng → exit code ≠ 0); JAR sạch → exit 0
- [x] T05. Oracle rig cục bộ
  - Agent: A1
  - Đầu vào/đầu ra: script chạy MIDlet headless trên phoneME (build Linux cục bộ, **không commit binary/mã GPL**), MicroEmulator, KEmulator (nếu có), JL-Mod-dex; thu stdout/exception
  - File: `tools/difftest/oracles/*` (chỉ script + hướng dẫn)
  - Hoàn thành khi: MIDlet hello in cùng chuỗi trên ít nhất 2 oracle; oracle nào không dựng được thì ghi vào Vướng mắc kèm lý do
- [x] T06. Corpus test
  - Agent: A1
  - Đầu vào/đầu ra: `tests/vmspec/` (≥ 1 class cho mỗi mục trong "Ràng buộc và edge case", sinh bằng Jasmin/ASM, kèm `expected.txt`), `tests/midlets/` khung MIDlet test, quy ước `tests/games/` (không commit) + định dạng kịch bản input
  - File: `tests/**`
  - Hoàn thành khi: mỗi case vmspec có kết quả mong đợi lấy từ oracle (T05) hoặc trích dẫn đặc tả, ghi rõ nguồn trong file

### M1 — Classfile + verifier
- [x] T07. Parser class file
  - Agent: A1
  - Đầu vào/đầu ra: `byte[]` → `ClassFile` (CP đủ tag cho version ≤ 49; attribute Code, Exceptions, ConstantValue, StackMap, SourceFile, LineNumberTable, LocalVariableTable; attribute lạ bỏ qua)
  - File: `vm-core/.../classfile/*`
  - Hoàn thành khi: parse 100% class trong corpus + ROM; fuzz (cắt cụt/đảo byte 10k mẫu) không crash host, chỉ ném `ClassFormatError` guest
- [x] T08. Verifier CLDC dựa trên StackMap — **Quản đốc nghiệm thu**
  - Agent: A1 (phụ thuộc T07)
  - Đầu vào/đầu ra: method + StackMap → chấp nhận/`VerifyError` (kèm pc, lý do); chế độ strict/lenient (D6)
  - File: `vm-core/.../verify/*`
  - Hoàn thành khi: mọi class preverify trong corpus qua; mọi case âm trong vmspec bị từ chối; tập chấp nhận/từ chối khớp phoneME (difftest), khác biệt nào cũng được ghi vào báo cáo
- [x] T09. Xuất bản đồ kiểu cho GC
  - Agent: A1 (phụ thuộc T08)
  - Đầu vào/đầu ra: (method, pc) → bitmap tham chiếu cho locals + stack, có cache
  - File: `vm-core/.../verify/TypeMaps.java`
  - Hoàn thành khi: unit test trên 10 method mẫu khớp bản tính tay
- [x] T08a. Sửa nhỏ verifier (quản đốc ghi khi nghiệm thu T08)
  - Agent: A1
  - Lỗi: `MethodVerifier.classAssignable` cho mảng gán được vào `java/lang/Cloneable` và `java/io/Serializable` — CLDC 1.0/1.1 không có hai lớp này; với profile CLDC mảng chỉ gán được vào `java/lang/Object` (và mảng tương thích).
  - Hoàn thành khi: test ASM: class gán `int[]`/`Object[]` vào biến kiểu `java/lang/Cloneable` bị từ chối ở strict (lớp không tồn tại → VerifyError/NoClassDefFoundError theo cách hiện có), toàn bộ `:vm-core:test` + vmspec vẫn qua; ghi vào `docs/verifier-report.md`

### M2 — Runtime
- [x] T10. Heap arena + mô hình object
  - Agent: A1
  - Đầu vào/đầu ra: arena `int[]` kích thước từ profile; header (class id, word hash/monitor/cờ GC); mảng byte/char/short đóng gói; long/double 2 word; API cấp phát + temp roots
  - File: `vm-core/.../heap/*`
  - Hoàn thành khi: test đọc/ghi mọi kiểu; cấp phát vượt ngân sách đúng 1 byte thì đòi GC rồi OOM; `freeMemory()` khớp sổ sách
- [x] T11. Interpreter đủ opcode + frame + ngoại lệ — **Quản đốc nghiệm thu**
  - Agent: A1 (phụ thuộc T09, T10)
  - Đầu vào/đầu ra: thực thi mọi opcode (kể cả `jsr/ret`, `wide`, `tableswitch/lookupswitch`); bảng exception; stack trace guest
  - File: `vm-core/.../interp/*`
  - Hoàn thành khi: `tests/vmspec` 100% khớp `expected.txt`; không có nhánh "TODO opcode"
- [x] T12. Liên kết, phân giải, khởi tạo lớp
  - Agent: A1 (phụ thuộc T11)
  - Đầu vào/đầu ra: phân giải lười, kiểm quyền truy cập, `<clinit>` đúng JVM spec, xử lý class thiếu `ACC_SUPER`, ánh xạ lỗi sang lớp có trong CLDC
  - File: `vm-core/.../link/*`
  - Hoàn thành khi: test thời điểm ném `NoClassDefFoundError`, `<clinit>` đệ quy, `<clinit>` lỗi cho kết quả khớp oracle; bảng ánh xạ lỗi ghi trong `docs/error-mapping.md` kèm nguồn
- [x] T13. GC chính xác + nén + WeakReference + dọn peer — **Quản đốc nghiệm thu**
  - Agent: A1 (phụ thuộc T09, T10)
  - Đầu vào/đầu ra: root = stack (qua TypeMaps), static, chuỗi intern, temp roots, bảng handle, monitor; dọn peer host khi object guest chết
  - File: `vm-core/.../gc/*`
  - Hoàn thành khi: stress 1 triệu cấp phát đồ thị ngẫu nhiên có checksum không sai; hash định danh giữ nguyên sau nén; bảng handle host không rò (đếm về 0 sau khi thả hết)
- [ ] T13b. Benchmark sớm (cổng quyết định D2)
  - Agent: A1 (phụ thuộc T11)
  - Đầu vào/đầu ra: vòng lặp số học, gọi method ảo, truy cập mảng, cấp phát → triệu bytecode/giây trên desktop và 1 máy Android
  - File: `tests/bench/*`, `docs/bench.md`
  - Hoàn thành khi: có số đo; quản đốc so với nhu cầu game (ngưỡng quản đốc chốt sau khi có số đầu tiên) và ghi quyết định giữ lõi Java hay mở nhánh lõi C++
- [x] T11a. Tách vòng lặp interpreter cho JIT (quản đốc ghi sau T13b, V7)
  - Agent: A1
  - Lỗi: `Interpreter.run()` dài ~9744 byte bytecode > ngưỡng 8000 (`HugeMethodLimit`) → HotSpot không JIT với cờ mặc định (12–23 Mbc/s; với `-XX:-DontCompileHugeMethods` 78–149 Mbc/s). ART cũng có giới hạn tương tự → rủi ro R1.
  - Đầu vào/đầu ra: tách opcode hiếm (long/float/double ít dùng, multianewarray, switch, wide, jsr/ret, monitor, đường chậm/ngoại lệ) ra method phụ; vòng lặp nóng < 8000 byte; KHÔNG đổi ngữ nghĩa, không đổi D1–D18.
  - Hoàn thành khi: `javap -c` cho thấy `run()` (và mọi method trong đường nóng) < 8000 byte; `tests/bench/run_bench.sh` với cờ JVM MẶC ĐỊNH đạt ≥ 70% số đo khi bật `-XX:-DontCompileHugeMethods`; vmspec (thường + `--gc-stress 1 --gc-verify`) 56/56; `:vm-core:test` qua; cập nhật `docs/bench.md`
- [x] T14. Scheduler green thread + monitor
  - Agent: A1 (phụ thuộc T11)
  - Đầu vào/đầu ra: timeslice theo bytecode (từ profile), ưu tiên, `sleep/wait/notify/notifyAll/join/interrupt/yield`, `synchronized`; phát hiện deadlock (debug)
  - File: `vm-core/.../thread/*`
  - Hoàn thành khi: test producer–consumer, deadlock cố ý (debug báo); cùng seed chạy 10 lần ra output y hệt
- [x] T15. Native registry + NativeContext + blocker + bảng handle
  - Agent: A1 (phụ thuộc T11)
  - Đầu vào/đầu ra: theo "Interface giữa module"
  - File: `vm-core/.../natives/*`
  - Hoàn thành khi: native mẫu trên host-desktop chạy; native chặn treo đúng 1 thread guest, các thread khác vẫn chạy; native thiếu bị báo khi nạp lớp
- [x] T16. Debug & trace
  - Agent: A1
  - Đầu vào/đầu ra: log nạp lớp, ngoại lệ (stack guest), profiler vị trí cấp phát (theo pc), log GC, đếm bytecode theo method, ring buffer trace, chế độ xác định + ghi/phát lại input (D14)
  - File: `vm-core/.../debug/*`
  - Hoàn thành khi: bản debug có đủ log; bản release đo benchmark chênh < 2% so với khi gỡ hẳn hook

### M3 — Thư viện CLDC 1.1 (guest)
- [x] T17. Build pipeline thư viện guest + `romgen`
  - Agent: A1 (phụ thuộc T02, T08)
  - Đầu vào/đầu ra: `vm-classlib/src` → biên dịch theo D11 → preverify → verifier của dự án → `rom.jar` (đóng vào asset)
  - File: `vm-classlib/build.gradle.kts`, `tools/romgen/*`
  - Hoàn thành khi: build fail nếu mã guest tham chiếu lớp ngoài chính nó; ROM qua verifier strict
- [x] T18. `java.lang` / `java.util` / `java.io` / `java.lang.ref` đúng bề mặt CLDC 1.1
  - Agent: A1 (phụ thuộc T17, T03)
  - Đầu vào/đầu ra: lớp guest theo D12 (nguồn mã ghi ở header từng file)
  - File: `vm-classlib/src/java/**`
  - Hoàn thành khi: `api-sigs diff` (ROM vs CLDC 1.1) = 0 khác biệt; test classlib qua
- [x] T19. String, encoding, chuyển số
  - Agent: A1 (phụ thuộc T18)
  - Đầu vào/đầu ra: `String/StringBuffer`, intern, ISO-8859-1/UTF-8, encoding mặc định theo profile, `Integer/Long` parse/toString, `Float/Double.toString`
  - File: `vm-classlib/src/java/lang/*`
  - Hoàn thành khi: bộ test chuỗi khớp output phoneME; khác biệt `Double.toString` (nếu có) được ghi rõ là theo oracle nào
- [x] T20. System/Runtime + thuộc tính theo profile + GCF cơ sở
  - Agent: A1 (phụ thuộc T18)
  - Đầu vào/đầu ra: `System.getProperty` từ profile (sửa `CLDC-1.1`), `Runtime.gc/freeMemory/totalMemory` từ arena, `currentTimeMillis` (đồng hồ ảo ở chế độ xác định); `Connector` + chính sách mạng theo profile
  - File: `vm-classlib/src/java/lang/System.java`, `.../Runtime.java`, `vm-classlib/src/javax/microedition/io/*`
  - Hoàn thành khi: test in thuộc tính khớp profile JSON, khoá lạ → `null`; `Connector.open("http://…")` cho đúng hành vi (cho phép/từ chối/offline)

### M4 — MIDP 2.x + host (A2 đọc trước, chờ A1 ≥ 90%)
- [x] T21. Vòng đời MIDlet + JAD/manifest + AMS
  - Agent: A2
  - Đầu vào/đầu ra: tái dùng `ru.woesss.j2me.jar.Descriptor`; `startApp/pauseApp/destroyApp`, `notifyDestroyed`, `getAppProperty`, `platformRequest`
  - File: `vm-classlib/src/javax/microedition/midlet/*`, `host-*/.../ams/*`
  - Hoàn thành khi: MIDlet test ghi đúng thứ tự vòng đời, kể cả pause/resume theo `onPause/onResume` của Android
- [x] T22. EventLoop + Display + mã phím theo profile — **Quản đốc nghiệm thu**
  - Agent: A2 (phụ thuộc T21)
  - Đầu vào/đầu ra: D13; gộp repaint, `serviceRepaints`, `callSerially`, `setCurrent`, `showNotify/hideNotify`, `sizeChanged`; mã phím Nokia (soft −6/−7, chọn −5, mũi tên −1…−4…) lấy từ profile
  - File: `vm-classlib/src/javax/microedition/lcdui/{Display,Displayable,Canvas}.java`, `vm-classlib/src/jlkvm/internal/EventLoop.java`
  - Hoàn thành khi: test thứ tự sự kiện khớp oracle; không có lời gọi guest nào từ thread Android ngoài vm-thread (assert trong debug)
- [ ] T23. Graphics / Image / Font
  - Agent: A2 (phụ thuộc T22)
  - Đầu vào/đầu ra: guest giữ trạng thái rẻ (màu, translate, clip), native vẽ; host-android chuyển backend JL-Mod; host-desktop raster phần mềm
  - File: `vm-classlib/src/javax/microedition/lcdui/{Graphics,Image,Font}.java`, `host-android/.../lcdui/*`, `host-desktop/.../lcdui/*`
  - Hoàn thành khi: MIDlet kiểm đồ hoạ (clip, anchor, `drawRegion` 8 phép biến đổi, `drawRGB` alpha, `getRGB`, ảnh mutable/immutable) cho hash pixel giống nhau giữa desktop và Android; khác biệt với KEmulator được liệt kê
- [x] T22a. `java.util.Timer` / `TimerTask` (MIDP 2.0) — quản đốc thêm từ V11
  - Agent: A2
  - Đầu vào/đầu ra: Timer chạy trên thread guest riêng (green thread), `schedule`/`scheduleAtFixedRate` (delay, period, Date), `cancel`, `purge` không có trong MIDP (D9), TimerTask `run/cancel/scheduledExecutionTime`; ngoại lệ trong task kết thúc Timer như J2SE/MIDP javadoc; thời gian theo đồng hồ VM (đồng hồ ảo ở chế độ xác định)
  - Hoàn thành khi: `api-sigs diff` phần Timer/TimerTask vs midp-2.0 = 0 (bỏ khỏi `pending`); test fixed-delay vs fixed-rate, cancel trong run, task ném ngoại lệ; chế độ xác định chạy 10 lần ra y hệt
- [x] T24. lcdui cấp cao (đạt phần desktop; host-android để M6) (Form, List, TextBox, Alert, Ticker, Command, Item)
  - Agent: A2 (phụ thuộc T22)
  - File: `vm-classlib/src/javax/microedition/lcdui/*`, `host-android/.../lcdui/ui/*`
  - Hoàn thành khi: MIDlet mẫu thao tác được; `commandAction` luôn đến qua EventLoop
- [x] T25. Game API (GameCanvas, Sprite, TiledLayer, LayerManager)
  - Agent: A2 (phụ thuộc T23)
  - Đầu vào/đầu ra: phần lớn là Java guest thuần trên Graphics; va chạm pixel
  - Hoàn thành khi: test `getKeyStates`, `flushGraphics`, va chạm pixel, animated tile qua
- [x] T26. RMS
  - Agent: A2 (phụ thuộc T21)
  - Đầu vào/đầu ra: giới hạn dung lượng theo profile, thứ tự enumeration, ngoại lệ; tuỳ chọn nhập save từ JL-Mod
  - Hoàn thành khi: test RMS (đầy bộ nhớ, `getSizeAvailable`, xoá/thêm) khớp profile
- [x] T26b. GCF phần MIDP 2.0 + `javax.microedition.pki` — quản đốc thêm từ V11
  - Agent: A2 (phụ thuộc T20)
  - Đầu vào/đầu ra: interface `HttpConnection`, `HttpsConnection`, `SocketConnection`, `ServerSocketConnection`, `UDPDatagramConnection`, `CommConnection`, `SecureConnection`, `SecurityInfo`, `ContentConnection`/`StreamConnection` đủ theo DB; `PushRegistry`; `pki.Certificate`, `CertificateException`. Hành vi theo chính sách mạng của profile (T20: cho phép/từ chối/offline — mặc định offline); handler http thật ở host chỉ khi chính sách cho phép
  - Hoàn thành khi: `api-sigs diff` ROM vs midp-2.0 không còn mục `javax/microedition/io/`, `javax/microedition/pki/` trong `pending`; test: offline → IOException, deny → SecurityException, allow → GET tới server cục bộ trong test trả đúng mã/trường header
- [x] T27. MMAPI + tone
  - Agent: A2 (phụ thuộc T22)
  - Đầu vào/đầu ra: `Manager.createPlayer` theo định dạng profile hỗ trợ, trạng thái, listener, Volume/ToneControl, `playTone`; host dùng Sonivox/TSF của JL-Mod
  - Hoàn thành khi: log chuyển trạng thái đúng đặc tả; listener chỉ chạy trên thread guest; nhạc chuông dạng văn bản RTTTL/RTX (`.rtx`, MIME `audio/x-rtttl`/`text/x-rtttl` — [XM] danh sách MIME theo profile) được phân tích trong guest/host và phát qua bộ tạo tone, **không** đưa qua ffmpeg (log JL-Mod thật của người dùng: `mobile-ffmpeg: ….rtx: Invalid data found` → game mất tiếng); có test parser RTTTL

### M5 — Nokia API + JSR (giao đợt sau)
- [ ] T28. Nokia UI API lõi: FullCanvas, DirectGraphics, DirectUtils, DeviceControl, Sound/SoundListener — **Quản đốc nghiệm thu**
  - Hoàn thành khi: diff chữ ký với DB Nokia UI API của profile = 0; test pixel từng định dạng `drawPixels/getPixels` + phép biến đổi khớp KEmulator hoặc ảnh chụp máy thật; parser ringing tone có test (Smart Messaging OTT + RTTTL/RTX dùng chung bộ phân tích của T27)
- [ ] T29. API S40 6th/Developer Platform: gestures, TextEditor, frameanimator, multipointtouch, orientation, CategoryBar/IconCommand, VirtualKeyboard, LCDUIUtil, SoftNotification, Clipboard, FreeSizeFont
  - Hoàn thành khi: mọi lớp [XM] đã đối chiếu stub SDK; diff chữ ký profile S40 tương ứng = 0; ví dụ trong SDK Nokia chạy được
- [ ] T30. Thuộc tính hệ thống Nokia + thuộc tính JAD Nokia
  - Hoàn thành khi: mỗi khoá có nguồn (tài liệu SDK/đo máy thật); test hành vi scale/màn hình/bàn phím ảo theo thuộc tính
- [ ] T31. JSR-184 M3G: nối lại lõi C Nokia (EPL) với handle guest — **Quản đốc nghiệm thu**
  - Hoàn thành khi: toàn bộ native M3G cũ được thay bằng glue mới; demo M3G + game M3G trong corpus hiển thị đúng
- [ ] T32. JSR-75, 82, 120/205, 234, 256 (tái dùng), 179 (mới); P2: 226, 172
  - Hoàn thành khi: mỗi JSR có diff chữ ký = 0 trên profile khai báo nó; demo chạy
- [ ] T33. Profile thiết bị Nokia
  - Đầu vào/đầu ra: `profiles/*.json` (S40 2nd/3rd/5th/6th/Developer Platform; S60 tuỳ chọn): màn hình, mã phím, font, heap, giới hạn JAR, version class tối đa, CLDC/MIDP, danh sách API, thuộc tính, timeslice, tốc độ CPU giả lập
  - Hoàn thành khi: validator schema qua; **mọi số liệu có thẻ nguồn** (`datasheet` / `sdk` / `measured`) hoặc `UNVERIFIED`

### M6 — Tích hợp Android không dex
- [ ] T34. Installer không dex
  - Hoàn thành khi: cài game không tạo `converted.zip`/DEX nào (kiểm danh sách file thư mục app); báo cáo `jarscan` hiển thị sau khi cài
- [ ] T35. Runner + chọn engine + A/B
  - Đầu vào/đầu ra: Activity chạy `Vm` trên `vm-thread`; tái dùng surface/GLES/shader/skin/bàn phím ảo/âm thanh của JL-Mod; engine dex cũ chỉ có ở debug (D17)
  - Hoàn thành khi: corpus chạy; xoay màn hình, pause/resume ổn; debug chạy được cùng game trên 2 engine để so
- [ ] T35b. Log lỗi game gọn (người dùng yêu cầu 2026-10-29)
  - Bối cảnh: JL-Mod lưu log bằng `logcat -t 500 -f` không lọc (`ru/playsoftware/j2meloader/util/LogUtils.java`); log mẫu của người dùng 1031 dòng chủ yếu là `MIUIInput`/`Insets*`/`VRI`/`Audio*` của hệ thống (mỗi lần chạm phím ảo) và 157 dòng `AppClassLoader: CUSTOM GET RES`, không có lỗi game.
  - Đầu vào/đầu ra: VM ghi file `game-errors.log` riêng mỗi lần chạy: thông tin profile; ngoại lệ không bắt (stack trace guest: lớp, method, pc, dòng nếu có); lỗi liên kết (`NoClassDefFoundError`/`NoSuchMethodError`/`NoSuchFieldError`/`VerifyError`); `OutOfMemoryError` kèm số liệu heap; resource không tìm thấy sau khi thử hết đường dẫn hợp lệ (1 dòng/tài nguyên, không lặp); media không giải mã được (tên, định dạng, MIME). KHÔNG ghi sự kiện phím/chạm/khung hình (input chỉ ghi khi bật chế độ ghi/phát lại D14, ra file khác). Nút gửi/lưu log chỉ lấy file này + logcat lọc theo PID và tag của app. Bỏ log debug `CUSTOM GET RES` ở release.
  - Hoàn thành khi: chạy lại kịch bản tương đương log mẫu (chạm phím ảo liên tục + phát âm thanh) → file log không có dòng nào của `MIUIInput`/`Insets*`/`VRI`/`Audio*`/sự kiện chạm; ngoại lệ cố ý trong MIDlet test xuất hiện đúng 1 lần kèm stack trace guest; release không còn log debug nặng (D15)

### M7 — Hiệu năng & độ trung thực
- [ ] T36. Benchmark đầy đủ (throughput, tốc độ cấp phát, thời gian dừng GC, thời gian paint) trên ≥ 2 máy Android
- [ ] T37. Tối ưu: quickening, inline cache, siêu lệnh, native nhanh cho String/arraycopy — mỗi thay đổi phải qua lại toàn bộ vmspec + difftest, không đổi ngữ nghĩa
- [ ] T38. Bộ điều tốc giả lập tốc độ CPU máy gốc + nhịp khung hình

### M8 — Kiểm chứng & phát hành
- [ ] T39. Difftest tự động trên corpus (stdout, ngoại lệ, hash framebuffer) với phoneME / KEmulator / JL-Mod-dex — **Quản đốc nghiệm thu**
- [ ] T40. Hồi quy game (kịch bản input + golden hash)
- [ ] T41. Kiểm toán license: Apache-2.0 (NOTICE), EPL (M3G), LGPL v3 (MobileFFmpeg), MPL 2.0 (FilePicker); không có mã GPL trong APK; rà nguồn gốc mã guest `vm-classlib` bằng công cụ so khớp với OpenJDK/phoneME (V8) — đoạn nào giống phải viết lại hoặc thay bằng nguồn Apache có ghi rõ
- [ ] T42. Bản release: gỡ hook debug, xoá `dexlib`, R8 chỉ áp cho mã host (ROM guest không bị đụng), tài liệu

---

## Quy trình bắt buộc sau mỗi thay đổi (Definition of Done)
1. Biên dịch (host + guest).
2. Unit test `vm-core`.
3. `tests/vmspec` 100%.
4. Verifier strict trên ROM.
5. `api-sigs diff` ROM vs profile = 0.
6. `jarscan` trên corpus không có cảnh báo mới.
7. Đo cấp phát trong vòng lặp paint/update của MIDlet test (debug profiler).
8. Difftest với oracle.
9. Chạy trên host-desktop → host-android → máy thật (nếu có).
Task chỉ được tick khi qua mọi bước áp dụng được. "Game vẫn chạy" không phải bằng chứng.

## Rủi ro
- **R1 Hiệu năng interpreter Java trên ART.** Đo ở T13b (rất sớm). Dự phòng: lõi C++ cùng interface `ExecutionEngine`.
- **R2 Thiếu chân lý về VM Nokia.** phoneME là RI của Sun, không phải VM Nokia. Cần máy thật hoặc dữ liệu đo; mọi điểm chưa chắc đánh `UNVERIFIED`.
- **R3 License.** Không trộn mã GPLv2; stub jar SDK chỉ dùng cục bộ để tạo DB chữ ký.
- **R4 M3G** (khoảng 323 native) tốn công nối lại.
- **R5 Phình phạm vi** (SVG, Web Services, S60). Giữ đúng tầng P0→P3.
- **R6 Hồi quy so với JL-Mod** (JL-Mod có nhiều xử lý riêng cho game). A/B ở T35 để phát hiện.
- **R7 API chỉ chạy trên UI thread Android** (TextBox, dialog) — native phải post sang UI thread và chỉ treo thread guest.

## Vướng mắc (công nhân ghi vào đây, chờ quản đốc/người dùng quyết)
- V1. ĐÃ QUYẾT (người dùng, 2026-10-29): dùng nguồn mở thay thế. Stub CLDC 1.0/1.1 + MIDP 2.0 lấy từ MicroEmulator trên Maven Central (`org.microemu:cldcapi10`, `cldcapi11`, `midpapi20`; dùng làm DB chữ ký, ghi rõ nguồn trong JSON). Preverify: ProGuard `-microedition` thay WTK `preverify` (T02 chấp nhận). Oracle T05: MicroEmulator + FreeJ2ME (`../freej2me/freej2me_plus.jar` trong repo M-I) + phoneME build cục bộ nếu dựng được (không commit). DB Nokia UI API để trống cho tới khi người dùng cung cấp SDK S40 — ghi `UNVERIFIED`.
- V2. Có máy Nokia thật nào để đo không (model)? Quyết định profile ưu tiên và nguồn `measured`.
- V3. Mặc định đã chọn, người dùng có thể đổi: lõi Java (D2), ưu tiên S40 trước S60, giữ engine dex ở debug để A/B (D17), dự án dùng riêng hay phát hành công khai (ảnh hưởng T41).
- V4. (A1) Oracle T05 chưa dựng được: phoneME (nguồn không tải được: github.com/nikita36078/phoneME-feature trả 403 qua proxy, svn.java.net không phân giải; cần người dùng cung cấp nguồn/bản build cục bộ rồi đặt `tools/difftest/oracles/run_phoneme.sh`), KEmulator (app GUI Windows, không có bản headless), JL-Mod-dex (cần runtime Android; dành cho T35/T39). T05 đạt với 2 oracle: MicroEmulator + FreeJ2ME.
- V5. ĐÃ GIẢI QUYẾT 2026-10-30 (xem Nhật ký quản đốc). Nguyên văn (A1): Văn bản đặc tả CLDC 1.1 (chương Class File Verification) không tải được: jcp.org dẫn tới trang chấp nhận giấy phép của Oracle, Javadoc không có phần verifier. Quy tắc đặc thù CLDC (jsr/ret, thiếu StackMap, fall-through vào frame, uninit nhảy lùi, lớp lỗi) ghi UNVERIFIED trong `docs/verifier-report.md`. Cần người dùng cung cấp PDF đặc tả hoặc phoneME/thiết bị để đối chiếu.
- V6. (A1) T13b: phần Android CHƯA ĐO (container không có máy/emulator Android) -> T13b không tick. Cần: chạy `tests/bench/run_bench.sh` tương đương trên ≥ 1 máy Android (đưa `vm-core` + ROM stub + `tests/bench/build/bench.jar` vào một app thử hoặc adb shell + `dalvikvm`) và chốt ngưỡng Mbc/s.
- V7. (A1) Rủi ro hiệu năng D2/R1 phát hiện ở T13b: `Interpreter.run()` dài 9744 byte bytecode > 8000 byte nên HotSpot mặc định KHÔNG JIT (12–23 Mbc/s; bật `-XX:-DontCompileHugeMethods` lên 78–149 Mbc/s). ART có ngưỡng "huge method" tương tự (chưa kiểm chứng trên máy). Đề xuất quản đốc: giao tách `run()` (đẩy opcode hiếm/invoke/return/long-double-float ra hàm phụ để vòng nóng < 8000 byte, giữ ngữ nghĩa, chạy lại vmspec + stress GC) trước khi quyết giữ lõi Java hay mở nhánh C++; chưa làm vì ngoài phạm vi T13b ("KHÔNG đổi thiết kế").- V8. (A1) Nguồn gốc mã vm-classlib: header ghi "original code written for jl-kvm" (D12); một số thuật toán (Integer.parseInt dạng multmin/limit, Hashtable, Vector...) có dạng quen thuộc của JDK/Harmony — người trước viết từ trí nhớ, không có bằng chứng chép OpenJDK. Đề nghị T41 (kiểm toán license) rà lại bằng công cụ so khớp; nếu nghi ngờ thì viết lại phần đó.
- V9. ĐÃ QUYẾT (quản đốc 2026-10-30): chấp nhận module `host-common` và ROM MIDP ở `vm-classlib/midp/src` (không trái D1–D18). Nguyên văn (A2, đợt 2) Cấu trúc thêm so với cây thư mục PLAN, cần quản đốc xác nhận: (a) module mới `host-common` (Java thuần, không `android.*`, có task kiểm) chứa phần host dùng chung desktop + Android (Descriptor/MidletSuite/AmsHost, `MidpNatives`, `Surface`/`SoftRaster`, PNG, font bitmap) — không đặt trong vm-core (D2) và không nhân đôi; (b) tầng ROM MIDP ở `vm-classlib/midp/src` (không phải `vm-classlib/src/javax/...` như ghi trong file của T21-T23) vì D10 đòi lớp MIDP chỉ có khi profile khai báo `apis` và build CLDC phải giữ diff = 0; ROM = `rom.jar` (CLDC) + `rom-midp.jar` (chỉ lớp MIDP). Không đổi quyết định D1–D18.
- V10. CHẤP NHẬN tạm (quản đốc): giữ lựa chọn hiện tại, UNVERIFIED tới khi có máy thật (V2). Nguyên văn (A2) Hành vi MIDP mà oracle (MicroEmulator, FreeJ2ME; không phải máy thật, V4) khác nhau hoặc khác jl-kvm và spec để ngỏ — đã chọn và ghi trong `docs/midp.md` §3-§4, UNVERIFIED trên máy Nokia: thứ tự hideNotify/showNotify khi đổi Canvas (jl-kvm: hide trước show; cả hai oracle: show trước); `getCurrent()` đổi ngay khi `setCurrent` (oracle: trễ); Canvas tự paint sau showNotify; không gọi hideNotify khi MIDlet kết thúc; repaint hợp nhất theo hình chữ nhật hợp (FreeJ2ME chỉ giữ hình cuối); `isShown()` false trong showNotify; softkey: trái = lệnh quan trọng nhất, phải = BACK/CANCEL/EXIT/STOP hoặc lệnh thứ hai (menu >2 lệnh để T24); font = DejaVu 10/13/17 px nướng sẵn (không phải font Nokia). Cần số đo máy thật (V2) để chốt.
- V11. ĐÃ QUYẾT (quản đốc 2026-10-30): thêm T22a (Timer/TimerTask) và T26b (GCF MIDP + pki). Nguyên văn (A2) Các lớp MIDP 2.0 trong DB chữ ký chưa thuộc task nào của T21–T27: `javax.microedition.io` phần MIDP (HttpConnection, HttpsConnection, SocketConnection, ... T20 ghi "HttpConnection thuộc MIDP (M4)"), `javax.microedition.pki`, `java.util.Timer/TimerTask` — đang nằm trong danh sách `pending` của `vm-classlib/build.gradle.kts` (bỏ qua ở diff, chưa có trong ROM). Đề nghị giao vào T24/T26 hoặc thêm task. Kiểm tra D9 của tầng MIDP chỉ đạt trọn khi danh sách `pending` rỗng.
- V13. (A2) T24/T25 cần Android để nghiệm thu đủ: host-android phải hiện thực `ScreenHost` bằng view Android thật (Form/List/TextBox/Alert, menu command, ticker; xem `docs/midp.md` §6), layout thật quyết định kích thước Item (guest chỉ ước lượng); hành vi MIDP chưa kiểm trên máy thật (UNVERIFIED, V10): thứ tự DISMISS_COMMAND→chuyển màn, ngưỡng opaque alpha=255 của va chạm pixel, GameCanvas/Sprite/TiledLayer không sao chép ảnh mutable, buffer GameCanvas mới khi đổi cỡ canvas, `maxSize` tối đa 5000.
- V12. (A2) T21/T23 cần Android để nghiệm thu đủ: (1) nối `AmsHost.pause()/resume()` vào `Activity.onPause/onResume`; (2) host-android cài `PlatformServices` + `SurfaceFactory` (dùng chung `SoftRaster` thì cùng pixel với desktop theo cấu tạo; backend Bitmap/Canvas của JL-Mod không thể giống từng bit với raster phần mềm — cần so ở M6); (3) hash pixel "giống nhau giữa desktop và Android" chưa kiểm được nên T23 KHÔNG tick.
- V14. ĐÃ QUYẾT (quản đốc 2026-10-30, mở đợt 3): DB chữ ký Nokia UI API (V1 để trống) lấy tạm từ stub/impl mở: `org.microemu:microemu-nokiaui:2.0.4` (Maven) qua `api-sigs extract`, đối chiếu thêm các lớp `com.nokia.mid.{ui,sound}` của JL-Mod (Apache-2.0, `app/src/main/java/com/nokia/...`); chỉ lấy chữ ký (sự kiện API), không chép mã; phần chỉ có ở một nguồn ghi UNVERIFIED. KEmulator/máy thật không có (V2/V4) → test pixel DirectGraphics lấy kỳ vọng từ javadoc Nokia UI API 1.1 (định dạng TYPE_*, phép biến đổi FLIP/ROTATE) + so FreeJ2ME/MicroEmulator làm oracle phụ; điểm khác nhau ghi `known-diverge` + UNVERIFIED; khi người dùng có SDK S40 thì thay DB và chạy lại diff.
- V14b. CẬP NHẬT (người dùng cung cấp SDK, 2026-10-30): Nokia Series 40 Developer Platform 2.0 SDK 1.1 (2004-12-20). Bộ cài InstallAnywhere bóc trong `tools/env/cache/nokia-sdk/` (gitignored, KHÔNG commit SDK/javadoc — EULA Nokia). Chỉ commit DB chữ ký trích từ `classes.zip` của máy giả lập: `tools/api-sigs/db/s40dp2-{nokia-ui,jsr135,wma-1.1,jsr82,cldc-midp}.json`. **DB Nokia UI chuẩn = `s40dp2-nokia-ui.json`** (thay nguồn MicroEmulator của V14): khác MicroEmulator 4 điểm — `DeviceControl`/`DirectUtils` KHÔNG có constructor public, `FullCanvas` không khai báo lại `getWidth/getHeight`. `jsr-135-extra.json` (T27) khớp SDK ở MIDIControl/TempoControl/RateControl. Bề mặt CLDC+MIDP của S40 DP2.0 so DB midp-2.0 (MicroEmulator): không thiếu lớp/thành viên nào, chỉ khác lớp khai báo override (Alert/List/TextBox/Screen/Canvas/StringItem/ChoiceGroup/TextField/Sprite) — tương thích nhị phân. Javadoc Nokia UI 1.1, MMAPI 1.1, WMA 1.1, JSR-82 nằm ở `tools/env/cache/nokia-sdk/sdk/api/` làm nguồn kỳ vọng cho test.

## Nhật ký
- 2026-10-29 [quản đốc] Repo đặt tại `M-I/jl-kvm/` (snapshot JL-Mod `dev` @ f723a19, không kèm lịch sử git) theo quyết định người dùng. Android SDK cài ngoài repo tại `/opt/android-sdk` (platform 34, build-tools 34.0.0, NDK 22.1.7171670) — đặt `local.properties` (không commit). Đợt 1: A1 làm T01–T20 + T13b.
- 2026-10-29 [A1] T02 đạt: `tools/env/{lib.sh,versions.sh,setup_toolchain.sh,check_toolchain.sh,j2mec.sh,DumpAttrs.java,proof.txt}`. Chọn JDK 8 Temurin tại `/opt/jdk8` (javac 1.8 chấp nhận `-source 1.3 -target 1.3`, class v47); ECJ 3.12.3 làm dự phòng; preverify = ProGuard 7.5.0 `-microedition` (Maven Central); stub = MicroEmulator cldcapi10/11, midpapi20 2.0.4; CFR/FernFlower/Procyon/JADX/ASM/Jasmin tải vào `tools/env/cache` (gitignore). `check_toolchain.sh` exit 0, proof.txt có `StackMap`.
- 2026-10-29 [A1] T01 DANG DO (chưa tick): đã tạo `settings.gradle.kts` (include vm-core, vm-classlib, host-android, host-desktop, tools:*), `*/build.gradle.kts`, `vm-core` có task `checkNoAndroidImports` + test mẫu `VmInfoTest`, `local.properties`, `chmod +x gradlew`. Chưa xác nhận `./gradlew assembleDebug :vm-core:test` (lần chạy đầu đang tải phụ thuộc).
- 2026-10-29 [A1] T03 DANG DO (chưa tick, chưa build/test): code `tools/api-sigs` (ApiDb, Extractor, Differ, Main, test, launcher `api-sigs`); chưa sinh `db/*.json`. T04 DANG DO (chưa build/test): code `tools/jarscan` (Scanner, Main, profiles); thiếu JAR test bẩn/sạch + selftest. T05, T06 chưa bắt đầu.
- 2026-10-29 [A1] TẠM DỪNG theo quản đốc: đang ở T01 (chờ kết quả gradle) / T03; bước tiếp theo là chạy `./gradlew assembleDebug :vm-core:test :tools:api-sigs:test`, sửa lỗi biên dịch nếu có, sinh db/cldc-1.0, cldc-1.1, midp-2.0 từ stub MicroEmulator, rồi T04 (selftest bad/clean jar), T05, T06.
- 2026-10-29 [A1 mới] T01 đạt: `./gradlew assembleDebug :vm-core:test` BUILD SUCCESSFUL (cần mirror Maven của quản đốc; submodule TinySoundFont rỗng nên đã clone MIT source vào `app/.../mmapi_tsf/TinySoundFont`, bỏ .git, untracked); `checkNoAndroidImports` fail khi chèn `import android.` (đã thử), pass khi sạch. Kiểm kê: 3b8e3be có code T03–T06 chưa nghiệm thu, đang kiểm lại từng task.
- 2026-10-29 [A1 mới] T03 đạt: `tools/api-sigs/make_db.sh` sinh cldc-1.0/1.1/midp-2.0 (76/81/66 lớp = số lớp trong stub, đếm bằng javap), `api-sigs diff` chạy (0 diff khi tự so; cldc1.0→1.1 liệt kê float/double), test ApiSigsTest 2/2 không skip. nokia-ui-api.json rỗng UNVERIFIED (V1). T04 đạt: `tools/jarscan/selftest.sh` -> bad.jar exit=1 NOT J2ME-SAFE (isEmpty, ArrayList, NO_STACKMAP), clean.jar exit=0, SELFTEST PASS.
- 2026-10-29 [A1 mới] T05 đạt: `tools/difftest/oracles/run_all.sh tools/env/cache/out/hello.jar` -> oracles ran: microemu freej2me, SAME (HELLO-JLKVM / sum=25), rc=0. phoneME/KEmulator/JL-Mod-dex ghi V4.
- 2026-10-29 [A1 mới] T06 đạt: `tests/vmspec/run_vmspec.sh` -> pass=37 fail=0 spec-only=3 (neg01-03, nguồn đặc tả, lớp lỗi UNVERIFIED), mọi case có `source:` trong meta.txt và expected.txt; `tests/midlets` selftest chạy trên 2 oracle SAME (3 PASS); `tests/games/README.md` có quy ước + định dạng .inp. Lưu ý: oracle là JVM chứ không phải CLDC VM (D18/R2). Cụm M0 T01–T06 xong.
- 2026-10-29 [quản đốc] NGHIỆM THU M0 (T01–T06): chạy lại `assembleDebug :vm-core:checkNoAndroidImports :vm-core:test` OK, `jarscan/selftest.sh` PASS, `run_vmspec.sh` pass=37 fail=0 spec-only=3, oracle hello SAME (microemu == freej2me). Quyết định: (1) TinySoundFont (submodule JL-Mod) vendored chỉ `tsf.h`/`tml.h`/LICENSE (MIT) tại đúng commit ghim `0d10306`, xem `VENDORED.txt`; (2) Maven Central trả 429 qua proxy → init script `~/.gradle/init.d/maven-mirror.gradle` (ngoài repo) chuyển sang mirror Google; script tải phải dùng mirror này; (3) V4 (thiếu phoneME/KEmulator/JL-Mod-dex) chấp nhận cho M0, các case CLDC-riêng giữ `UNVERIFIED`.
- 2026-10-29 [A1 mới] T07 đạt: `vm-core/.../classfile/{ClassParser,ClassFile,ConstantPool,Descriptors,ClassFormatException}` + `jlkvm/ClassSource`. `./gradlew :vm-core:test`: parse 242/242 class (jar vmspec + stub cldcapi11/midpapi20, gồm raw 45.x), fuzz 10k (seed cố định) rejected=9040 accepted=960, không crash host. Test cần `tests/vmspec/run_vmspec.sh` đã chạy (assume nếu thiếu corpus). ROM chưa có.
- 2026-10-29 [A1 mới] T08 đạt (chờ quản đốc nghiệm thu): viết lại `verify/MethodVerifier` (bản dở chỉ có khung) + `Verifier`; strict/lenient, uninit, long/double 2 từ, handler, mảng, interface=Object, jsr/ret từ chối. `./gradlew --no-daemon :vm-core:test`: vmspec 92 class chấp nhận / 3 từ chối (neg01–03 đúng); midlets+hello+clean 7/0; stub 147/0; stress (microemu, ecj, gson qua ProGuard -microedition) 12052 method chấp nhận, 0 từ chối ngoài ý muốn (78 `ldc Class` ở v47 bị từ chối đúng); ASM `tests/vmspec/gen_verify_cases.sh` 16 positive chấp nhận + 42 negative bị từ chối (pc + lý do khớp), lenient 58/58; fuzz 33k mẫu không crash. So HotSpot-21: chỉ khác ở quy tắc riêng CLDC. phoneME không có (V4), đặc tả CLDC không tải được (V5) → mọi điểm chưa chắc ghi UNVERIFIED trong `docs/verifier-report.md`. `RawGen` m05 thêm StackMap (vmspec vẫn pass=37 fail=0 spec-only=3).
- 2026-10-29 [A1 mới] T09 đạt: `verify/TypeMaps` ((method,pc) → `RefMap` bitmap locals+stack, `at` = trước lệnh, `atCall` = caller trong lúc invoke, cache theo method); `TypeMapsTest` 11 test (10 method mẫu tính tay: long/double, uninit, uninitThis, handler, interface, mảng, null, field, dup_x2/dup2_x2) pass.
- 2026-10-30 [quản đốc] NGHIỆM THU T08 ĐẠT (kèm T08a sửa nhỏ), T09 đạt: chạy lại `:vm-core:test` 22/22 (Corpus, Generated, Stress, Fuzz, TypeMaps), soát `MethodVerifier`: StackMap strict, handler frame, uninit/backward branch, `<init>` đúng lớp, invokespecial, CLDC 1.0 float. Chấp nhận các điểm UNVERIFIED trong `docs/verifier-report.md` (V4/V5). Phát hiện: mảng gán được vào Cloneable/Serializable → T08a.
- 2026-10-30 [quản đốc] V5 ĐÃ GIẢI QUYẾT: người dùng cung cấp đặc tả CLDC 1.1 FR (JSR-139, 03/2003) — lưu cục bộ, KHÔNG commit (giấy phép Oracle): `tools/env/cache/spec/{CLDCSpecification1.1.pdf,.txt, Appendix1-verifier.pdf,.txt, javadocs/}`. Điểm đã xác minh: (a) Appendix1 §isJavaAssignable: mảng chỉ là subtype của `java/lang/Object`; `isArrayInterface` không có clause trong CLDC → xác nhận T08a; (b) Spec §5.3 (dòng ~1318–1337, 1518–1522): preverifier inline và xoá mọi jsr/jsr_w/ret/wide ret; verifier kiểu StackMap từ chối chúng; chỉ VM dùng verifier JVMS §4.9 truyền thống mới được hỗ trợ jsr/ret → strict từ chối là ĐÚNG, interpreter vẫn thực thi jsr/ret cho lenient; (c) Spec §4.2/§5.1.4: Error ngoài danh sách CLDC (§6.2.7.2: Error, NoClassDefFoundError, OutOfMemoryError, VirtualMachineError) → VM hoặc dừng theo cách riêng, hoặc ném **lớp cha gần nhất có trong CLDC** của Error theo JVMS (vd. VerifyError/NoSuchMethodError/NoSuchFieldError/ClassFormatError/ExceptionInInitializerError → `java.lang.Error`; StackOverflowError/InternalError → `VirtualMachineError`). Hành vi máy Nokia cụ thể (dừng hay ném) vẫn UNVERIFIED (V2/V4). Javadoc CLDC 1.1 chính thức dùng được làm nguồn bề mặt API cho T18 (đối chiếu với DB từ stub MicroEmulator).

- 2026-10-30 [A1 mới #2] T08a đạt: `classAssignable` mảng chỉ gán được `Object`/mảng (Appendix1 dòng 441–447); thêm case ASM n42/n43 (int[]→Cloneable, Object[]→Serializable bị từ chối); `./gradlew --no-daemon :vm-core:test` 22/22 qua (GeneratedVerify 60 case); `docs/verifier-report.md` cập nhật nguồn (Appendix1, §5.3, §4.2/5.1.4).

- 2026-10-30 [A1 mới #2] T10 đạt: `vm-core/.../heap/{Heap,TempRoots}`, `docs/heap-layout.md`; `HeapTest` 4/4 (mọi kiểu field/phần tử, mảng đóng gói, cấp phát vượt đúng 1 byte -> gọi collector rồi trả 0 = OOM, freeMemory khớp sổ sách, heap duyệt tuyến tính, hash xác định). Header 2 word (class id+kind+cờ, hash), mảng thêm 1 word length.
- 2026-10-30 [A1 mới #2] T11 đạt (chờ quản đốc nghiệm thu): `vm-core/.../{Vm,HostPlatform,ExecutionEngine,VmConfig,DeviceProfile}`, `interp/{Interpreter,Frames,NativeCtx,CoreNatives}`, `link/{Linker,ClassInfo,RMethod,RField,Errors,VmThrow}`, `thread/{VmThread,Scheduler,Monitors}`, `natives/{NativeMethod,NativeContext,NativeRegistry,GuestServices,Blocker,Handles}` đúng chữ ký "Interface giữa module". Interpreter chạy đủ opcode 0..201 (trừ 186 không tồn tại): chứng minh bằng đếm opcode (`-Djlkvm.opstats`, tập thiếu = rỗng sau khi thêm case o01/o02/o03). Frame = vùng int[] (locals, header 7 word, operand stack) nên T13 quét được bằng TypeMaps; stack trace guest có số dòng (`Throwable.printStackTrace`, log lỗi chưa bắt). Lệnh: `tests/vmspec/run_vmspec.sh --engine jlkvm` -> pass=43 fail=0 (40 case cũ + o01_opcodes, o02_raw_ops, o03_jsr_ret ghi từ oracle MicroEmu+FreeJ2ME); `./gradlew --no-daemon :vm-core:test` 28 test qua (gồm `VmSpecTest` 43 case chạy trong tiến trình, `HeapTest`); `run_vmspec.sh` (oracle) vẫn qua các case đã sửa. Thay đổi corpus: (1) i03 và neg01/02/03 đổi expected thành `java.lang.Error` theo CLDC §4.2/§5.1.4 (ExceptionInInitializerError/VerifyError không có trong CLDC; i03 thêm `diverges: microemu|freej2me`); (2) e01 dùng IllegalArgumentException thay IllegalStateException (lớp này không có trong CLDC 1.1); (3) khoá meta mới `jlkvm-args:` (o03 chạy `--noverify` vì jsr/ret bị verifier từ chối đúng). TẠM THỜI (ghi rõ, T17/T18/T21-T23 thay): ROM stub guest `vm-core/src/test/guest-stub/{src,build.sh}` (Object, String, Throwable, Thread, System, PrintStream, MIDlet, Canvas/Display/Image/Graphics tối giản...; biên dịch -source 1.3 bootclasspath rỗng + ProGuard preverify, verifier strict qua cả ROM), host thử `vm-core/src/test/java/jlkvm/tmp/{TempHost,JarSource,RunJar}` (native console, Float/Double.toString, getAppProperty, Image phần mềm), `tools/difftest/oracles/run_jlkvm.sh`. KHUNG đã làm (KHÔNG tick): T12 (phân giải lười, <clinit> bằng frame F_INIT + yield khi thread khác đang khởi tạo, ánh xạ lỗi `Errors` + `docs/error-mapping.md`, không kiểm quyền truy cập), T14 (round-robin theo bytecode, monitor bàn giao quyền, wait/notify/sleep/join/interrupt, structured locking; chưa có deadlock detect, chưa xác định/D14), T15 (registry, NativeContext, Blocker poll, Handles; chưa sweep peer). Chưa có: GC (heap đầy -> OOM), Runtime.gc là no-op, T13.
- 2026-10-30 [quản đốc] NGHIỆM THU T08a, T10 ĐẠT; T11 ĐẠT: chạy lại `:vm-core:test` OK, `run_vmspec.sh --engine jlkvm` pass=43 fail=0; không có TODO/UnsupportedOperation trong vm-core; soát idiv/irem (MIN/-1 đúng nhờ ngữ nghĩa Java host), f2i/d2l bão hoà, NegativeArraySize, ArrayStore, StackOverflow→VirtualMachineError, invokespecial. Đổi expected `e01` (IllegalStateException không có trong CLDC 1.1 — xác nhận bằng javadoc đặc tả) và `i03/neg01–03` → `java.lang.Error` (§4.2) là ĐÚNG. **Yêu cầu cho T12:** hiện `invokespecial` luôn áp ngữ nghĩa ACC_SUPER (Interpreter.java ~dòng 440) — T12 phải xử lý class thiếu cờ ACC_SUPER (version 45.x, ngữ nghĩa JVMS cũ: gọi đúng method được tham chiếu, không chọn lại ở superclass) + case vmspec cho cả hai trường hợp. Công nhân trước dừng ở ~371k ngữ cảnh → thay người mới cho T12+.

- 2026-10-30 [A1 mới #3] T12 đạt: `Linker` (kiểm quyền lớp/field/method theo JVMS 5.4.4, ICCE theo tag Methodref/InterfaceMethodref, `<init>` không kế thừa, `specialTarget` ACC_SUPER theo cờ của lớp gọi, `selectInterface` ICCE/IllegalAccessError, lớp hệ thống `java.*`/`javax.microedition.*`/`com.nokia.mid.*` từ JAR app bị từ chối theo CLDC 3.4.2.2), `Interpreter` (checkcast/instanceof null không phân giải, `new` abstract trước init, putfield/putstatic final khác lớp -> IllegalAccessError, catch-type phân giải lỗi thay ngoại lệ, EIIE -> `java.lang.Error` kèm stack gốc qua `Vm.wrapInitializerError`, `Vm.launch` khởi tạo lớp chính), `ClassInfo.selectVirtual` (abstract chặn, không rơi xuống lớp cha), `Class.forName` (lớp cha thiếu vẫn NoClassDefFoundError). `Errors.error` giữ tên JVMS ở đầu message khi bị ánh xạ. `tests/vmspec/run_vmspec.sh --engine jlkvm` -> pass=50 fail=0 (thêm k03, k04, l01 NCDFE timing, l02 32 kịch bản truy cập, l03 gói bảo vệ, i05 init đa luồng, i06 lỗi init; đổi k01 expected B.m -> A.m: thiếu ACC_SUPER theo JVMS 2nd 6.4, 2 oracle HotSpot lệch ghi `diverges`, UNVERIFIED trên KVM); oracle: k03/k04/l02/i05/i06 khớp 2 oracle, l01 chỉ MicroEmu (FreeJ2ME không chạy được). `./gradlew --no-daemon :vm-core:test` OK. `docs/error-mapping.md` viết lại kèm nguồn (số dòng CLDC spec).

- 2026-10-30 [A1 mới #3] T13 đạt (chờ quản đốc nghiệm thu): `vm-core/.../gc/Gc.java` (mark bằng stack tường minh -> WeakReference xoá referent -> dọn handle chủ chết (`Disposable`) -> bảng run sống -> viết lại mọi root/trường -> trượt `System.arraycopy`; hash ở header nên sống qua nén), roots: frame mọi thread qua `TypeMaps` (`at` cho frame trên cùng và caller của frame <clinit>, `atCall` cho caller trong invoke; đối số native đang chạy theo `RMethod.argWordIsRef` + `VmThread.inNative`), static, mirror, `strRefs`, intern, TempRoots, `Vm.hostRootCells()` (OOM), `threadObj/waitRef/locks`, monitor (`Monitors.dropDead/relocate`). Safepoint (D5): interpreter công bố fp/sp/pc/method TRƯỚC mỗi lệnh cấp phát (`new/newarray/anewarray/multianewarray/ldc/mirror của static synchronized`, mirror nay cấp phát trước khi dựng frame), GC không bao giờ chạy giữa các bytecode khác; native/helper dùng TempRoots; chi tiết `docs/gc.md` (+ ghi chú: local chết theo StackMap không phải root; method không verify thì không GC). `Runtime.gc()` -> `Vm.collectGarbage()` thật. Kiểm: `./gradlew --no-daemon :vm-core:test` OK (80s; GcTest: g01 1M cấp phát đồ thị ngẫu nhiên heap 3 MB + gcVerify = 26 lần GC, checksum khớp 2 oracle; hash qua nén; WeakReference; handle về 0; thread ngủ/wait/chặn monitor; OOM rồi phục hồi; stress mỗi cấp phát GC 1 lần + validate toàn heap); `tests/vmspec/run_vmspec.sh --engine jlkvm` pass=56 fail=0; thêm `JLKVM_ARGS="--gc-stress 1 --gc-verify"` cho cả corpus -> pass=56 (trừ case --noverify/`no-gc-stress`); oracle pass=51 spec-only=5. Case mới g01-g06. RunJar có `--heap-kb --gc-stress --gc-verify --gc-stats`.

- 2026-10-30 [A1 mới #3] T13b (KHÔNG tick, thiếu Android): số đo DESKTOP (Xeon 2.1 GHz, 7 workload, checksum khớp HotSpot 7/7) trong `docs/bench.md`, chạy bằng `tests/bench/run_bench.sh`. JDK 8/21 cờ mặc định: arith 22.8/21.4, long 13.9/13.1, calls 13.7/13.0, arrays 21.7/19.9, alloc 13.4/12.6, strings 12.0/11.9, fp 12.8/12.3 Mbc/s; với `-XX:-DontCompileHugeMethods`: 78–149 Mbc/s. Nguyên nhân chênh: `Interpreter.run` 9744 byte > ngưỡng 8000 của HotSpot (xem V7). Quản đốc quyết: ngưỡng + tách `run()` + đo Android (V6).
- 2026-10-30 [quản đốc] NGHIỆM THU T12 ĐẠT, T13 ĐẠT: chạy lại `:vm-core:test` OK, `run_vmspec.sh --engine jlkvm` với `--gc-stress 1 --gc-verify` 56/56; soát `gc/Gc.java`: roots (stack qua TypeMaps, statics, mirror, literal cache, intern, TempRoots, thread, monitor, OOM dựng sẵn), weak referent không trace + clear, handle owner yếu, nén cập nhật monitor/handle. Chấp nhận: method `--noverify` không GC được (chỉ chế độ debug); `k01` theo JVMS (không ACC_SUPER → `A.m`) khác HotSpot, ghi diverges; từ chối lớp `java.*`/`javax.microedition.*`/`com.nokia.mid.*` trong JAR app theo CLDC §3.4.2.2 — lớp lỗi cụ thể và hành vi Nokia thật UNVERIFIED, nếu game thật bị chặn oan sẽ xử lý bằng tuỳ chọn profile (không làm bây giờ). **T13b: CHƯA tick** — quyết định tạm (D2): GIỮ lõi Java, giao T11a tách vòng lặp; đo Android chờ có APK chạy được (M6) hoặc người dùng chạy benchmark trên máy (V6).

- 2026-10-29 [A1 mới #4] TẠM DỪNG: đang ở T11a (mới đọc PLAN + Interpreter.java, chưa sửa file nào, cây mã nguyên trạng), đã xong: không có task mới; bước tiếp theo: T11a tách `Interpreter.run()` (hiện 9774 byte theo `javap -c`, cần < 8000) rồi đo lại `tests/bench/run_bench.sh` cờ mặc định, sau đó T14 → T15 → T16; lưu ý: chạy gradle phải dùng JDK 11+ (JAVA_HOME=/opt/jdk8 làm `./gradlew` fail vì AGP cần JVM ≥ 11; chỉ dùng /opt/jdk8 cho javac/javap/bench), số Mbc/s trước T11a là bảng trong docs/bench.md (JDK8 mặc định 12–23 Mbc/s).

- 2026-10-30 [A1 mới #4] T11a đạt: `interp/RareOps.java` (mới) + các method phụ trong `Interpreter` (ldc, wide, multianewarray, monitor, returnChecks, invokeNative); `javap -c`: `run()` 9774 -> 6826 byte, method khác < 460. Bench cờ MẶC ĐỊNH JDK 8: arith 22.8->132.3, long 13.9->113.2, calls 13.7->108.2, arrays 21.7->144.8, alloc 13.4->108.9, strings 12.0->100.7, fp 12.8->111.9 Mbc/s (với `-XX:-DontCompileHugeMethods`: 98–140, tức 100% ≥ 70%); JDK 21 mặc định 97–151; checksum 7/7 khớp HotSpot. `run_vmspec.sh --engine jlkvm` 56/56, với `JLKVM_ARGS="--gc-stress 1 --gc-verify"` 56/56; `env -u JAVA_HOME ./gradlew :vm-core:test` qua; `docs/bench.md` cập nhật. T13b vẫn chờ Android (V6).

- 2026-10-30 [A1 mới #4] T14 đạt: `thread/Scheduler` (hàng đợi theo ưu tiên 1..10, round robin trong mỗi mức; `DeviceProfile.priorityScheduling` tắt được; `Thread.setPriority0` native + đọc field `priority` khi start; timeslice = `profile.timesliceBytecodes`), chế độ xác định D14 (`VmConfig.deterministic`: đồng hồ ảo tăng `nanosPerBytecode`/bytecode, nhảy tới timer kế khi mọi thread ngủ, epoch cố định), phát hiện deadlock ở debug (`Monitors.detectDeadlock` khi thread chặn ở monitor: chu trình chờ-giữ, log + `Vm.lastDeadlock` kèm stack guest; `reportStall` khi mọi thread chặn vĩnh viễn — CHƯA có test riêng), sleep/wait(timeout)/notify/notifyAll/join/interrupt/yield/synchronized đã có từ trước và vmspec m01–m05, i05, g05 vẫn qua. Case mới p01_producer_consumer (khớp 2 oracle), p02_deadlock, p03_threads_schedule; `ThreadTest` (5 test): producer–consumer, deadlock báo ở debug / không báo ở release, 10 lần cùng seed output y hệt (và seed khác chỉ đổi hash), thứ tự ưu tiên. `run_vmspec.sh --engine jlkvm` 59/59, với `--gc-stress 1 --gc-verify` 59/59; `:vm-core:test` qua; bench không đổi (143/108 Mbc/s). UNVERIFIED: KVM Nokia có lập lịch theo ưu tiên chặt hay không (V2/V4); lớp `java.lang.Thread` thật ở T18 phải gọi native `setPriority0(I)V` và có field `priority`.

- 2026-10-30 [A1 mới #4] T15 đạt: `NativeRegistry` (khoá "lớp.tên(descriptor)" được kiểm, đăng ký trùng -> IllegalStateException, `keys()`), `Vm.missingNatives` + log "missing native" khi nạp lớp ở debug (gọi native thiếu -> guest `java.lang.Error` "UnsatisfiedLinkError: key"), `natives/AsyncBlocker` (host thread khác gọi `finish()`, hoàn tất trên vm-thread). Sửa cơ chế block: thread bị treo NGAY TRONG lệnh invoke, đối số còn trên operand stack (GC giữ và dời tham chiếu qua `th.inNative`), Blocker đọc lại được `c.argRef(i)` sau khi thức, `completeBlocked` mới pop đối số, đẩy kết quả, tăng pc; ngoại lệ từ blocker mang message (`VmThread.pendingMsg`). Case `q01_natives` (spec-only, native mẫu trong `TempHost`: mọi kiểu đối số/kết quả, throwNew, native thiếu, 2 thread cùng bị chặn thứ tự đúng, thread khác tiếp tục chạy + cấp phát khi 1 thread bị chặn); `NativesTest` 5 test (có chạy heap 1 MB + gcVerify trong lúc thread bị treo). vmspec jlkvm 60/60 cả thường lẫn `--gc-stress 1 --gc-verify`; `:vm-core:test` qua. Ghi chú: host-desktop mới chỉ có package-info (T23), nên native mẫu nằm ở host thử `TempHost`; Handles/TempRoots giữ nguyên (T13).

- 2026-10-30 [A1 mới #4] T16 đạt: `vm-core/.../debug/{DebugFlags,Debug,TraceRing,InputLog}` + hook trong Interpreter/Linker/Gc/Monitors/Scheduler/Vm, tất cả dạng `if (DebugFlags.ENABLED && ...)` (hằng biên dịch D15: debug = `src/main/.../DebugFlags.java` true; release = `src/release/.../DebugFlags.java` false, chọn bằng `-Pjlkvm.release=true` hoặc `JLKVM_RELEASE=1` cho `run_jlkvm.sh`). Có: log nạp lớp/native thiếu/GC/deadlock (`debug`), log ngoại lệ kèm stack guest (`logExceptions`), đếm bytecode theo method (`countBytecodes`), profiler cấp phát theo (method, pc) kèm dòng nguồn (`profileAlloc`), ring buffer N lệnh cuối (tự dump trước ngoại lệ không bắt), chế độ xác định + ghi/phát lại input D14 (`recordInput/replayInput`, stamp = ns ảo; phát lại bỏ input sống; khi rảnh đồng hồ ảo nhảy tới timer/sự kiện kế). Case `d01_input_replay` (meta `driven-by-test`, `run_vmspec.sh` chỉ build), `InputReplayTest` (ghi 1 lần, phát lại 2 lần, output y hệt), `DebugHooksTest`; `docs/debug.md`. ĐO RELEASE vs gỡ hẳn hook (bản trước T16, JDK 8, 7 workload x 3 vòng xen kẽ, best-of-4, Mbc/s base/release/debug): arith 145.2/144.9/140.7, long 111.3/110.0/111.1, calls 109.0/111.6/101.9, arrays 142.1/144.3/135.5, alloc 107.1/106.4/100.8, strings 104.0/104.5/100.6, fp 119.4/120.4/116.2 -> trung bình nhân release/base = 1.0045 (best) / 0.9943 (mean) tức chênh < 1% (< 2% yêu cầu); `javap`: `run()` bản release giống bản gốc trừ +4 byte, debug 6937 byte < 8000 (JIT được). Debug (hook biên dịch vào, mọi tính năng tắt) chậm ~4%. vmspec jlkvm 60/60 (thường, `--gc-stress 1 --gc-verify`, và bản release `JLKVM_RELEASE=1`), `:vm-core:test` qua cả hai cấu hình Gradle biên dịch (`-Pjlkvm.release=true` chỉ kiểm biên dịch, test cần bản debug).
- 2026-10-30 [quản đốc] NGHIỆM THU T11a, T14, T15, T16 ĐẠT: chạy lại `:vm-core:test` OK; vmspec 60/60 ở cả thường, `--gc-stress 1 --gc-verify`, `JLKVM_RELEASE=1`; `javap` bản debug `Interpreter.run()` = 6963 byte < 8000. Bench JDK8 cờ mặc định 101–145 Mbc/s (trước 12–23) → V7 đóng; quyết định D2 tạm giữ lõi Java, T13b chỉ còn chờ số đo Android (V6). M2 xong trừ T13b. Giao M3 (T17–T20) cho công nhân mới.
- 2026-10-30 [A1 mới #5] KIỂM KÊ be838ff (code dở của người trước, đã đọc/chạy lại, không tin mù): `tools/romgen/RomGen.java` đúng (javac JDK8 -source/-target 1.3, bootclasspath = thư mục rỗng hoặc ROM nền, ProGuard -microedition trong tiến trình, kiểm đóng: mọi lớp được nhắc trong CP/descriptor/throws phải có trong ROM, verifier strict của dự án cho từng lớp, tuỳ chọn diff bề mặt D9, jar xác định) -> giữ; `Differ` thêm `IMPL_BITS` (bỏ qua synchronized/native/strict khi so — chi tiết hiện thực, javadoc không nêu) + `--ignore PREFIX` -> giữ; `javadoc_crosscheck.py` chạy: DB cldc-1.1 vs javadoc chính thức = 0 khác biệt (DB từ stub MicroEmulator ĐÚNG, không cần sửa); 86 file vm-classlib: biên dịch được, bề mặt == DB, và qua test hành vi (xem T18/T19).
- 2026-10-30 [A1 mới #5] T17 đạt: `vm-classlib/build.gradle.kts` task `buildRom` (gắn vào assemble/check) chạy romgen -> `vm-classlib/build/rom/rom.jar` (91 lớp, 81 CLDC + 10 `jlkvm/internal/**`); `RomGenTest` 7/7 (đóng kín bằng ASM; nguồn guest dùng `java.util.HashMap` / `String` ngoài thư viện -> javac fail vì bootclasspath rỗng; thừa 1 method public -> "API surface differs"; thư viện thật build + verify + diff == cldc-1.1). Lệnh: `env -u JAVA_HOME ./gradlew --no-daemon :vm-classlib:buildRom :tools:romgen:test`, `tools/romgen/romgen --src vm-classlib/src --out X.jar --api-db tools/api-sigs/db/cldc-1.1.json --api-ignore jlkvm/internal/`. ROM test của vm-core = ROM thật + overlay nhỏ `vm-core/src/test/guest-stub/src` (chỉ MIDlet/MIDletBoot/lcdui tối giản + handler `testnet`; các lớp java.* của stub cũ đã xoá; `build.sh` gọi romgen 2 lần: nền rồi `--base`). Chưa "đóng vào asset" host-android (module còn rỗng; M6).
- 2026-10-30 [A1 mới #5] T18 đạt: `api-sigs diff --ignore jlkvm/internal/ db/cldc-1.1.json <DB trích từ vm-classlib/build/rom/rom.jar>` = 0 (không có `--ignore`: đúng 3 lớp `+ class jlkvm/internal/{Encoding,Encoding$Decoder,io/ConnectionBase}`); `javadoc_crosscheck.py` trên DB trích từ ROM (bỏ lớp internal) = 0 khác biệt với javadoc CHÍNH THỨC (DB stub MicroEmulator cũng = 0 -> không cần sửa DB). Cách xử lý lớp nội bộ: package `jlkvm.internal` public trong ROM nhưng bị loại khỏi diff bằng tiền tố VÀ bị Linker ẩn với lớp app (`Linker.isRomPrivate`: tham chiếu/extends/`Class.forName` từ app -> NoClassDefFoundError/ClassNotFoundException; case `cl08_romprivate`); lớp package-private (ConsoleOutputStream, CalendarImpl...) không vào bề mặt. Test classlib: 11 case mới `tests/vmspec/cases/cl01..cl11` (string/StringBuffer, số, float/double, util, io, encoding, system, rom-private, lang/Thread/ref/Class/resource, Character, encoding mặc định) — expected ghi từ oracle MicroEmu/FreeJ2ME khi khớp, ngoại lệ ghi `diverges`/notes trong meta (cl03: JDK8 vs JDK21; cl09: tên thread MicroEmu, resource tương đối FreeJ2ME, GC); lỗi tìm thấy và sửa: thread chính (`Vm.launch`) không có priority/name (priority 0, name null) -> đặt NORM_PRIORITY + "main"; `Character.isLowerCase(U+1E9F)`. Thread thật có field `priority` + native `setPriority0(I)V` (đã có, case p03). Hồi quy: `run_vmspec.sh --engine jlkvm` 69/69 ở thường, `JLKVM_ARGS="--gc-stress 1 --gc-verify"`, `JLKVM_RELEASE=1`; `env -u JAVA_HOME ./gradlew --no-daemon :vm-core:test :tools:romgen:test :tools:api-sigs:test :vm-classlib:buildRom` BUILD SUCCESSFUL (70 test, 0 lỗi/skip). Chi tiết `docs/classlib.md`.
- 2026-10-30 [A1 mới #5] T19 đạt: `Float/Double.toString` + `parseFloat/parseDouble` là native VM `interp/FloatText` (chữ số NGẮN NHẤT quay vòng được, ít nhất 2 chữ số `4.9E-324`, văn phạm JDK 1.3), không phụ thuộc JDK host; `FloatTextTest` so 120k giá trị ngẫu nhiên + biên với `Double/Float.toString` của JDK 21 = khớp 100%. KHÁC BIỆT ORACLE (ghi rõ): MicroEmulator chạy JDK 8 (`FloatingDecimal` không phải ngắn nhất: `1.0E23`->`9.999999999999999E22`, `8.41E21`->`8.409999999999999E21`, `Float.MIN_NORMAL`->`1.17549435E-38`, bug JDK-4511638) còn FreeJ2ME (JDK 21) khớp VM; javadoc CLDC 1.1 Double.toString đòi chữ số ngắn nhất nên `cl03_float` expected = dạng FreeJ2ME/javadoc (`diverges: microemu`); giá trị máy Nokia thật UNVERIFIED. Encoding: ISO-8859-1/UTF-8 (+ASCII, UTF-16) trong `jlkvm.internal.Encoding`, mặc định = `microedition.encoding` của profile (test `ClasslibSystemTest`: Latin-1 `41e93fff`, UTF-8 `41c3a9e282acc3bf`, ngoài hỗ trợ -> Latin-1; UTF-8 sai -> U+FFFD), literal intern (s01), Integer/Long parse/toString (cl02, so oracle). Không có phoneME (V4) nên đối chiếu bằng MicroEmu/FreeJ2ME.
- 2026-10-30 [A1 mới #5] T20 đạt: `jlkvm.profile.{MiniJson,ProfileLoader}` + `profiles/jlkvm-min.json` (tạm, mọi giá trị UNVERIFIED; khoá không rõ bị từ chối, khoá `_...` là chú thích); `DeviceProfile` thêm `network` (allow/deny/offline theo scheme + `networkDefault`, scheme cục bộ file/comm/resource/storage không áp chính sách) và `timeZoneId/OffsetMillis`; native mới `Connector.policy0`, `TimeZone.defaultId0/defaultOffset0` (+ `System.getProperty0`, `Runtime.*`, `currentTimeMillis` đã có). `ClasslibSystemTest` 10/10: thuộc tính in ra khớp từng khoá của JSON (kể cả JSON khác), khoá lạ/sai hoa thường -> `null`, `null`->NPE, `""`->IAE, `CLDC-1.1` đúng chính tả; `totalMemory`=kích thước arena, free giảm/hồi sau `gc()`, OOM bắt được; đồng hồ ảo ở chế độ xác định (2 lần chạy y hệt); `Connector.open`: deny->SecurityException, offline->IOException, allow->handler `jlkvm.internal.io.<scheme>.Protocol` (overlay test `testnet`) hoặc ConnectionNotFoundException, URL sai->IllegalArgumentException. RunJar có `--profile-json FILE`. Phạm vi: không có traffic mạng thật (PLAN); HttpConnection thuộc MIDP (M4).
- 2026-10-30 [quản đốc] NGHIỆM THU M3 (T17–T20) ĐẠT: chạy lại gradle `:vm-core:test :tools:romgen:test :tools:api-sigs:test :vm-classlib:buildRom` OK; vmspec 69/69 (thường + `--gc-stress 1 --gc-verify`); tự trích chữ ký `rom.jar` (84 lớp) → `api-sigs diff --ignore jlkvm/internal/` vs cldc-1.1 = 0 (không ignore: chỉ 3 lớp `jlkvm/internal/*`, Linker ẩn khỏi app). V8 (nguồn gốc thuật toán guest) đưa vào T41. **Tổng kết đợt 1: A1 tick 20/21 (≥ 90%)**, còn T13b chờ đo Android (V6). Đợt 1 kết thúc; M4 (T21–T27, A2) chờ người dùng xác nhận mở đợt 2.
- 2026-10-30 [A2] T21 đạt (phần desktop; Android chờ M6): `vm-classlib/midp/src` (MIDlet đủ API + Bridge + `jlkvm.internal.Ams/MidletHooks`), `host-common` (`ams/Descriptor` — tái dùng thuật toán `ru.woesss.j2me.jar.Descriptor` bỏ phần Android UI —, `MidletSuite` gộp manifest+JAD, `AmsHost`), `host-desktop` (`DesktopHost`, `HeadlessRunner`, `ScriptedInput` đọc định dạng `.inp` của tests/games, đồng hồ ảo D14, `RomLayers` theo `profile.apis`). Test `:host-desktop:test` (`DesktopHostTest.lifecycleOrder`): MIDlet `tests/midlets/lifecycle` ghi đúng thứ tự constructor → startApp → hideNotify+pauseApp / startApp+showNotify (pause/resume giả lập onPause/onResume) → notifyPaused/resumeRequest → destroyApp(false) bị từ chối → destroyApp(true); getAppProperty (JAD thắng manifest, tên không phân biệt hoa thường), platformRequest, checkPermission, chọn MIDlet-2. CHƯA kiểm trên Android: nối `AmsHost.pause()/resume()` vào Activity (M6/T35).
- 2026-10-30 [A2] T22 đạt (chờ quản đốc nghiệm thu): `jlkvm.internal.EventLoop` (thread guest `event`, hàng đợi job guest + native `next0` chặn chỉ thread này, công bằng 1 job : 1 sự kiện host), `Display/Displayable/Canvas/Dispatcher/Command/...` (D13); mã phím + softkey + repeat lấy từ profile (`keys{}`, `keyRepeat{}`, `display{}`); assert debug `Vm.checkVmThread` (lời gọi guest từ thread khác vm-thread → log + `threadViolations` + IllegalStateException; `postEvent/requestExit` vẫn hợp lệ ở mọi thread). Test: `DesktopHostTest` (thứ tự paint/showNotify/key/commandAction/serviceRepaints/callSerially/sizeChanged/full-screen/pause, mọi callback trên 1 thread, phát lại 2 lần giống hệt, profile khác đổi mã phím), vmspec `ev01_display_order` so oracle MicroEmu + FreeJ2ME: bảng khác biệt trong `docs/midp.md` §3 và V10. Hồi quy: vmspec jlkvm 71/71 (69 cũ + ev01 + gfx01) thường, `--gc-stress 1 --gc-verify`, `JLKVM_RELEASE=1`.
- 2026-10-30 [A2] T23 ĐẠT PHẦN DESKTOP, KHÔNG tick (tiêu chí hash giống Android chưa kiểm được): guest `Graphics` (trạng thái rẻ, native nhận trạng thái qua đối số), `Image` (PNG giải mã ở host, `imagesCountInHeap` tính pixel vào heap guest, handle host nhả khi Image chết), `Font`; `host-common` `Surface/SurfaceFactory` = interface native chung, `SoftRaster` (Java thuần, tất định, không AWT), `PngDecoder` (mọi loại màu/độ sâu, tRNS, Adam7), font bitmap DejaVu nướng sẵn. MIDlet `gfx01_graphics` (clip, anchor, drawRegion 8 phép biến đổi, createImage 8 phép, drawRGB alpha, getRGB, ảnh mutable/immutable, PNG từ JAR, ngoại lệ) → 64 dòng fingerprint, chạy 2 lần giống hệt (`GraphicsHostTest`); so oracle: khớp MicroEmu 38/64, FreeJ2ME 36/64, khác biệt từng dòng + nguyên nhân trong `docs/midp.md` §4 (DOTTED, elip/chữ do rasteriser, drawRegion BOTTOM|RIGHT của MicroEmu lệch 1 px, alpha, kiểm tra tham số). CÒN LẠI cho T23: host-android (V12), hash pixel desktop = Android.
- 2026-10-30 [A2] BÀN GIAO (cụm T21–T23 xong, T23 chỉ phần desktop): đã xong T21, T22 (chờ quản đốc nghiệm thu), T23-desktop; bước tiếp theo: T24 (lcdui cấp cao: Form/List/TextBox/Choice*/StringItem/ImageItem/Spacer/DateField/CustomItem/TextField trên nền Item/Screen/Alert/Gauge đã có phần mô hình; bỏ dần danh sách `pending` trong `vm-classlib/build.gradle.kts`), rồi T25 (GameCanvas/Sprite/TiledLayer trên Graphics), T26, T27; lưu ý: đọc `docs/midp.md` trước; test host chạy bằng `env -u JAVA_HOME ./gradlew --no-daemon :host-common:test :host-desktop:test :vm-core:test` (cần `tests/vmspec/run_vmspec.sh --engine jlkvm` đã chạy ít nhất một lần và `vm-core/src/test/guest-stub/build.sh` để ROM test mới); sau khi sửa guest `vm-classlib/midp/src` phải chạy lại `guest-stub/build.sh` (gradle test không tự dựng ROM test); suite có thuộc tính `JLKVM-No-Event-Loop` chỉ dành cho case d01 (host tự đọc sự kiện). Hồi quy cuối cụm: vmspec jlkvm 71/71 thường + `--gc-stress 1 --gc-verify` + `JLKVM_RELEASE=1`; gradle test 89 test (vm-core, host-common, host-desktop, romgen, api-sigs) 0 lỗi; `api-sigs diff --ignore jlkvm/internal/` cldc-1.1 vs ROM = 0; `buildMidpRom` bề mặt == cldc-1.1 ∪ midp-2.0 trừ danh sách `pending` (V11).
- 2026-10-30 [quản đốc] NGHIỆM THU T21 (phần desktop) ĐẠT, T22 ĐẠT (soát `docs/midp.md` §3: một thread `event`, gộp repaint đúng MIDP, callSerially FIFO sau paint đang chờ, soft key theo profile, assert vm-thread), T23 phần desktop ĐẠT — giữ CHƯA tick tới khi so hash Android (V12/M6). V9/V11 đã quyết, V10 tạm chấp nhận. Công nhân A2 trước vượt 400k (462k) → thay người mới cho T22a/T24/T25.

- 2026-10-30 [A2 mới] T22a đạt: `vm-classlib/midp/src/java/util/{Timer,TimerTask}.java` (thread guest riêng, hàng đợi theo thời gian, ngữ nghĩa J2SE/MIDP: fixed-delay tính từ lúc BẮT ĐẦU lần chạy trước, fixed-rate bắt kịp, cancel trong run, task ném ngoại lệ kết thúc Timer -> `IllegalStateException` cho schedule sau đó; không có purge, D9). Bỏ `java/util/Timer` khỏi `pending`: `buildMidpRom` bề mặt == cldc-1.1 ∪ midp-2.0 (ngoại trừ phần pending còn lại). Case `tests/vmspec/cases/tm01_timer` (33 dòng; ghi từ FreeJ2ME = Timer của JDK; MicroEmulator có Timer riêng sơ sài, `diverges: microemu`); `vm-core` `TimerTest`: chế độ xác định (đồng hồ ảo) chạy 10 lần giống hệt và thời điểm chạy chính xác (fixed-rate `100 350 350 400 500`). Hồi quy: vmspec jlkvm 72/72 thường + `--gc-stress 1 --gc-verify` + `JLKVM_RELEASE=1`; gradle `:vm-core:test :tools:romgen:test :tools:api-sigs:test :vm-classlib:buildRom :vm-classlib:buildMidpRom :host-common:test :host-desktop:test` BUILD SUCCESSFUL.

- 2026-10-30 [A2 mới] T24 ĐẠT PHẦN DESKTOP (host-android UI thật = M6, ghi rõ trong `docs/midp.md` §6): guest `Form, List, TextBox, Alert, Ticker, Command, Item, ChoiceGroup, Gauge, DateField, TextField, StringItem, ImageItem, Spacer, CustomItem, ItemStateListener, Choice` (model + mọi luật/ngoại lệ MIDP trong guest; `ChoiceModel`/`TextModel` dùng chung), menu khi > 2 command cho Canvas (vẽ bởi thư viện, phím trái mở menu, phải = BACK/CANCEL/EXIT/STOP). Giao thức màn hình cấp cao: guest gửi MÔ TẢ đầy đủ (`Ui.sync0`, `lcdui.ScreenCodec` ↔ `host-common` `ScreenModel.decode`, gộp thay đổi bằng `Displayable.touch()`), host báo thao tác người dùng bằng `Vm.postEvent` (`HostEvents.UI_*`) → `Display.uiEvent` trên thread `event` → áp vào model rồi gọi `commandAction/itemStateChanged` (luôn qua EventLoop, D13; `notifyStateChanged` cũng post); interface chung `jlkvm.host.ui.ScreenHost` (+ `MidpNatives` nhận `ScreenHost`), `host-desktop` `HeadlessUi` + động từ kịch bản `ui dump|cmd|dismiss|select|text|item N cmd|choice|text|gauge|date|custom` (`ScriptedInput` kind UI). Kiểm: `host-desktop` `UiHostTest` (MIDlet mẫu `tests/midlets/ui` + `scripts/ui.inp`: List chọn → Form (nút, TextField, 2 ChoiceGroup, Gauge, DateField, CustomItem) → TextBox số (nhập sai bị bỏ) → Alert dismiss → Canvas 4 command với menu; golden `tests/midlets/expected/ui.txt` + `ui-dumps.txt`; mọi callback trên thread event, `threadViolations`=0, chạy 2 lần giống hệt, hành động trên màn hình cũ bị bỏ qua); vmspec `ui01_form_items`, `ui02_choice`, `ui03_text` (spec-only: cả MicroEmulator và FreeJ2ME sai nhiều luật model — liệt kê khác biệt trong `meta.txt` từng case); bỏ T24 khỏi `pending`: `buildMidpRom` bề mặt == cldc-1.1 ∪ midp-2.0 (trừ pending còn `game/ rms/ media/ pki/ io/`). Sửa nhỏ: `VmSpecTest` bỏ qua case `realtime:` (tm01), `DebugHooksTest` xét top 40 method. Hồi quy: vmspec jlkvm 75/75 thường + `--gc-stress 1 --gc-verify` + `JLKVM_RELEASE=1`; gradle `:vm-core:test :tools:romgen:test :tools:api-sigs:test :vm-classlib:buildRom :vm-classlib:buildMidpRom :host-common:test :host-desktop:test` BUILD SUCCESSFUL. Layout: `getPreferred*/getMinimum*` là ƯỚC LƯỢNG từ metrics font (host layout thật quyết định); thứ tự DISMISS_COMMAND→chuyển màn UNVERIFIED (V10).

- 2026-10-30 [A2 mới] T25 ĐẠT (desktop): `vm-classlib/midp/src/javax/microedition/lcdui/game/{GameCanvas,Layer,LayerManager,Sprite,TiledLayer,PixelMask}` (Java guest trên Graphics) + hook ROM-private `jlkvm.internal.{KeyTap,CanvasAccess,Access}` (GameCanvas thấy phím trước ứng dụng mà không thêm thành viên public); bỏ `game/` khỏi `pending`: `buildMidpRom` bề mặt == cldc-1.1 ∪ midp-2.0 (còn pending `rms/ media/ pki/ io/`). getKeyStates latch theo MIDP (press+release giữa hai lần gọi thấy 1 lần, phím giữ thấy mọi lần; suppressKeyEvents chỉ nuốt phím có game action); flushGraphics = repaint+serviceRepaints (paint trên thread event); va chạm pixel qua mặt nạ alpha dựng lười (opaque = alpha 255, hai oracle đồng ý, UNVERIFIED), kiểm chứng độc lập: dòng "self check" so pixel Sprite.paint (rasteriser) với collidesWith pixel cho 8 phép biến đổi = 0 lệch; animated tile; LayerManager (view window, khôi phục translate/clip). Đối chiếu oracle: `gm05_sprite_paint` và paint TiledLayer (`gm03`) có checksum GIỐNG FreeJ2ME từng dòng (8 transform + ref pixel, frame, sequence, clip+translate); MicroEmulator lệch nhiều; khác biệt liệt kê trong `meta.txt` từng case (`gm01..gm05`): FreeJ2ME sai hình chữ nhật va chạm khi transform và va chạm pixel với vài transform, `append` layer đã có ném IOOBE; MicroEmulator sai vị trí/kích thước sau setTransform, LayerManager. DoD 7 (không cấp phát trong vòng vẽ): bỏ cấp phát `int[2]` mỗi lần `drawImage/drawRegion/copyArea` (`placeX/placeY`), `drawString/drawSubstring/stringWidth` đưa String thẳng cho host (native mới `Graphics.drawStr0`, `Font.strWidth0`), `drawChar` dùng buffer tái sử dụng, `Canvas.paintNow` tái dùng Graphics màn hình; đo bằng MIDlet `tests/midlets/game` + `GameHostTest`: 200 khung (LayerManager.paint Sprite+TiledLayer, 3 va chạm, flushGraphics) cấp phát 0 byte heap guest (freeMemory trước/sau; profiler `--profile` không có điểm cấp phát theo khung). jarscan ui.jar/game.jar: không lỗi mới. Hồi quy: vmspec jlkvm 80/80 thường + `--gc-stress 1 --gc-verify` + `JLKVM_RELEASE=1`; gradle `:vm-core:test :tools:romgen:test :tools:api-sigs:test :vm-classlib:buildRom :vm-classlib:buildMidpRom :host-common:test :host-desktop:test` BUILD SUCCESSFUL; `api-sigs diff --ignore jlkvm/internal/` cldc-1.1 vs ROM = 0. Chưa làm: hash pixel desktop = Android / host-android (V12, M6); tiêu chí "khớp MicroEmu/FreeJ2ME" chỉ ở mức liệt kê khác biệt vì không oracle nào đáng tin trên va chạm pixel.
- 2026-10-30 [A2 mới] BÀN GIAO (T22a, T24, T25 xong; T24/T25 chỉ phần desktop): bước tiếp theo T26 (RMS) → T26b (GCF MIDP + pki; còn trong `pending`) → T27 (MMAPI + tone); lưu ý: guest MIDP sửa xong phải dựng lại ROM test (`tests/vmspec/run_vmspec.sh --engine jlkvm` tự gọi `guest-stub/build.sh`); đừng chạy gradle test song song với `run_vmspec.sh` (cùng ghi `tests/vmspec/build`); host-android phải cài `PlatformServices`, `SurfaceFactory` và `ScreenHost` (view Android từ `ScreenModel`, UI thread, R7) rồi truyền vào `MidpNatives`; case `realtime:` (tm01) không chạy trong `VmSpecTest`.
- 2026-10-30 [quản đốc] Người dùng gửi bộ game test (`tests/games/vip/`, KHÔNG commit; bỏ WIPI). Quét 68 JAR bằng jarscan → `docs/games-api-scan.md`: 25/68 sạch CLDC1.1+MIDP2.0. API ngoài profile: **WMA `javax.wireless.messaging` 33 game** (gửi SMS — cần chạy offline có kiểm soát), `com.nokia.mid.ui` 12, FileConnection 9, PIM 3, `media.control.MIDIControl/TempoControl` 1 (JSR-135 đầy đủ), `com.nokia.mid.sound` 1. 6 bản mod thiếu StackMap (tới 850 method), 1 có class version 50. **Quyết định ưu tiên:** sau M4, làm T28 (Nokia UI lõi) và phần WMA + JSR-75 của T32 trước các mục khác của M5; T27 phải có MIDIControl/TempoControl khi profile khai báo JSR-135 đầy đủ.
- 2026-10-30 [A2 mới 2] T26 ĐẠT: guest `javax.microedition.rms` (`RecordStore`, `RecordEnumerationImpl` gói riêng, `RecordEnumeration/Filter/Comparator/Listener`, 5 exception) + ROM-private `jlkvm.internal.Rms`; host `host-common` `jlkvm.host.rms.{RecordStorage, MemoryRecordStorage, FileRecordStorage, JlModRmsImport}` + `jlkvm.host.RmsNatives` (`MidpNatives` nhận thêm `RecordStorage`, mặc định bộ nhớ; host-desktop `HeadlessRunner --rms-dir DIR`, `--import-jlmod-rms DIR`). Profile mới `rms{quotaBytes,storeOverheadBytes,recordOverheadBytes}` (mặc định 262144/16/8, UNVERIFIED; `ProfileLoader` + `jlkvm-min.json`): quota theo SUITE (mọi store của suite chung), `getSize` = overhead store + Σ(dữ liệu + overhead record), `getSizeAvailable` = quota − đã dùng, add/set vượt → `RecordStoreFullException` không đổi gì. Luật MIDP ở guest: tên 1..32 (null → NPE như cả hai oracle), mở lại trả CÙNG object + đếm tham chiếu (đóng cuối xoá listener/enumeration), id không dùng lại kể cả sau khởi động lại, `getRecord` record rỗng → null, buffer nhỏ → AIOOBE, authmode/writable chỉ áp khi tạo, mở store suite khác theo vendor+suite (chỉ AUTHMODE_ANY, ngược lại SecurityException; không writable → SecurityException khi add/set/delete/setMode), listener chạy đồng bộ trên thread guest gây thay đổi, enumeration: filter/comparator (merge sort ổn định), con trỏ nằm GIỮA phần tử, keepUpdated/rebuild/destroy (IllegalStateException). Lưu: file `<root>/<suite>/<tên>.rms` ghi qua .tmp + rename. Nhập save JL-Mod (định dạng rõ: `<tên>.rsh` "MIDRMS" 3.0 + `<tên>.<id>.rsr`, đọc từ `AndroidRecordStoreManager`/`RecordStoreImpl`): `JlModRmsImport`, không áp quota, file hỏng bỏ qua và liệt kê. Bỏ `rms/` khỏi `pending` (còn `media/ pki/ io/`). Test: vmspec `rms01_store` (spec-only; MicroEmulator/FreeJ2ME đều sập ở ca biên — khác biệt trong `meta.txt`), `RecordStorageTest` (host-common), `RmsHostTest` (host-desktop: store sống qua các lần chạy, chia sẻ giữa suite `tests/midlets/rms`+`rmsb`, quota 1000/16/8 từ profile với `rmsq` → RecordStoreFullException, nhập JL-Mod). Hồi quy: vmspec jlkvm 81/81 thường + `--gc-stress 1 --gc-verify` + `JLKVM_RELEASE=1`; gradle `:vm-core:test :tools:romgen:test :tools:api-sigs:test :vm-classlib:buildRom :vm-classlib:buildMidpRom :host-common:test :host-desktop:test` BUILD SUCCESSFUL; buildRom "api surface == cldc-1.1.json" (diff CLDC = 0), buildMidpRom bề mặt == cldc-1.1 ∪ midp-2.0. UNVERIFIED (V10): số quota/overhead, `getRecord` rỗng → null (theo javadoc, oracle không xác nhận), vị trí con trỏ sau rebuild. Chưa làm: host-android (dùng `FileRecordStorage` trên `getFilesDir()`), tag nguồn T33 cho 3 số rms. Chi tiết `docs/midp.md` §8.
- 2026-10-30 [A2 mới 2] T26b ĐẠT: interface sinh từ DB midp-2.0 (`HttpConnection, HttpsConnection, SocketConnection, ServerSocketConnection, UDPDatagramConnection, CommConnection, SecureConnection, SecurityInfo`, `pki.Certificate`) + `PushRegistry`, `pki.CertificateException` trong `vm-classlib/midp/src`; bỏ `io/` và `pki/` khỏi `pending` (CHỈ CÒN `media/` của T27). Chính sách T20 giữ nguyên (deny → SecurityException, offline → IOException — mặc định, allow → tìm handler; scheme không có handler → ConnectionNotFoundException). Handler DUY NHẤT là `http` (`jlkvm.internal.io.http.Protocol`, trạng thái Setup/Connected/Closed, GET/HEAD/POST, request property, body gom trong bộ nhớ, gửi ở lần gọi đầu cần response, không theo redirect, 4xx/5xx không ném, `getHeaderField(n)` không tính status line, parse ngày RFC1123); host: `host-common` `NetNatives` ↔ ROM-private `jlkvm.internal.Net` + `net.HttpTransport` (`JavaHttpTransport` = HttpURLConnection, body ≤ 16 MiB, timeout 30 s) chạy trên thread riêng chỉ treo thread guest gọi (`AsyncBlocker`), native kiểm lại chính sách `http` = allow trước khi tạo thread (host offline/deny không bao giờ mở socket). `PushRegistry`: không có push — kiểm đối số rồi `registerConnection/registerAlarm` → ConnectionNotFoundException, danh sách rỗng. Test: `NetHostTest` (host-desktop; MIDlet `tests/midlets/net`, JDK HttpServer loopback, JAD `Test-Url`): offline → IOException mọi scheme, deny → SecurityException, deny/offline không có kết nối nào tới server, allow → GET đúng mã/message/header (tên, chỉ số, int, ngày)/type/length/body đọc sau khi đóng connection, 404 không ngoại lệ, 302 không theo, HEAD, POST có body + Content-Type, GET có body thành POST, lỗi trạng thái, connection refused → IOException; vmspec `io01_gcf_midp` (hằng số, phân cấp interface, PushRegistry, CertificateException, Connector theo profile mặc định của runner). Hồi quy: vmspec jlkvm 82/82 thường + `--gc-stress 1 --gc-verify` + `JLKVM_RELEASE=1`; gradle `:vm-core:test :tools:romgen:test :tools:api-sigs:test :vm-classlib:buildRom :vm-classlib:buildMidpRom :host-common:test :host-desktop:test` BUILD SUCCESSFUL; diff CLDC = 0 (buildRom "api surface == cldc-1.1.json"), buildMidpRom bề mặt == cldc-1.1 ∪ midp-2.0 với pending chỉ `media/`. Chưa làm / UNVERIFIED: handler https/socket/serversocket/datagram/ssl/sms (chỉ interface → ConnectionNotFoundException khi allow), `Certificate` chưa có hiện thực (không có https), GET có body → POST, `getFile()` null khi URL không có path, push thật. Chi tiết `docs/midp.md` §9.
- 2026-10-30 [quản đốc] NGHIỆM THU T26 + T26b ĐẠT (phần desktop; host-android để M6): chạy lại gradle `:vm-core:test :tools:romgen:test :tools:api-sigs:test :vm-classlib:buildRom :vm-classlib:buildMidpRom :host-common:test :host-desktop:test` OK; vmspec `--engine jlkvm` 82/82 thường + `JLKVM_ARGS="--gc-stress 1 --gc-verify"` + `JLKVM_RELEASE=1`; `pending` chỉ còn `media/` (T27). Phát hiện lỗi nhỏ của runner (có từ M3): ở chế độ oracle, case spec-only có `driven-by-test` mà không có `expected.txt` (cl07_system, cl11_default_encoding) bị tính FAIL vì kiểm `expected.txt` trước `driven-by-test` → giao kèm T27 sửa thứ tự.
- 2026-10-30 [A2 mới 3] T27 ĐẠT (phần desktop; host-android = M6): `javax.microedition.media` (MIDP subset: Manager, Player, PlayerListener, Control(lable), MediaException, ToneControl, VolumeControl; bề mặt == midp-2.0.json) trong `vm-classlib/midp/src`, `pending` RỖNG; tầng thứ ba `vm-classlib/jsr135/src` -> `rom-jsr135.jar` (`:vm-classlib:buildJsr135Rom`) chứa `MIDIControl, RateControl, TempoControl` + hiện thực (chữ ký lấy bằng `api-sigs extract` từ interface đã biên dịch của JL-Mod, Apache-2.0 -> `tools/api-sigs/db/jsr-135-extra.json`; UNVERIFIED so với JSR-135 vì không có stub/javadoc trong repo); profile phải khai báo `"jsr-135"` trong `apis` (RomLayers), nếu không lớp không tồn tại cho game và `getControl("MIDIControl")` = null. Guest: `BasePlayer` (máy trạng thái 100/200/300/400/0 + ngoại lệ theo javadoc, loop, media time µs, listener, VolumeControl), `TonePlayer` (tone-seq MIDP + RTTTL/RTX: thread guest hẹn giờ tuyệt đối trên `System.currentTimeMillis`, phát từng nốt qua `Audio.tone0`; KHÔNG qua decoder/ffmpeg), `ClipPlayer` (MIDI/WAV/MP3/AMR do host phát; dữ liệu stage từng khúc 1 KiB sang host, không nằm trong heap guest; kết thúc clip học qua thread guest đỗ ở `Audio.await0` + `AsyncBlocker`), `Events` (MỌI `playerUpdate` chạy trên một thread guest `media-events`, không bao giờ trong lời gọi gây ra nó, không bao giờ từ thread host). Parser RTTTL (`tone/Rtttl`) và trình biên dịch tone sequence (`tone/ToneSeq`) viết ở GUEST (lý do: một bản dùng chung cho Android/desktop, tất định, dùng lại cho Smart Messaging OTT ở T32, biên dịch như Java thường trong test host-common). Host: `host-common` `jlkvm.host.audio.AudioDevice` (interface tone/MIDI/clip, chỗ cắm Sonivox/TSF), `RecordingAudioDevice` (headless: log có thời gian ảo + PCM giả lập tất định qua `ToneSynth`, `audio end N` trong script), `AudioNatives`; profile mới `media{contentTypes[],protocols[],tone}` (`DeviceProfile` + `ProfileLoader`, mặc định UNVERIFIED). Test: `ToneParsersTest` (host-common; RTTTL: thiếu default, octave/duration/dấu chấm trước-sau octave, bpm, hoa/thường, tên dài/rỗng, BOM, dòng trống, 19 ca lỗi; tone-seq: thời lượng, block/repeat/volume, 25 ca lỗi, giới hạn 65536 sự kiện), vmspec `mm01_player_states` (69 dòng, spec-only) + `mm02_tone` (41 dòng, spec-only; nguồn javadoc ghi trong meta.txt), `MediaHostTest` (host-desktop; MIDlet `tests/midlets/mmapi` + `mmapi135`: RTTTL = đúng 2 tone cách 500 ms, PCM 6800 mẫu, clip MIDI 2 vòng + restart/stop/close, WAV 500 ms từ header, EOM do host báo tới listener trên thread guest, danh sách MIME theo profile, thiết bị từ chối kiểu, MIDIControl/TempoControl có/không theo profile, tempo nửa = chậm gấp đôi đo trên đồng hồ ảo). Sửa runner `run_vmspec.sh`: chế độ oracle xử lý `driven-by-test` TRƯỚC nhánh `expected.txt` (cl07_system/cl11_default_encoding báo `SPEC`, không FAIL). Hồi quy: gradle `:vm-core:test :tools:romgen:test :tools:api-sigs:test :vm-classlib:buildRom :vm-classlib:buildMidpRom :host-common:test :host-desktop:test` BUILD SUCCESSFUL; vmspec jlkvm 84/84 thường + `--gc-stress 1 --gc-verify` + `JLKVM_RELEASE=1`; diff CLDC = 0; `pending` rỗng; buildMidpRom bề mặt == cldc-1.1 ∪ midp-2.0; buildJsr135Rom bề mặt == cldc-1.1 ∪ midp-2.0 ∪ jsr-135-extra. UNVERIFIED (chi tiết `docs/midp.md` §10): danh sách MIME/protocol mặc định, mức volume mặc định 100, `close()` khi đang STARTED gửi STOPPED trước CLOSED, VOLUME_CHANGED chỉ khi đổi, start sau END_OF_MEDIA phát lại từ đầu, TEMPO/RESOLUTION cho phép ở giữa chuỗi tone, player `device://tone` chưa đặt sequence = im lặng, khoảng giá trị MIDIControl/Rate/Tempo và kẹp giá trị, default octave RTTTL 6 / octave 0..8, MIDI synth dùng chung cho cả thiết bị. CHƯA LÀM: host-android `AudioDevice` (Sonivox/TSF của JL-Mod, M6; độ trễ/đa âm chưa đo), thiết bị desktop phát âm thật (tuỳ chọn), `PitchControl/StopTimeControl/MetaDataControl/RecordControl/VideoControl/GUIControl/FramePositioningControl/TimeBase/DataSource`, handler Connector `file`/`resource` (nên `Manager.createPlayer(locator)` chỉ chạy với `device://tone` và `http` khi policy cho phép; game dùng `createPlayer(InputStream, type)` không bị ảnh hưởng), `com.nokia.mid.sound` (T28), OTT Smart Messaging (T32).
- 2026-10-30 [quản đốc] NGHIỆM THU T27 ĐẠT (phần desktop): chạy lại gradle `:vm-core:test :tools:romgen:test :tools:api-sigs:test :vm-classlib:assemble :host-common:test :host-desktop:test` OK; vmspec `--engine jlkvm` 84/84 thường + `--gc-stress 1 --gc-verify` (công nhân thêm `JLKVM_RELEASE=1` 84/84); `pending` rỗng → kiểm tra D9 tầng MIDP đạt trọn. **Tổng kết đợt 2 (M4):** T21, T22, T22a, T24–T27 phần desktop xong; T23 và phần host-android của T21/T24–T27 (V12/V13) chờ M6. Đợt 3 (M5 theo thứ tự ưu tiên: T28 Nokia UI lõi → WMA + JSR-75 của T32) chờ người dùng mở.
- 2026-10-30 [quản đốc] Người dùng mở đợt 3 (M5). Giao T28 (Nokia UI lõi) cho công nhân A3 mới theo V14; sau T28 là WMA + JSR-75 của T32 (tách thành T32a nếu cần). Luật thay công nhân >400k giữ nguyên; công nhân dừng ở ~300k và ghi BÀN GIAO.

---

## Prompt khởi động cho công nhân Sonnet 5.5 (sau khi clear context)
```
Đọc PLAN.md ở gốc dự án rồi implement lần lượt từng task chưa tick trong Checklist được giao cho bạn (A1 hoặc A2).
Làm đúng phạm vi từng task, chạy kiểm tra theo "Hoàn thành khi" và quy trình Definition of Done, tick [x] và ghi Nhật ký.
Không đổi Quyết định thiết kế; có vấn đề thì ghi vào Vướng mắc và báo quản đốc.
A2: chỉ đọc trước, không sửa file/không build cho tới khi quản đốc xác nhận A1 ≥ 90%.
```
