package org.example.gabartischolarsqbbe.dto;

public class CsvUploadResponse {
    
    private int totalProcessed;
    private int successfullyInserted;
    private int failed;
    private String message;
    
    // Default constructor
    public CsvUploadResponse() {
    }
    
    // Constructor with parameters
    public CsvUploadResponse(int totalProcessed, int successfullyInserted, int failed, String message) {
        this.totalProcessed = totalProcessed;
        this.successfullyInserted = successfullyInserted;
        this.failed = failed;
        this.message = message;
    }
    
    // Getters and Setters
    public int getTotalProcessed() {
        return totalProcessed;
    }
    
    public void setTotalProcessed(int totalProcessed) {
        this.totalProcessed = totalProcessed;
    }
    
    public int getSuccessfullyInserted() {
        return successfullyInserted;
    }
    
    public void setSuccessfullyInserted(int successfullyInserted) {
        this.successfullyInserted = successfullyInserted;
    }
    
    public int getFailed() {
        return failed;
    }
    
    public void setFailed(int failed) {
        this.failed = failed;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        return "CsvUploadResponse{" +
                "totalProcessed=" + totalProcessed +
                ", successfullyInserted=" + successfullyInserted +
                ", failed=" + failed +
                ", message='" + message + '\'' +
                '}';
    }
}
