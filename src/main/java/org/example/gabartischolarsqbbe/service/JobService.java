package org.example.gabartischolarsqbbe.service;

import org.example.gabartischolarsqbbe.dto.CreateJobRequest;
import org.example.gabartischolarsqbbe.dto.UpdateJobRequest;
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
     * @param createJobRequest the job request containing name, code, and optional fields
     * @return JobResponse with the created job details
     * @throws IllegalArgumentException if job code already exists
     */
    public JobResponse createJob(CreateJobRequest createJobRequest) {
        // Check if job code already exists
        if (jobRepository.existsByCode(createJobRequest.getCode())) {
            throw new IllegalArgumentException("Job with code '" + createJobRequest.getCode() + "' already exists");
        }
        
        // Create new job entity
        Job job = new Job(createJobRequest.getName(), createJobRequest.getCode());
        
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
        return jobRepository.existsByCode(code);
    }
    
    /**
     * Update an existing job by code
     * @param code the job code to update
     * @param updateJobRequest the update request containing fields to update
     * @return JobResponse with the updated job details
     * @throws IllegalArgumentException if job code does not exist or no updates provided
     */
    @Transactional
    public JobResponse updateJobByCode(String code, UpdateJobRequest updateJobRequest) {
        // Check if any updates are provided
        if (!updateJobRequest.hasUpdates()) {
            throw new IllegalArgumentException("No updates provided");
        }
        
        // Find the job by code
        Optional<Job> jobOptional = jobRepository.findByCode(code);
        if (jobOptional.isEmpty()) {
            throw new IllegalArgumentException("Job with code '" + code + "' does not exist");
        }
        
        Job job = jobOptional.get();
        
        // Update fields if provided
        if (updateJobRequest.getName() != null) {
            job.setName(updateJobRequest.getName());
        }
        
        // Note: Description and Department fields would need to be added to Job entity
        // For now, we only update the name field
        
        // Save the updated job
        Job updatedJob = jobRepository.save(job);
        
        // Convert to response DTO
        return convertToJobResponse(updatedJob);
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
