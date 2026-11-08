#!/usr/bin/env bash
set -euo pipefail

repo_root="$(cd "$(dirname "$0")/.." && pwd)"
cd "$repo_root"

set +e
./mvnw verify -Dgroups=Layer1
test_exit=$?
set -e

if [ "$test_exit" -eq 0 ]; then
  echo
  echo "✅ Layer 1 integration tests PASSED"
else
  echo
  echo "❌ Layer 1 integration tests FAILED"
fi
exit "$test_exit"
