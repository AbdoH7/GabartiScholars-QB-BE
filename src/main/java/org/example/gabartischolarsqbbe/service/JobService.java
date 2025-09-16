package org.example.gabartischolarsqbbe.service;

import org.example.gabartischolarsqbbe.dto.JobRequest;
import org.example.gabartischolarsqbbe.dto.JobResponse;
import org.example.gabartischolarsqbbe.entity.Job;
import org.example.gabartischolarsqbbe.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class JobService {
    
    private final JobRepository jobRepository;
    
    @Autowired
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }
    
    /**
     * Create a new job
     * @param jobRequest the job request containing name and code
     * @return JobResponse with the created job details
     * @throws IllegalArgumentException if job code already exists
     */
    public JobResponse createJob(JobRequest jobRequest) {
        // Check if job code already exists
        if (jobRepository.existsByCode(jobRequest.getCode())) {
            throw new IllegalArgumentException("Job with code '" + jobRequest.getCode() + "' already exists");
        }
        
        // Create new job entity
        Job job = new Job(jobRequest.getName(), jobRequest.getCode());
        
        // Save the job
        Job savedJob = jobRepository.save(job);
        
        // Convert to response DTO
        return convertToJobResponse(savedJob);
    }
    
    /**
     * Get all jobs
     * @return List of JobResponse
     */
    @Transactional(readOnly = true)
    public List<JobResponse> getAllJobs() {
        return jobRepository.findAll()
                .stream()
                .map(this::convertToJobResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get job by ID
     * @param id the job ID
     * @return Optional containing JobResponse if found
     */
    @Transactional(readOnly = true)
    public Optional<JobResponse> getJobById(Long id) {
        return jobRepository.findById(id)
                .map(this::convertToJobResponse);
    }
    
    /**
     * Get job by code
     * @param code the job code
     * @return Optional containing JobResponse if found
     */
    @Transactional(readOnly = true)
    public Optional<JobResponse> getJobByCode(String code) {
        return jobRepository.findByCode(code)
                .map(this::convertToJobResponse);
    }
    
    /**
     * Check if job exists by ID
     * @param id the job ID
     * @return true if job exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean jobExists(Long id) {
        return jobRepository.existsById(id);
    }
    
    /**
     * Check if job exists by code
     * @param code the job code
     * @return true if job exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean jobExistsByCode(String code) {
        return jobRepository.findByCode(code).isPresent();
    }
    
    /**
     * Convert Job entity to JobResponse DTO
     * @param job the job entity
     * @return JobResponse DTO
     */
    private JobResponse convertToJobResponse(Job job) {
        return new JobResponse(job.getId(), job.getName(), job.getCode());
    }
}