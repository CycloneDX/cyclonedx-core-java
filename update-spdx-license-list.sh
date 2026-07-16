#!/usr/bin/env bash

set -euxo pipefail

SCRIPT_DIR="$(cd -P -- "$(dirname "$0")" && pwd -P)"
RESOURCES_DIR="${SCRIPT_DIR}/src/main/resources"
LICENSES_DIR="${RESOURCES_DIR}/licenses"
TMP="$(mktemp)"
trap 'rm -f "${TMP}"' EXIT

gh -R spdx/license-list-data release download "v$1" --archive tar.gz --clobber --output "${TMP}"

rm -rf "${LICENSES_DIR}" && mkdir -p "${LICENSES_DIR}"
tar -xzf "${TMP}" --strip-components 2 -C "${LICENSES_DIR}" "license-list-data-$1/text"
tar -xzf "${TMP}" --strip-components 2 -C "${LICENSES_DIR}" "license-list-data-$1/json/licenses.json"

curl -fsSL https://cyclonedx.org/schema/spdx.schema.json | jq '.' - > "${RESOURCES_DIR}/spdx.schema.json"
curl -fsSL https://cyclonedx.org/schema/spdx.xsd -o "${RESOURCES_DIR}/spdx.xsd"
jq -e --arg v "$1" '.["$comment"] | endswith("-" + $v)' "${RESOURCES_DIR}/spdx.schema.json" >/dev/null \
  || echo "WARN: spdx.schema.json \$comment ($(jq -r '.["$comment"]' "${RESOURCES_DIR}/spdx.schema.json")) != $1 (cyclonedx.org not updated yet)"