package org.example.gabartischolarsqbbe.dto;

public class JobResponse {
    
    private Long id;
    private String name;
    private String code;
    
    // Default constructor
    public JobResponse() {
    }
    
    // Constructor with parameters
    public JobResponse(Long id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
        return "JobResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
