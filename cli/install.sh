#!/usr/bin/env bash
set -euo pipefail

CLI_DIR="$(cd "$(dirname "$0")" && pwd)"

pip install -r "$CLI_DIR/requirements.txt"

mkdir -p "$HOME/.local/bin"
cp "$CLI_DIR/debuglog.py" "$HOME/.local/bin/debuglog"
cp "$CLI_DIR/formatter.py" "$HOME/.local/bin/formatter.py"
chmod +x "$HOME/.local/bin/debuglog"

echo "debuglog installed to $HOME/.local/bin/debuglog"
echo ""
echo "If 'debuglog' is not found, add this to your ~/.bashrc or ~/.zshrc:"
echo '  export PATH="$HOME/.local/bin:$PATH"'