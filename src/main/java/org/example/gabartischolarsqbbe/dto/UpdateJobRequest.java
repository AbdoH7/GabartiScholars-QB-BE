package org.example.gabartischolarsqbbe.dto;

import jakarta.validation.constraints.Size;

/**
 * DTO for updating an existing job
 * This class encapsulates the data that can be updated for a job
 * All fields are optional to allow partial updates
 */
public class UpdateJobRequest {
    
    @Size(min = 2, max = 255, message = "Job name must be between 2 and 255 characters")
    private String name;
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
    
    @Size(max = 100, message = "Department must not exceed 100 characters")
    private String department;
    
    // Note: Job code is typically not updatable to maintain data integrity
    
    // Default constructor
    public UpdateJobRequest() {
    }
    
    // Constructor with all parameters
    public UpdateJobRequest(String name, String description, String department) {
        this.name = name;
        this.description = description;
        this.department = department;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    /**
     * Check if any field is set for update
     * @return true if at least one field is not null
     */
    public boolean hasUpdates() {
        return name != null || description != null || department != null;
    }
    
    @Override
    public String toString() {
        return "UpdateJobRequest{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
