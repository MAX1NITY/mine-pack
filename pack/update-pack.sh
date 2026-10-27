#!/usr/bin/env bash
# Resolves pack/mods.txt into packwiz metadata. Safe to re-run: already-resolved
# slugs are skipped, failures are retried. Writes a readable report next to it.
set -u
cd "$(dirname "$0")"

REPORT=last-run.txt
: > "$REPORT"
touch resolved.txt
: > failed.txt

say() { echo "$@" | tee -a "$REPORT"; }

say "packwiz: $(packwiz --version 2>&1 | head -1)"
say "started: $(date -u +%FT%TZ)"

while IFS= read -r raw; do
  slug="${raw%%#*}"
  slug="$(echo "$slug" | xargs)"
  [ -z "$slug" ] && continue

  if grep -qx "$slug" resolved.txt; then
    say "skip    $slug (already resolved)"
    continue
  fi

  out="$(packwiz modrinth add "$slug" -y < /dev/null 2>&1)"
  code=$?
  if [ $code -eq 0 ]; then
    echo "$slug" >> resolved.txt
    say "ok      $slug"
  else
    echo "$slug" >> failed.txt
    say "FAILED  $slug (exit $code)"
    echo "$out" | tail -5 | sed 's/^/        /' | tee -a "$REPORT"
  fi
done < mods.txt

packwiz refresh 2>&1 | tee -a "$REPORT"
say "mods in pack: $(ls mods/*.pw.toml 2>/dev/null | wc -l)"
say "failed: $(wc -l < failed.txt)"
