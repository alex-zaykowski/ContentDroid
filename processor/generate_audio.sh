#!/bin/bash

FILE_NAME=$1
SESSION_ID=$2
VOICE=$3
VOICEOVER_NAME=$4

python3 tts.py -v "$VOICE" -f "$FILE_NAME" --session "$SESSION_ID" --name "$VOICEOVER_NAME"
mv ./$VOICEOVER_NAME ./output/$VOICEOVER_NAME
