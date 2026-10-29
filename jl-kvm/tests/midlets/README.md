# tests/midlets

API test MIDlets. `framework/` holds `jlkvm.test.TestMIDlet` (PASS/FAIL protocol, `> ` prefixed lines).
`selftest/` checks the framework itself. `hello/` is the toolchain/oracle smoke test (T02/T05).

    tests/midlets/build.sh tests/midlets/selftest out/selftest.jar
    tools/difftest/oracles/run_all.sh out/selftest.jar

New API test MIDlet: create `tests/midlets/<name>/{src,MANIFEST.MF}` extending `TestMIDlet`; build with `build.sh`.
