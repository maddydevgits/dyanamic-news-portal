package com.newsportal.newsportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.newsportal.newsportal.model.News;

public interface NewsRepository extends JpaRepository<News, Long> {

    // Spring Data JPA will generate basic CRUD methods automatically,
    // such as save(), findById(), findAll(), and deleteById().
    
    // Additional custom query methods (optional)

    // Example: Find news by title (optional)
    List<News> findByTitle(String title);
}
