#!/usr/bin/env bash
# Boots a real Forge dedicated server with the full pack and the custom mods,
# then writes a report to reports/server-test.txt. This is the "do these mods
# actually load together?" check. Run from CI (needs network).
set -u
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
SERVER="$ROOT/server-test"
REPORT="$ROOT/reports/server-test.txt"
MC=1.20.1
FORGE=47.2.20
mkdir -p "$SERVER" "$ROOT/reports"
: > "$REPORT"
rep() { echo "$@" | tee -a "$REPORT"; }
fail() { rep "RESULT: FAIL ($1)"; exit 1; }

rep "server test started: $(date -u +%FT%TZ)"
cd "$SERVER"

curl -fsSL -o installer.jar \
  "https://maven.minecraftforge.net/net/minecraftforge/forge/$MC-$FORGE/forge-$MC-$FORGE-installer.jar" \
  || fail "could not download the Forge installer"
java -jar installer.jar --installServer > install.log 2>&1 \
  || { tail -30 install.log >> "$REPORT"; fail "Forge server install"; }
echo "eula=true" > eula.txt
echo "-Xmx5G" > user_jvm_args.txt

curl -fsSL -o bootstrap.jar \
  https://github.com/packwiz/packwiz-installer-bootstrap/releases/latest/download/packwiz-installer-bootstrap.jar \
  || fail "could not download packwiz-installer"
( cd "$ROOT/pack" && nohup packwiz serve > "$SERVER/serve.log" 2>&1 & )
sleep 8
java -jar bootstrap.jar -g -s server http://localhost:8080/pack.toml > packwiz-install.log 2>&1 \
  || { tail -30 packwiz-install.log >> "$REPORT"; fail "packwiz-installer"; }
pkill -f "packwiz serve" || true

mkdir -p mods
for jar in "$ROOT"/mods/*/build/libs/*.jar; do
  [ -f "$jar" ] || continue
  case "$jar" in *-sources.jar|*-dev.jar) continue;; esac
  cp "$jar" mods/
  rep "added custom mod: $(basename "$jar")"
done
rep "jars in server mods folder: $(ls mods/*.jar 2>/dev/null | wc -l)"

mkfifo input
java @user_jvm_args.txt "@libs/net/minecraftforge/forge/$MC-$FORGE/unix_args.txt" nogui \
  < input > server.log 2>&1 &
SPID=$!
exec 3> input

OK=0
for _ in $(seq 1 150); do
  sleep 5
  if grep -q 'Done (' server.log; then OK=1; break; fi
  if ! kill -0 "$SPID" 2>/dev/null; then break; fi
done

if [ $OK -eq 1 ]; then
  echo "stop" >&3
  sleep 20
fi
kill "$SPID" 2>/dev/null || true

rep ""
if [ $OK -eq 1 ]; then
  rep "RESULT: PASS (server reached 'Done')"
else
  rep "RESULT: FAIL (server did not finish starting)"
fi
rep "--- startup time line ---"
grep 'Done (' server.log | tail -1 | tee -a "$REPORT"
rep "--- errors and warnings worth reading (first 80) ---"
grep -E "ERROR|FATAL|Exception|Mixin apply|Failed to|Missing or unsupported|requires " server.log | head -80 >> "$REPORT"
for crash in crash-reports/*.txt; do
  [ -f "$crash" ] || continue
  rep "--- $(basename "$crash") (first 60 lines) ---"
  head -60 "$crash" >> "$REPORT"
done
rep "--- last 40 log lines ---"
tail -40 server.log >> "$REPORT"
[ $OK -eq 1 ]
