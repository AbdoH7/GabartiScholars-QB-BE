package org.example.gabartischolarsqbbe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class JobRequest {
    
    @NotBlank(message = "Job name is required")
    @Size(max = 255, message = "Job name must not exceed 255 characters")
    private String name;
    
    @NotBlank(message = "Job code is required")
    @Size(max = 50, message = "Job code must not exceed 50 characters")
    private String code;
    
    // Default constructor
    public JobRequest() {
    }
    
    // Constructor with parameters
    public JobRequest(String name, String code) {
        this.name = name;
        this.code = code;
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
    
    @Override
    public String toString() {
        return "JobRequest{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}