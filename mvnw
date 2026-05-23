#!/bin/sh
set -e

BASEDIR="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"
WRAPPER_PROPS="$BASEDIR/.mvn/wrapper/maven-wrapper.properties"

if [ ! -f "$WRAPPER_PROPS" ]; then
  echo "Maven Wrapper properties not found: $WRAPPER_PROPS" >&2
  exit 1
fi

DIST_URL="$(sed -n 's/^distributionUrl=//p' "$WRAPPER_PROPS" | head -n 1)"
if [ -z "$DIST_URL" ]; then
  echo "distributionUrl not found in $WRAPPER_PROPS" >&2
  exit 1
fi

DIST_FILE="${DIST_URL##*/}"
DIST_NAME="${DIST_FILE%-bin.zip}"
WRAPPER_DIR="${HOME}/.m2/wrapper/dists/${DIST_NAME}"
MAVEN_HOME="${WRAPPER_DIR}/${DIST_NAME}"

if [ ! -x "$MAVEN_HOME/bin/mvn" ]; then
  mkdir -p "$WRAPPER_DIR"
  echo "Downloading $DIST_URL"
  if command -v curl >/dev/null 2>&1; then
    curl -fsSL "$DIST_URL" -o "$WRAPPER_DIR/$DIST_FILE"
  elif command -v wget >/dev/null 2>&1; then
    wget -q "$DIST_URL" -O "$WRAPPER_DIR/$DIST_FILE"
  else
    echo "curl or wget is required to download Maven" >&2
    exit 1
  fi
  unzip -q -o "$WRAPPER_DIR/$DIST_FILE" -d "$WRAPPER_DIR"
fi

exec "$MAVEN_HOME/bin/mvn" "$@"
