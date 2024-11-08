package com.newsportal.newsportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newsportal.newsportal.model.JobApplication;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByStatus(String status);
}

