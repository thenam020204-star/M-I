#!/bin/bash
# run_micro.sh <jar> <keys-script> <outdir> [runtime-seconds] [extra java -D args...]
# Chạy MicroEmulator 2.0.4 dưới Xvfb, tiêm phím bằng Robot, lưu stdout game vào <outdir>/out.txt và ảnh chụp theo kịch bản.
JAR=$(readlink -f "$1"); SCRIPT=$(readlink -f "$2"); OUT=$3; SECS=${4:-60}; shift 4 2>/dev/null
L=$(dirname "$0")/../lib; L=$(readlink -f $L); mkdir -p "$OUT"; OUT=$(readlink -f "$OUT")
HERE=$(readlink -f $(dirname "$0"))
[ -f $HERE/Keys.class ] || javac -d $HERE $HERE/Keys.java
unset JAVA_TOOL_OPTIONS
xvfb-run -a -s "-screen 0 480x700x24" bash -c "
  cd $OUT
  openbox >/dev/null 2>&1 &
  sleep 1
  java $* -cp $HERE/dev240.jar:$L/microemulator-2.0.4.jar:$L/microemu-cldc-2.0.4.jar:$L/microemu-midp-2.0.4.jar org.microemu.app.Main --device dev240/device.xml --appclasspath $JAR Main > $OUT/out.txt 2>&1 &
  MP=\$!
  sleep 8
  java -cp $HERE Keys $SCRIPT > $OUT/keys.txt 2>&1
  sleep 1; kill \$MP 2>/dev/null
" &
W=$!
( sleep $SECS; kill $W 2>/dev/null ) &
wait $W 2>/dev/null
echo "done $OUT"
