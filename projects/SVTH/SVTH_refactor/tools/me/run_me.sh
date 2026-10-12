#!/bin/bash
# run_me.sh <jar> <script> <outdir> [timeout s] — chạy MeRun dưới Xvfb + openbox. stdout → <outdir>/out.txt (có các dòng MERUN, SFXLOG của game)
JAR=$(readlink -f "$1"); SCRIPT=$(readlink -f "$2"); OUT=$3; SECS=${4:-120}
HERE=$(readlink -f $(dirname "$0")); L=$(readlink -f $HERE/../lib)
mkdir -p "$OUT"; OUT=$(readlink -f "$OUT")
CP=$HERE:$HERE/dev240.jar:$L/microemulator-2.0.4.jar:$L/microemu-cldc-2.0.4.jar:$L/microemu-midp-2.0.4.jar:$L/midpapi20-2.0.4.jar:$L/cldcapi10-2.0.4.jar
unset JAVA_TOOL_OPTIONS
[ -f $HERE/MeRun.class ] && [ $HERE/MeRun.class -nt $HERE/MeRun.java ] || javac -nowarn -cp $CP -d $HERE $HERE/MeRun.java 2>&1 | head -20
timeout $SECS xvfb-run -a -s "-screen 0 480x700x24" bash -c "openbox >/dev/null 2>&1 & sleep 1; cd $OUT; java $JOPTS -cp $CP MeRun $JAR $SCRIPT $OUT > $OUT/out.txt 2>&1"
echo "exit $? (out: $OUT/out.txt)"
