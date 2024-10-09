package com.contentdroid.api.databases.repositories;

import com.contentdroid.api.databases.dtos.Video;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VideosRepository extends CrudRepository<Video, Integer> {
    List<Video> findPathByCreator(String creator);
    Video findVideoById(int id);
}