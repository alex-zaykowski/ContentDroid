#!/bin/bash
cd output 

VOICEOVER_AUDIO_NAME=$1
LANGUAGE=$2
BACKGROUND_VIDEO_NAME=$3

# generate srt file
whisper "$VOICEOVER_AUDIO_NAME" --model "medium" --language "$LANGUAGE" --output_format "srt" --word_timestamps "True" --max_words_per_line 1

echo "GENERATING VIDEO..." 

# add audio to video
ffmpeg \
    -i $BACKGROUND_VIDEO_NAME -i $VOICEOVER_AUDIO_NAME\
    -c:v copy \
    -map 0:v -map 1:a \
    -y output.mp4 

# add subtitles to video
ffmpeg -i output.mp4 -vf "subtitles=audio.srt:force_style='Fontname=Komika Axis,Alignment=10,PrimaryColour=&H03fcff,Fontsize=35'" $BACKGROUND_VIDEO_NAME

rm $VOICEOVER_AUDIO_NAME
