#!/bin/bash
python3 -I "$(dirname "$0")/apply_text.py" "$1" "$(dirname "$0")/../text_can_dich.tsv"
