package org.example.gabartischolarsqbbe.controller;

import org.example.gabartischolarsqbbe.dto.ApiResponse;
import org.example.gabartischolarsqbbe.dto.CsvUploadResponse;
import org.example.gabartischolarsqbbe.entity.Question;
import org.example.gabartischolarsqbbe.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "*") 
public class QuestionController {
    
    private final QuestionService questionService;
    
    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }
    
    /**
     * Upload CSV file with questions
     * POST /api/questions/upload-csv
     * Multipart form data with file field named "file"
     * 
     * CSV format: question_text,difficulty,job_code,option1,option1_correct,option2,option2_correct,option3,option3_correct,option4,option4_correct
     * Example CSV row: "What is Java?,5,DEV001,Programming Language,true,Database,false,Operating System,false,Web Browser,false"
     */
    @PostMapping("/upload-csv")
    public ResponseEntity<ApiResponse<CsvUploadResponse>> uploadCsvFile(@RequestParam("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                ApiResponse<CsvUploadResponse> response = ApiResponse.error("Please select a file to upload");
                return ResponseEntity.badRequest().body(response);
            }
            
            CsvUploadResponse uploadResponse = questionService.processCsvFile(file);
            
            if (uploadResponse.getSuccessfullyInserted() > 0) {
                ApiResponse<CsvUploadResponse> response = ApiResponse.success("CSV file processed successfully", uploadResponse);
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<CsvUploadResponse> response = ApiResponse.error("No records were successfully processed");
                response.setData(uploadResponse);
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            ApiResponse<CsvUploadResponse> response = ApiResponse.error("An error occurred while processing the CSV file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get questions by job title ID
     * GET /api/questions/job/{jobTitleId}
     */
    @GetMapping("/job/{jobTitleId}")
    public ResponseEntity<ApiResponse<List<Question>>> getQuestionsByJobTitleId(@PathVariable Long jobTitleId) {
        try {
            List<Question> questions = questionService.getQuestionsByJobTitleId(jobTitleId);
            ApiResponse<List<Question>> response = ApiResponse.success("Questions retrieved successfully", questions);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<Question>> response = ApiResponse.error("An error occurred while retrieving questions");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get questions count by job title ID
     * GET /api/questions/job/{jobTitleId}/count
     */
    @GetMapping("/job/{jobTitleId}/count")
    public ResponseEntity<ApiResponse<Long>> getQuestionsCountByJobTitleId(@PathVariable Long jobTitleId) {
        try {
            long count = questionService.getQuestionsCountByJobTitleId(jobTitleId);
            ApiResponse<Long> response = ApiResponse.success("Questions count retrieved successfully", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Long> response = ApiResponse.error("An error occurred while retrieving questions count");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}