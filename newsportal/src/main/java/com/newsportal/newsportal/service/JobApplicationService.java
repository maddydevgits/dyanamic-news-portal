package com.newsportal.newsportal.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newsportal.newsportal.model.JobApplication;
import com.newsportal.newsportal.repository.JobApplicationRepository;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    public List<JobApplication> getAllApplications() {
        return jobApplicationRepository.findAll(); // Fetch all applications from DB
    }

    public JobApplication saveApplication(JobApplication jobApplication) {
        jobApplication.setStatus("Pending"); // Default status is Pending
        return jobApplicationRepository.save(jobApplication);
    }

    public List<JobApplication> getPendingApplications() {
        return jobApplicationRepository.findByStatus("Pending");
    }

    public JobApplication updateApplicationStatus(Long id, String status) {
        Optional<JobApplication> applicationOpt = jobApplicationRepository.findById(id);
        if (applicationOpt.isPresent()) {
            JobApplication application = applicationOpt.get();
            application.setStatus(status);
            return jobApplicationRepository.save(application);
        }
        return null;
    }
    
}

