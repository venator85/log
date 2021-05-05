#!/bin/bash
set -euxo pipefail

./gradlew assemble :log:apiDump

mkdir -p log-noop/api/
cp log/api/log.api log-noop/api/log-noop.api

./gradlew :log-noop:apiCheck

./gradlew publish

/Users/venator/git/venator85-maven/generator/maven-generator-1.0-SNAPSHOT-all
