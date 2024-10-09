package com.contentdroid.api.services;

import com.contentdroid.api.databases.dtos.Video;
import com.contentdroid.api.databases.repositories.VideosRepository;
import com.contentdroid.api.models.VideoGenerationData;
import com.contentdroid.api.models.exceptions.BadRequestException;
import com.contentdroid.api.models.exceptions.MessageQueueFailureException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class VideosService {
    private final VideosRepository videosRepository;
    private static final String TASK_QUEUE_NAME = "videos_task_queue";

    public VideosService(VideosRepository videosRepository){
        this.videosRepository = videosRepository;
    }
    public void uploadVideo(VideoGenerationData videoGenerationData){
        String backgroundVideoFilePath =
                "/Users/alex/Dev/ContentDroid/videos/" + videoGenerationData.backgroundVideo.getOriginalFilename();
        File videoFile = new File(backgroundVideoFilePath);
        try (FileOutputStream outputStream = new FileOutputStream(videoFile)) {
            outputStream.write(videoGenerationData.backgroundVideo.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException();
        }
        Timestamp date = Timestamp.valueOf(videoGenerationData.date);

        Video video = new Video(
                backgroundVideoFilePath,
                videoGenerationData.voice,
                videoGenerationData.dialogue,
                videoGenerationData.creator,
                videoGenerationData.backgroundVideo.getOriginalFilename(),
                date
        );

        videosRepository.save(video);
        queueVideo(video.getId());
    }

    public List<Video> getVideos(String creator) {
        return videosRepository.findPathByCreator(creator);
    }

    public Video getVideoById(int id) {
        return videosRepository.findVideoById(id);
    }

    private void queueVideo(Integer videoId) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            boolean durable = true;
            channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);
            channel.basicPublish("", TASK_QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    videoId.toString().getBytes());

            System.out.println(" [x] Sent Video to " + TASK_QUEUE_NAME);

        } catch (TimeoutException e){
            e.printStackTrace();
            throw new MessageQueueFailureException();
        } catch (IOException e) {
            e.printStackTrace();
            throw new MessageQueueFailureException();
        }
    }
}
