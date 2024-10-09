package com.contentdroid.api.databases.dtos;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "videos")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String path;
    private String voice;
    private String dialogue;
    private String creator;
    private String name;
    private Timestamp date;
    private boolean completed = false;

    private Video() {}

    public Video(String path, String voice, String dialogue, String creator, String name, Timestamp date) {
        this.path = path;
        this.voice = voice;
        this.dialogue = dialogue;
        this.creator = creator;
        this.name = name;
        this.date = date;
    }

    public String getPath() {
        return this.path;
    }

    public String getVoice() {
        return this.voice;
    }

    public String getDialogue() {
        return this.dialogue;
    }

    public String getCreator() {
        return this.creator;
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Timestamp getDate(){
        return this.date;
    }

    public boolean isCompleted() {
        return this.completed;
    }
}
