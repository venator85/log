#!/bin/bash
set -euxo pipefail

./gradlew assemble :log:apiDump

mkdir -p log-noop/api/
cp log/api/log.api log-noop/api/log-noop.api

./gradlew :log-noop:apiCheck

./gradlew publishToMavenLocal
