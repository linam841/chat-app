package com.chat.repository;

import com.chat.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("""
                SELECT m FROM Movie m
                WHERE LOWER(m.movie) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(m.overview) LIKE LOWER(CONCAT('%', :query, '%'))
                ORDER BY m.ratingBall DESC
            """)
    Page<Movie> searchByQuery(@Param("query") String query, Pageable pageable);
}