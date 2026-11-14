#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
ROLES_DIR="$SCRIPT_DIR/roles"
ROLE_NAME="labtrack_app"
ROLE_PATH="$ROLES_DIR/$ROLE_NAME"

TMP_DIR="$(mktemp -d)"
trap 'rm -rf "$TMP_DIR"' EXIT

mkdir -p "$ROLES_DIR"
ansible-galaxy init --init-path="$TMP_DIR" "$ROLE_NAME"

if [ -d "$ROLE_PATH" ]; then
  cp -rn "$TMP_DIR/$ROLE_NAME/." "$ROLE_PATH/"
  echo "Role '$ROLE_NAME' already exists at $ROLE_PATH."
  echo "Merged any missing standard skeleton directories/files without overwriting existing files."
else
  cp -r "$TMP_DIR/$ROLE_NAME" "$ROLES_DIR/"
  echo "Role skeleton initialized at $ROLE_PATH."
fi
