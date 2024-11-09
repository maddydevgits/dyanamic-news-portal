package com.newsportal.newsportal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newsportal.newsportal.model.JobApplication;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByStatus(String status);
    Boolean existsByEmail(String email);
    Optional<JobApplication> findByEmail(String email);
    void deleteByEmail(String email);

}

