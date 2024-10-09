package com.contentdroid.api.models;

import org.springframework.web.multipart.MultipartFile;

public class VideoGenerationData {
	public String creator;
	
	public String voice;
	
	public String dialogue;

	public String date;

	public MultipartFile backgroundVideo;

	public VideoGenerationData(String creator, String voice, String dialogue, String date, MultipartFile backgroundVideo){
		this.creator = creator;
		this.voice = voice;
		this.dialogue = dialogue;
		this.date = date;
		this.backgroundVideo = backgroundVideo;
	}
}
