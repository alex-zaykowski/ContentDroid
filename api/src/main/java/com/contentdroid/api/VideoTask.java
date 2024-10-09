package com.contentdroid.api;

import com.contentdroid.api.models.VideoGenerationData;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.json.JSONObject;
import org.springframework.boot.jackson.JsonObjectSerializer;

public class VideoTask {
    private static final String TASK_QUEUE_NAME = "videos_task_queue";

    public static void CreateVideoTask(String backgroundVideoPath, String dialogue) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            boolean durable = true;
            channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);

            JSONObject json = new JSONObject();
            json.put("dialogue", dialogue);
            json.put("backgroundVideoPath", backgroundVideoPath);

            channel.basicPublish("", TASK_QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    json.toString().getBytes());
            System.out.println(" [x] Sent Video to " + TASK_QUEUE_NAME);
        }
    }
}
