#!/usr/bin/env bash
# build_phoneme.sh  -> builds, from the gitignored clone in tools/env/cache/oracles/phoneME (mirror https://github.com/magicus/phoneME, GPLv2:
# local oracle only, never committed, no source copied - D12/T05b), into tools/env/cache/phoneme/:
#   1. bin/cldc_vm       CLDC 1.1 reference VM (linux_i386, product build) + shim.jar (the project's own tiny MIDlet shim, tools/difftest/oracles/phoneme-shim)
#   2. midp-out/         headless MIDP 2.0 (pcsl + midp "linux_stubs_gcc" port + a CLDC rebuilt with ENABLE_PCSL) -> midp-out/bin/i386/runMidlet.
#                        MIDP failures are not fatal: run_phoneme.sh falls back to the bare CLDC + shim (CLDC-only cases).
#   Needs: gcc-multilib g++-multilib (apt), JDK 8 (/opt/jdk8) for the Java part of the build, perl, make. About 8 minutes in total.
# Notes on the modern-gcc build: the tree is compiled with -Werror, which gcc 13 trips over (maybe-uninitialized in 2008 code) ->
#   ENABLE_COMPILATION_WARNINGS=true; USER_CFLAGS=-w -fpermissive -std=gnu++98 -fno-strict-aliasing -fno-delete-null-pointer-checks.
#   Do NOT pass make -j: the loopgen/romgen sub-builds race on tools/buildtool.jar (about 3 min single threaded).
set -euo pipefail
. "$(dirname "$0")/lib.sh"; . "$ENV_DIR/versions.sh"
SRC="$CACHE/oracles/phoneME"; OUT="$CACHE/phoneme"
[ -d "$SRC/cldc/build/linux_i386" ] || { echo "missing $SRC (git clone --depth 1 https://github.com/magicus/phoneME $SRC)" >&2; exit 2; }
if ! dpkg -s gcc-multilib g++-multilib >/dev/null 2>&1; then echo "installing gcc-multilib g++-multilib"; apt-get install -y gcc-multilib g++-multilib >&2; fi
BIN="$SRC/cldc/build/linux_i386/dist/bin/cldc_vm"
if [ ! -x "$BIN" ]; then
  ( cd "$SRC/cldc/build/linux_i386" && export JVMWorkSpace="$SRC/cldc" JDK_DIR="$JDK8_HOME" &&
    env -u JAVA_TOOL_OPTIONS make ENABLE_COMPILATION_WARNINGS=true \
      USER_CFLAGS="-w -fpermissive -std=gnu++98 -fno-strict-aliasing -fno-delete-null-pointer-checks" ) > "$CACHE/phoneme-build.log" 2>&1 \
    || { tail -30 "$CACHE/phoneme-build.log" >&2; echo "phoneME CLDC build failed (log: $CACHE/phoneme-build.log)" >&2; exit 1; }
fi
mkdir -p "$OUT/bin"; cp "$BIN" "$OUT/bin/cldc_vm"
"$ENV_DIR/j2mec.sh" "$ROOT_DIR/tools/difftest/oracles/phoneme-shim" "$ROOT_DIR/tools/difftest/oracles/phoneme-shim/MANIFEST.MF" "$OUT/shim.jar" cldc11
build_midp() {
  local M="$SRC/cldc-midp" G="$OUT/gcc32"
  # a) 32-bit toolchain wrappers (the 2008 makefiles pass no -m32 to pcsl/midp, use a bare --end-group, -lnsl and a socket callback MIDP's stubs port lacks)
  mkdir -p "$G/bin" "$G/lib" "$OUT/hdr"; ( cd "$G/lib" && rm -f libnsl.a && ar rc libnsl.a )
  printf 'void NotifySocketStatusChanged(long handle, int waitingFor) { (void)handle; (void)waitingFor; }\n' > "$G/lib/stub.c"; gcc -m32 -c -o "$G/lib/stub.o" "$G/lib/stub.c"
  printf 'typedef struct { int code; const char *name; } Key;\n' > "$OUT/hdr/key.h"
  cat > "$G/bin/gcc" <<W
#!/bin/bash
exec /usr/bin/gcc -L$G/lib -m32 -w -fcommon -fpermissive "\$@"
W
  cat > "$G/bin/g++" <<W
#!/bin/bash
a=(); p=; c=0; for x in "\$@"; do [ "\$x" = -c ] && c=1; if [ "\$x" = --end-group ] && [ "\$p" != -Xlinker ]; then a+=("-Wl,--end-group"); else a+=("\$x"); fi; p="\$x"; done
[ \$c = 0 ] && a+=("$G/lib/stub.o")
exec /usr/bin/g++ -L$G/lib -m32 -w -fcommon -fpermissive "\${a[@]}"
W
  chmod +x "$G/bin/gcc" "$G/bin/g++"; ln -sf /usr/bin/ar "$G/bin/ar"
  # b) pcsl (32-bit, generic bsd sockets)
  [ -f "$OUT/pcsl-out/linux_i386/lib/libpcsl_memory.a" ] || ( cd "$SRC/pcsl" && JDK_DIR="$JDK8_HOME" env -u JAVA_TOOL_OPTIONS make PCSL_PLATFORM=linux_i386_gcc PCSL_OUTPUT_DIR="$OUT/pcsl-out" GNU_TOOLS_DIR="$G" NETWORK_MODULE=bsd/generic ) || return 1
  # c) a second CLDC tree built with ENABLE_PCSL (MIDP's Verify.gmk insists on it); the plain one above stays untouched
  if [ ! -x "$M/build/linux_i386/dist/bin/cldc_vm" ]; then
    rm -rf "$M"; mkdir -p "$M/build"; cp -a "$SRC/cldc/src" "$M/"; cp -a "$SRC/cldc/build/share" "$SRC/cldc/build/linux_i386" "$M/build/"
    rm -rf "$M/build/linux_i386"/{dist,loopgen,romgen,target,tools}
    ( cd "$M/build/linux_i386" && export JVMWorkSpace="$M" JDK_DIR="$JDK8_HOME" &&
      env -u JAVA_TOOL_OPTIONS make ENABLE_COMPILATION_WARNINGS=true ENABLE_PCSL=true PCSL_OUTPUT_DIR="$OUT/pcsl-out" \
        USER_CFLAGS="-w -fpermissive -std=gnu++98 -fno-strict-aliasing -fno-delete-null-pointer-checks" ) || return 1
  fi
  # d) the MIDP stubs port has no display device: lfjport_get_display_device_ids returns NULL and the first lcdui call crashes. Patch the
  #    LOCAL copy (cache, gitignored) to report one device; nothing is drawn anywhere.
  local F="$SRC/midp/src/highlevelui/lfjport/stubs/native/lfjport_st_export.c"
  grep -q 'static jint ids\[1\]' "$F" || python3 - "$F" <<'P'
import sys
p = sys.argv[1]; s = open(p).read()
s = s.replace("jint* lfjport_get_display_device_ids(jint* n) {\n    // Not implemented\n    return 0;", "jint* lfjport_get_display_device_ids(jint* n) {\n    static jint ids[1] = {1};\n    *n = 1;\n    return ids;")
open(p, "w").write(s)
P
  # e) MIDP itself (the Options.gmk of the stubs port lacks three boolean options that Verify.gmk checks)
  ( cd "$SRC/midp/build/linux_stubs_gcc" && env -u JAVA_TOOL_OPTIONS EXTRA_CFLAGS="-include $OUT/hdr/key.h" JDK_DIR="$JDK8_HOME" make \
      USE_BINARY_CRYPTO=false USE_GCC=true USE_JSR_293=false MIDP_OUTPUT_DIR="$OUT/midp-out" PCSL_OUTPUT_DIR="$OUT/pcsl-out" \
      CLDC_DIST_DIR="$M/build/linux_i386/dist" JVMWorkSpace="$M" GNU_TOOLS_DIR="$G" TOOLS_DIR="$SRC/tools" ) || return 1
  [ -x "$OUT/midp-out/bin/i386/runMidlet" ]
}
if [ ! -x "$OUT/midp-out/bin/i386/runMidlet" ]; then
  build_midp > "$CACHE/phoneme-midp-build.log" 2>&1 && echo "phoneME MIDP ready: $OUT/midp-out/bin/i386/runMidlet" \
    || echo "WARNING: phoneME MIDP build failed (log: $CACHE/phoneme-midp-build.log); only the CLDC + shim oracle is available" >&2
fi
echo "phoneME CLDC ready: $OUT/bin/cldc_vm ($("$OUT/bin/cldc_vm" -version 2>&1 | head -1))"
