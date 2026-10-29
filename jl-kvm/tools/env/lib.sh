#!/usr/bin/env bash
# Common helpers for tools/env. Source this file.
ENV_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$ENV_DIR/../.." && pwd)"
CACHE="${JLKVM_CACHE:-$ENV_DIR/cache}"
JDK8_HOME="${JDK8_HOME:-/opt/jdk8}"
MAVEN_BASE="${MAVEN_BASE:-https://repo.maven.apache.org/maven2}"
mkdir -p "$CACHE"

# fetch URL DEST : download with retry/backoff (Maven Central may answer 429)
fetch() {
  local url="$1" dest="$2" i code delay=3
  [ -s "$dest" ] && return 0
  mkdir -p "$(dirname "$dest")"
  for i in 1 2 3 4 5 6 7 8; do
    code=$(curl -sSL -w '%{http_code}' -o "$dest.part" --max-time 600 "$url" 2>/dev/null) || code=000
    if [ "$code" = 200 ]; then mv "$dest.part" "$dest"; return 0; fi
    rm -f "$dest.part"
    [ "$code" = 404 ] && { echo "fetch: 404 $url" >&2; return 1; }
    sleep "$delay"; delay=$((delay * 2)); [ $delay -gt 60 ] && delay=60
  done
  echo "fetch: failed ($code) $url" >&2
  return 1
}

# mvn_fetch group artifact version [classifier] -> prints local path
mvn_fetch() {
  local g="$1" a="$2" v="$3" c="${4:+-$4}"
  local dest="$CACHE/maven/$a-$v$c.jar"
  fetch "$MAVEN_BASE/${g//.//}/$a/$v/$a-$v$c.jar" "$dest" && echo "$dest"
}

jar_path() { # name -> path of tool jar in cache (set by setup_toolchain.sh)
  echo "$CACHE/$1"
}
