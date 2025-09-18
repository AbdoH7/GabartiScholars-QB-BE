package org.example.gabartischolarsqbbe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating a new job
 * This class encapsulates the data required for job creation
 */
public class CreateJobRequest {
    
    @NotBlank(message = "Job name is required")
    @Size(min = 2, max = 255, message = "Job name must be between 2 and 255 characters")
    private String name;
    
    @NotBlank(message = "Job code is required")
    @Size(min = 2, max = 50, message = "Job code must be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "Job code must contain only uppercase letters, numbers, underscores, and hyphens")
    private String code;
    
    // Optional field for future expansion
    private String description;
    
    // Optional field for categorization
    private String department;
    
    // Default constructor
    public CreateJobRequest() {
    }
    
    // Constructor with required parameters
    public CreateJobRequest(String name, String code) {
        this.name = name;
        this.code = code;
    }
    
    // Constructor with all parameters
    public CreateJobRequest(String name, String code, String description, String department) {
        this.name = name;
        this.code = code;
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
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
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
     * Builder pattern for convenient object creation
     */
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String name;
        private String code;
        private String description;
        private String department;
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public Builder code(String code) {
            this.code = code;
            return this;
        }
        
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        
        public Builder department(String department) {
            this.department = department;
            return this;
        }
        
        public CreateJobRequest build() {
            return new CreateJobRequest(name, code, description, department);
        }
    }
    
    @Override
    public String toString() {
        return "CreateJobRequest{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
