package com.chat.controller;

import com.chat.service.MovieIndexingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieIndexingController {

    @Autowired
    private MovieIndexingService movieIndexingService;

    @GetMapping("/reindex")
    public String reindexMovies() {
        movieIndexingService.reindexAllMovies();
        return "Переиндексация завершена!";
    }
}