package org.example.gabartischolarsqbbe.repository;

import org.example.gabartischolarsqbbe.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    
    /**
     * Find a job by its code
     * @param code the job code
     * @return Optional containing the job if found
     */
    Optional<Job> findByCode(String code);
    
    /**
     * Check if a job exists with the given code
     * @param code the job code
     * @return true if job exists, false otherwise
     */
    boolean existsByCode(String code);
}