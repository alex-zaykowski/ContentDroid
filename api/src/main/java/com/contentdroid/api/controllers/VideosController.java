package com.contentdroid.api.controllers;

import com.contentdroid.api.databases.repositories.VideosRepository;
import com.contentdroid.api.databases.dtos.Video;
import com.contentdroid.api.models.VideoGenerationData;
import com.contentdroid.api.services.VideosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.contentdroid.api.VideoTask.CreateVideoTask;

@RestController
public class VideosController {
    private final VideosService videosService;
    private final VideosRepository videosRepository;

    public VideosController(VideosRepository videosRepository) {
        this.videosRepository = videosRepository;
        this.videosService = new VideosService(this.videosRepository);
    }

    @PostMapping(path = "/generate", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public void generateVideo(@RequestParam String dialogue, @RequestParam String voice, @RequestParam String username, @RequestParam String date, MultipartFile backgroundVideo) {
        VideoGenerationData videoGenerationData = new VideoGenerationData(username, voice, dialogue, date, backgroundVideo);
        videosService.uploadVideo(videoGenerationData);
    }

    @GetMapping(path = "/{username}/videos")
    public Iterable<Video> getData(@PathVariable String username) {
        return videosService.getVideos(username);
    }

    @GetMapping(path = "/videos/{id}")
    public boolean getVideo(@PathVariable int id) {
        return videosService.getVideoById(id).isCompleted();
    }
}
