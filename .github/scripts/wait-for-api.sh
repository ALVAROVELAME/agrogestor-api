#!/usr/bin/env bash
set -euo pipefail

# A VM limita a API a 0,5 CPU: o primeiro boot pode levar vários minutos.
timeout_seconds="${API_READINESS_TIMEOUT:-300}"
url="${API_READINESS_URL:-http://127.0.0.1:3002/}"
container="${API_CONTAINER_NAME:-agrogestor_api}"
deadline=$((SECONDS + timeout_seconds))
attempt=0

echo "Aguardando a API responder por até ${timeout_seconds} segundos..."
while (( SECONDS < deadline )); do
  attempt=$((attempt + 1))
  if curl --silent --fail --connect-timeout 2 --max-time 3 "$url" > /dev/null; then
    echo "API pronta na tentativa $attempt, após ${SECONDS} segundos."
    exit 0
  fi

  if ! state=$(docker inspect --format '{{.State.Status}} {{.RestartCount}} {{.State.OOMKilled}}' "$container" 2>/dev/null); then
    echo "Erro: container $container não foi encontrado."
    exit 1
  fi
  read -r status restarts oom <<< "$state"
  if [[ "$status" == "exited" || "$status" == "dead" || "$oom" == "true" ]] || (( restarts >= 3 )); then
    echo "Erro: container não iniciou (estado: $status; reinícios: $restarts; falta de memória: $oom)."
    docker logs --tail 150 "$container" || true
    exit 1
  fi

  echo "Tentativa $attempt: API ainda não está pronta (estado: $status; ${SECONDS}s)."
  sleep 3
done

echo "Erro: a API não respondeu dentro de ${timeout_seconds} segundos."
docker logs --tail 150 "$container" || true
exit 1
