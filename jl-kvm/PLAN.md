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
- [ ] T12. Liên kết, phân giải, khởi tạo lớp
  - Agent: A1 (phụ thuộc T11)
  - Đầu vào/đầu ra: phân giải lười, kiểm quyền truy cập, `<clinit>` đúng JVM spec, xử lý class thiếu `ACC_SUPER`, ánh xạ lỗi sang lớp có trong CLDC
  - File: `vm-core/.../link/*`
  - Hoàn thành khi: test thời điểm ném `NoClassDefFoundError`, `<clinit>` đệ quy, `<clinit>` lỗi cho kết quả khớp oracle; bảng ánh xạ lỗi ghi trong `docs/error-mapping.md` kèm nguồn
- [ ] T13. GC chính xác + nén + WeakReference + dọn peer — **Quản đốc nghiệm thu**
  - Agent: A1 (phụ thuộc T09, T10)
  - Đầu vào/đầu ra: root = stack (qua TypeMaps), static, chuỗi intern, temp roots, bảng handle, monitor; dọn peer host khi object guest chết
  - File: `vm-core/.../gc/*`
  - Hoàn thành khi: stress 1 triệu cấp phát đồ thị ngẫu nhiên có checksum không sai; hash định danh giữ nguyên sau nén; bảng handle host không rò (đếm về 0 sau khi thả hết)
- [ ] T13b. Benchmark sớm (cổng quyết định D2)
  - Agent: A1 (phụ thuộc T11)
  - Đầu vào/đầu ra: vòng lặp số học, gọi method ảo, truy cập mảng, cấp phát → triệu bytecode/giây trên desktop và 1 máy Android
  - File: `tests/bench/*`, `docs/bench.md`
  - Hoàn thành khi: có số đo; quản đốc so với nhu cầu game (ngưỡng quản đốc chốt sau khi có số đầu tiên) và ghi quyết định giữ lõi Java hay mở nhánh lõi C++
- [ ] T14. Scheduler green thread + monitor
  - Agent: A1 (phụ thuộc T11)
  - Đầu vào/đầu ra: timeslice theo bytecode (từ profile), ưu tiên, `sleep/wait/notify/notifyAll/join/interrupt/yield`, `synchronized`; phát hiện deadlock (debug)
  - File: `vm-core/.../thread/*`
  - Hoàn thành khi: test producer–consumer, deadlock cố ý (debug báo); cùng seed chạy 10 lần ra output y hệt
- [ ] T15. Native registry + NativeContext + blocker + bảng handle
  - Agent: A1 (phụ thuộc T11)
  - Đầu vào/đầu ra: theo "Interface giữa module"
  - File: `vm-core/.../natives/*`
  - Hoàn thành khi: native mẫu trên host-desktop chạy; native chặn treo đúng 1 thread guest, các thread khác vẫn chạy; native thiếu bị báo khi nạp lớp
- [ ] T16. Debug & trace
  - Agent: A1
  - Đầu vào/đầu ra: log nạp lớp, ngoại lệ (stack guest), profiler vị trí cấp phát (theo pc), log GC, đếm bytecode theo method, ring buffer trace, chế độ xác định + ghi/phát lại input (D14)
  - File: `vm-core/.../debug/*`
  - Hoàn thành khi: bản debug có đủ log; bản release đo benchmark chênh < 2% so với khi gỡ hẳn hook

### M3 — Thư viện CLDC 1.1 (guest)
- [ ] T17. Build pipeline thư viện guest + `romgen`
  - Agent: A1 (phụ thuộc T02, T08)
  - Đầu vào/đầu ra: `vm-classlib/src` → biên dịch theo D11 → preverify → verifier của dự án → `rom.jar` (đóng vào asset)
  - File: `vm-classlib/build.gradle.kts`, `tools/romgen/*`
  - Hoàn thành khi: build fail nếu mã guest tham chiếu lớp ngoài chính nó; ROM qua verifier strict
- [ ] T18. `java.lang` / `java.util` / `java.io` / `java.lang.ref` đúng bề mặt CLDC 1.1
  - Agent: A1 (phụ thuộc T17, T03)
  - Đầu vào/đầu ra: lớp guest theo D12 (nguồn mã ghi ở header từng file)
  - File: `vm-classlib/src/java/**`
  - Hoàn thành khi: `api-sigs diff` (ROM vs CLDC 1.1) = 0 khác biệt; test classlib qua
- [ ] T19. String, encoding, chuyển số
  - Agent: A1 (phụ thuộc T18)
  - Đầu vào/đầu ra: `String/StringBuffer`, intern, ISO-8859-1/UTF-8, encoding mặc định theo profile, `Integer/Long` parse/toString, `Float/Double.toString`
  - File: `vm-classlib/src/java/lang/*`
  - Hoàn thành khi: bộ test chuỗi khớp output phoneME; khác biệt `Double.toString` (nếu có) được ghi rõ là theo oracle nào
- [ ] T20. System/Runtime + thuộc tính theo profile + GCF cơ sở
  - Agent: A1 (phụ thuộc T18)
  - Đầu vào/đầu ra: `System.getProperty` từ profile (sửa `CLDC-1.1`), `Runtime.gc/freeMemory/totalMemory` từ arena, `currentTimeMillis` (đồng hồ ảo ở chế độ xác định); `Connector` + chính sách mạng theo profile
  - File: `vm-classlib/src/java/lang/System.java`, `.../Runtime.java`, `vm-classlib/src/javax/microedition/io/*`
  - Hoàn thành khi: test in thuộc tính khớp profile JSON, khoá lạ → `null`; `Connector.open("http://…")` cho đúng hành vi (cho phép/từ chối/offline)

### M4 — MIDP 2.x + host (A2 đọc trước, chờ A1 ≥ 90%)
- [ ] T21. Vòng đời MIDlet + JAD/manifest + AMS
  - Agent: A2
  - Đầu vào/đầu ra: tái dùng `ru.woesss.j2me.jar.Descriptor`; `startApp/pauseApp/destroyApp`, `notifyDestroyed`, `getAppProperty`, `platformRequest`
  - File: `vm-classlib/src/javax/microedition/midlet/*`, `host-*/.../ams/*`
  - Hoàn thành khi: MIDlet test ghi đúng thứ tự vòng đời, kể cả pause/resume theo `onPause/onResume` của Android
- [ ] T22. EventLoop + Display + mã phím theo profile — **Quản đốc nghiệm thu**
  - Agent: A2 (phụ thuộc T21)
  - Đầu vào/đầu ra: D13; gộp repaint, `serviceRepaints`, `callSerially`, `setCurrent`, `showNotify/hideNotify`, `sizeChanged`; mã phím Nokia (soft −6/−7, chọn −5, mũi tên −1…−4…) lấy từ profile
  - File: `vm-classlib/src/javax/microedition/lcdui/{Display,Displayable,Canvas}.java`, `vm-classlib/src/jlkvm/internal/EventLoop.java`
  - Hoàn thành khi: test thứ tự sự kiện khớp oracle; không có lời gọi guest nào từ thread Android ngoài vm-thread (assert trong debug)
- [ ] T23. Graphics / Image / Font
  - Agent: A2 (phụ thuộc T22)
  - Đầu vào/đầu ra: guest giữ trạng thái rẻ (màu, translate, clip), native vẽ; host-android chuyển backend JL-Mod; host-desktop raster phần mềm
  - File: `vm-classlib/src/javax/microedition/lcdui/{Graphics,Image,Font}.java`, `host-android/.../lcdui/*`, `host-desktop/.../lcdui/*`
  - Hoàn thành khi: MIDlet kiểm đồ hoạ (clip, anchor, `drawRegion` 8 phép biến đổi, `drawRGB` alpha, `getRGB`, ảnh mutable/immutable) cho hash pixel giống nhau giữa desktop và Android; khác biệt với KEmulator được liệt kê
- [ ] T24. lcdui cấp cao (Form, List, TextBox, Alert, Ticker, Command, Item)
  - Agent: A2 (phụ thuộc T22)
  - File: `vm-classlib/src/javax/microedition/lcdui/*`, `host-android/.../lcdui/ui/*`
  - Hoàn thành khi: MIDlet mẫu thao tác được; `commandAction` luôn đến qua EventLoop
- [ ] T25. Game API (GameCanvas, Sprite, TiledLayer, LayerManager)
  - Agent: A2 (phụ thuộc T23)
  - Đầu vào/đầu ra: phần lớn là Java guest thuần trên Graphics; va chạm pixel
  - Hoàn thành khi: test `getKeyStates`, `flushGraphics`, va chạm pixel, animated tile qua
- [ ] T26. RMS
  - Agent: A2 (phụ thuộc T21)
  - Đầu vào/đầu ra: giới hạn dung lượng theo profile, thứ tự enumeration, ngoại lệ; tuỳ chọn nhập save từ JL-Mod
  - Hoàn thành khi: test RMS (đầy bộ nhớ, `getSizeAvailable`, xoá/thêm) khớp profile
- [ ] T27. MMAPI + tone
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
- [ ] T41. Kiểm toán license: Apache-2.0 (NOTICE), EPL (M3G), LGPL v3 (MobileFFmpeg), MPL 2.0 (FilePicker); không có mã GPL trong APK
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

---

## Prompt khởi động cho công nhân Sonnet 5.5 (sau khi clear context)
```
Đọc PLAN.md ở gốc dự án rồi implement lần lượt từng task chưa tick trong Checklist được giao cho bạn (A1 hoặc A2).
Làm đúng phạm vi từng task, chạy kiểm tra theo "Hoàn thành khi" và quy trình Definition of Done, tick [x] và ghi Nhật ký.
Không đổi Quyết định thiết kế; có vấn đề thì ghi vào Vướng mắc và báo quản đốc.
A2: chỉ đọc trước, không sửa file/không build cho tới khi quản đốc xác nhận A1 ≥ 90%.
```
