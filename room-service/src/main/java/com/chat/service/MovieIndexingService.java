package com.chat.service;

import com.chat.model.Movie;
import com.chat.model.MovieDoc;
import com.chat.repository.MovieElasticsearchRepository;
import com.chat.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovieIndexingService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieElasticsearchRepository movieElasticsearchRepository;

    @Transactional(readOnly = true)
    public void reindexAllMovies() {
        List<Movie> movies = movieRepository.findAll();

        movieElasticsearchRepository.saveAll(
                movies.stream().map(
                        movie -> new MovieDoc(
                                movie.getId(),
                                movie.getMovie(),
                                movie.getOverview()
                        )
                ).toList()
        );
    }
}