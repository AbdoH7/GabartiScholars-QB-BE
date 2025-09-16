package org.example.gabartischolarsqbbe.controller;

import jakarta.validation.Valid;
import org.example.gabartischolarsqbbe.dto.ApiResponse;
import org.example.gabartischolarsqbbe.dto.JobRequest;
import org.example.gabartischolarsqbbe.dto.JobResponse;
import org.example.gabartischolarsqbbe.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*")
public class JobController {
    
    private final JobService jobService;
    
    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }
    
    /**
     * Create a new job
     * POST /api/jobs
     * Request body: {"name": "Software Developer", "code": "SD001"}
     */
    @PostMapping
    public ResponseEntity<ApiResponse<JobResponse>> createJob(@Valid @RequestBody JobRequest jobRequest) {
        try {
            JobResponse jobResponse = jobService.createJob(jobRequest);
            ApiResponse<JobResponse> response = ApiResponse.success("Job created successfully", jobResponse);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<JobResponse> response = ApiResponse.error(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            ApiResponse<JobResponse> response = ApiResponse.error("An error occurred while creating the job");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get all jobs
     * GET /api/jobs
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<JobResponse>>> getAllJobs() {
        try {
            List<JobResponse> jobs = jobService.getAllJobs();
            ApiResponse<List<JobResponse>> response = ApiResponse.success("Jobs retrieved successfully", jobs);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<JobResponse>> response = ApiResponse.error("An error occurred while retrieving jobs");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get job by code
     * GET /api/jobs/code/{code}
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<JobResponse>> getJobByCode(@PathVariable String code) {
        try {
            Optional<JobResponse> jobResponse = jobService.getJobByCode(code);
            if (jobResponse.isPresent()) {
                ApiResponse<JobResponse> response = ApiResponse.success("Job retrieved successfully", jobResponse.get());
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<JobResponse> response = ApiResponse.error("Job not found with code: " + code);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            ApiResponse<JobResponse> response = ApiResponse.error("An error occurred while retrieving the job");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}