#!/bin/bash
# run_audio_test.sh — kiểm thử bộ phát SFX (độ trễ, chồng tiếng, không cắt, bể Player, hạ nhạc) bằng MMAPI giả trên JVM thường.
# Chạy từ SVTH_refactor/. Không cần emulator/thiết bị âm thanh.
set -e
unset JAVA_TOOL_OPTIONS
OUT=${1:-build/audiotest}
rm -rf $OUT && mkdir -p $OUT
javac -nowarn -encoding UTF-8 -d $OUT tools/test/fake/*.java tools/test/fake/javax/microedition/media/*.java tools/test/fake/javax/microedition/media/control/*.java \
  src/SFXManager.java src/BeepBackend.java src/SFXType.java src/SfxLog.java src/BuildFlags.java src/BgmDuck.java tools/test/SfxAudioTest.java
java -Dstdout.encoding=UTF-8 -cp $OUT SfxAudioTest || true
java -Dstdout.encoding=UTF-8 -cp $OUT SfxAudioTest cap
java -Dstdout.encoding=UTF-8 -cp $OUT SfxAudioTest total
# session 6: thiết bị có độ trễ (Player còn STARTED tới start + L + độ dài) + createPlayer chậm 60 ms, dùng WAV thật trong res/
for L in 0 150 300; do java -Dstdout.encoding=UTF-8 -cp $OUT:res SfxAudioTest lat$L; done
# session 7: mô phỏng J2ME Loader (MicroPlayer: start() mới prepare, phát hết → reset, stop = pause) + kịch bản trận có sủng vật
java -Dstdout.encoding=UTF-8 -cp $OUT:res SfxAudioTest j2mel
