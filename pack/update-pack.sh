#!/usr/bin/env bash
# Resolves pack/mods.txt into packwiz metadata. Safe to re-run: already-resolved
# lines are skipped, failures are retried. Writes a readable report next to it.
# A line may list alternatives separated by '|'; the first slug that resolves wins.
set -u
cd "$(dirname "$0")"

REPORT=last-run.txt
: > "$REPORT"
touch resolved.txt
: > failed.txt

say() { echo "$@" | tee -a "$REPORT"; }

say "started: $(date -u +%FT%TZ)"

while IFS= read -r raw; do
  line="${raw%%#*}"
  line="$(echo "$line" | xargs)"
  [ -z "$line" ] && continue

  if grep -qxF "$line" resolved.txt; then
    say "skip    $line (already resolved)"
    continue
  fi

  IFS='|' read -ra candidates <<< "$line"
  added=0
  for slug in "${candidates[@]}"; do
    slug="$(echo "$slug" | xargs)"
    out="$(packwiz modrinth add "$slug" -y < /dev/null 2>&1)"
    code=$?
    if [ $code -eq 0 ]; then
      echo "$line" >> resolved.txt
      say "ok      $slug"
      added=1
      break
    else
      say "miss    $slug: $(echo "$out" | grep -E 'Failed|no ' | head -1)"
    fi
  done
  if [ $added -eq 0 ]; then
    echo "$line" >> failed.txt
    say "FAILED  $line"
  fi
done < mods.txt

packwiz refresh 2>&1 | tee -a "$REPORT"
say "mods in pack: $(ls mods/*.pw.toml 2>/dev/null | wc -l)"
say "failed lines: $(wc -l < failed.txt)"
