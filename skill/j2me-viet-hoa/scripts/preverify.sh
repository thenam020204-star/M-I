#!/usr/bin/env bash
# Preverify CLDC (sinh attribute StackMap) cho cả jar bằng ProGuard -microedition.
# Không shrink/optimize/obfuscate -> chỉ thêm StackMap, tên & logic giữ nguyên.
#   bash preverify.sh in.jar out.jar [lib.jar ...]
# Thư viện mặc định: freej2me_plus.jar ($FJ, có đủ javax.microedition + com.nokia) + java.base.
# Class đầu vào phải version <= 48 và KHÔNG còn StackMapTable. Class mới: biên dịch bằng
# ecj -source 1.3 -target 1.3 (như tools/build_class.sh của bộ font) rồi mới chạy script này.
# BitmapFont.class của bộ font đã preverify sẵn; chạy lại script vẫn an toàn.
set -e
IN="$1"; OUT="$2"; shift 2
LIBS="${*:-$FJ}"
# ProGuard hiểu đường dẫn tương đối trong file @.pro theo thư mục của file .pro (mktemp -> /tmp),
# không phải thư mục đang đứng -> đổi hết sang đường dẫn tuyệt đối.
IN=$(realpath "$IN"); OUT=$(realpath -m "$OUT")
LIBS=$(for l in $LIBS; do realpath "$l"; done)
JM="$(dirname "$(dirname "$(readlink -f "$(which java)")")")/jmods/java.base.jmod"
[ -f "$JM" ] || { echo "LỖI: JDK này không có jmods/java.base.jmod (chỉ là JRE). Cài JDK đầy đủ, ví dụ: apt-get install openjdk-21-jdk-headless"; exit 1; }
PRO=$(mktemp --suffix=.pro)
{ echo "-injars $IN"; echo "-outjars $OUT"
  for l in $LIBS; do echo "-libraryjars $l"; done
  echo '-libraryjars <java.home>/jmods/java.base.jmod(!**.jar;!module-info.class)'
  echo '-dontshrink'; echo '-dontoptimize'; echo '-dontobfuscate'; echo '-microedition'
  echo '-dontwarn'; echo '-dontnote'; echo '-keepattributes *'; echo '-keep class * { *; }'
} > "$PRO"
LOG=$(env -u JAVA_TOOL_OPTIONS java -jar "${PROGUARD:?source tools/env.sh trước}" @"$PRO" 2>&1) \
  || { echo "$LOG" | head -15; rm -f "$PRO"; echo "LỖI: ProGuard preverify thất bại"; exit 1; }
echo "$LOG" | tail -2
rm -f "$PRO"
[ -s "$OUT" ] || { echo "LỖI: không sinh được $OUT"; exit 1; }
