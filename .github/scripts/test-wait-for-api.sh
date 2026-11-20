#!/usr/bin/env bash
set -euo pipefail

script="$(dirname "$0")/wait-for-api.sh"

curl() { [[ "${SCENARIO}" == "ready" ]]; }
docker() {
  if [[ "$1" == "logs" ]]; then return 0; fi
  case "$SCENARIO" in
    absent) return 1 ;;
    exited) echo "exited 0 false" ;;
    restarting) echo "restarting 3 false" ;;
    oom) echo "restarting 1 true" ;;
    *) echo "running 0 false" ;;
  esac
}
export -f curl docker

output=$(SCENARIO=ready bash "$script")
[[ "$output" == *"API pronta"* ]]

for scenario in absent exited restarting oom; do
  if output=$(SCENARIO="$scenario" bash "$script"); then
    echo "Falhou: $scenario deveria retornar erro."
    exit 1
  fi
  [[ "$output" == *"Erro:"* ]]
done

if output=$(SCENARIO=timeout API_READINESS_TIMEOUT=0 bash "$script"); then
  echo "Falhou: timeout deveria retornar erro."
  exit 1
fi
[[ "$output" == *"não respondeu"* ]]
echo "Verificações do script de espera passaram."
