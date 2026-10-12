# Ghi chú đặt tên — cụm sdk (u cw v by cb dg ai ac an ak p ap s af z ay ar aq aa al ad ag am r q t ah w aj bq n br az bk a aw y bt f ch df)

Bảng: `mapping/part_sdk.map` (41 class, ~780 dòng). Đây là SDK "cổng/cộng đồng QQ" của nhà phát hành (lõi kiểu Tencent) nhúng vào game, **không dính tới gameplay**.

* **Mạng:** `HttpRequestQueue` (ap) hàng đợi tối đa 20 `HttpRequest` (af) do `HttpWorker` (s) xử lý; `HttpConnection` với header Q-UA, proxy CMWAP `10.0.0.172`, theo redirect, giới hạn 400 KB.
  `PortalProtocol` (ak) dựng gói nhị phân mã hoá `QQTeaCipher` (aj, TEA 16 vòng, khoá `TT@Game911&007**`); phản hồi parse bằng `MarkupParser` (z) → `MarkupTag/Attribute` (ay, ar).
* **UI cổng:** `PortalCanvasBase` (ai) → `PortalBrowser` (ac) duyệt trang bằng widget (`PageTextWidget`, `PageInputWidget`, `PageButtonWidget`, `PageImageWidget`, `PageSeparatorWidget`; ag, am, r, q, t, ah),
  nhập đa nhấn `MultiTapInput` (w), lịch sử `PageSnapshot` (aa), `PageRequest` (al). `PortalManager` (an) = bảng lệnh → URL, phiên `_uid/_sid/_a8`. `PortalClient` (u) = ngữ cảnh SDK; `PortalApi` (cw) = mặt tiền; `PortalListener` (v), `PortalResult` (by).
* **Cộng đồng QQ:** `QQCommunityOverlay` (bk) là lớp phủ "Cộng đồng QQ" (hồ sơ `QQUserProfile` cb, `LeaderboardEntry` dg, tải điểm); `ab/bs/da` gọi.
* **Mã hoá/băm:** `Md5` (n, hex HOA, băm mật khẩu ô nhập), `Md5Text` (br, hex thường, kiểm giá trị JAD), `IdeaCipher` (bq), `Base64Decoder` (az, giải ảnh thẻ `img`).
* **Thanh toán:** `PayServerRequest/Connection/Timeout` (aw, y, bt) POST nhị phân tới `wap.moonic.cn/payServer.do`; `LotteryRequest` (f); `SmsPaymentFlow` (ch) và `PaidServiceMenu` (df: "Dịch vụ ưu đãi", mua **không qua SMS**).
  `SmsFeeConfig` (a) đọc khoá JAD thanh toán nhưng **không ai khởi tạo** (mã chết).
* LOW: `u.platformId` (=888888), `ai.isEmbeddedMode`, `af.sngameServerUrl2`, `z.isParsing/isReentered` (ghi không đọc), `al.protocolVersion`, `f.amount` (luôn 0).

Phát hiện (không sửa): gửi SMS (`bh` = `SmsSendTask`) không đọc nội dung/số điện thoại mà luôn gọi `onResult(0,null)` — thanh toán SMS luôn "thành công" trong bản này; `SmsFeeConfig` là mã chết.
