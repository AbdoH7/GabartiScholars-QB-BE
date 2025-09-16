package org.example.gabartischolarsqbbe.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.example.gabartischolarsqbbe.dto.CsvUploadResponse;
import org.example.gabartischolarsqbbe.entity.Question;
import org.example.gabartischolarsqbbe.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class QuestionService {
    
    private final QuestionRepository questionRepository;
    private final JobService jobService;
    
    @Autowired
    public QuestionService(QuestionRepository questionRepository, JobService jobService) {
        this.questionRepository = questionRepository;
        this.jobService = jobService;
    }
    
    /**
     * Process CSV file and save questions to database
     * CSV format: question_text,difficulty,job_code,option1,option1_correct,option2,option2_correct,option3,option3_correct,option4,option4_correct
     * @param file the CSV file to process
     * @return CsvUploadResponse with processing results
     */
    public CsvUploadResponse processCsvFile(MultipartFile file) {
        if (file.isEmpty()) {
            return new CsvUploadResponse(0, 0, 0, "File is empty");
        }
        
        if (!isValidCsvFile(file)) {
            return new CsvUploadResponse(0, 0, 0, "Invalid file format. Please upload a CSV file");
        }
        
        int totalProcessed = 0;
        int successfullyInserted = 0;
        int failed = 0;
        StringBuilder errorMessages = new StringBuilder();
        
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            List<String[]> records = csvReader.readAll();
            
            if (records.isEmpty()) {
                return new CsvUploadResponse(0, 0, 0, "CSV file contains no records.");
            }
            // Skip header row if exists
            boolean hasHeader = isHeaderRow(records.get(0));
            int startIndex = hasHeader ? 1 : 0;
            
            for (int i = startIndex; i < records.size(); i++) {
                String[] record = records.get(i);
                totalProcessed++;
                
                try {
                    Question question = parseRecordToQuestion(record);
                    
                    questionRepository.save(question);
                    successfullyInserted++;
                    
                } catch (Exception e) {
                    failed++;
                    errorMessages.append("Row ").append(i + 1).append(": ").append(e.getMessage()).append(". ");
                }
            }
            
        } catch (IOException | CsvException e) {
            return new CsvUploadResponse(0, 0, 0, "Error reading CSV file: " + e.getMessage());
        }
        
        String message = String.format("Processing completed. %d successful, %d failed out of %d total records.", 
                successfullyInserted, failed, totalProcessed);
        
        if (errorMessages.length() > 0) {
            message += " Errors: " + errorMessages.toString();
        }
        
        return new CsvUploadResponse(totalProcessed, successfullyInserted, failed, message);
    }
    
    /**
     * Parse a CSV record to Question entity
     * Expected format: question_text,difficulty,job_code,option1,option1_correct,option2,option2_correct,option3,option3_correct,option4,option4_correct
     */
    private Question parseRecordToQuestion(String[] record) {
        if (record.length < 11) {
            throw new IllegalArgumentException("Invalid CSV format. Expected at least 11 columns");
        }
        
        try {
            String questionText = record[0].trim();
            Integer difficulty = Integer.parseInt(record[1].trim());
            String jobCode = record[2].trim();
            
            if (questionText.isEmpty()) {
                throw new IllegalArgumentException("Question text cannot be empty");
            }
            
            if (difficulty < 1 || difficulty > 10) {
                throw new IllegalArgumentException("Difficulty must be between 1 and 10");
            }
            
            if (jobCode.isEmpty()) {
                throw new IllegalArgumentException("Job code cannot be empty");
            }
            
            // Convert job code to job ID
            Optional<JobResponse> jobResponse = jobService.getJobByCode(jobCode);
            if (jobResponse.isEmpty()) {
                throw new IllegalArgumentException("Job with code '" + jobCode + "' does not exist");
            }
            Long jobTitleId = jobResponse.get().getId();
            
            // Parse MCQ options
            List<Question.McqOption> mcqOptions = new ArrayList<>();
            
            for (int i = 0; i < 4; i++) {
                int valueIndex = 3 + (i * 2);
                int correctIndex = 4 + (i * 2);
                
                if (valueIndex < record.length && correctIndex < record.length) {
                    String value = record[valueIndex].trim();
                    Boolean correct = Boolean.parseBoolean(record[correctIndex].trim());
                    
                    if (!value.isEmpty()) {
                        mcqOptions.add(new Question.McqOption(value, correct));
                    }
                }
            }
            
            if (mcqOptions.size() < 2) {
                throw new IllegalArgumentException("At least 2 MCQ options are required");
            }
            
            // Validate that at least one option is marked as correct
            boolean hasCorrectOption = mcqOptions.stream().anyMatch(Question.McqOption::getCorrect);
            if (!hasCorrectOption) {
                throw new IllegalArgumentException("At least one MCQ option must be marked as correct");
            }
            
            return new Question(questionText, difficulty, jobTitleId, mcqOptions);
            
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in CSV record");
        }
    }
    
    /**
     * Check if the uploaded file is a valid CSV file
     */
    private boolean isValidCsvFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return fileName != null && fileName.toLowerCase().endsWith(".csv");
    }
    
    /**
     * Check if the first row is a header row
     */
    private boolean isHeaderRow(String[] firstRow) {
        if (firstRow.length < 3) {
            return false;
        }
        
        // Check if first three columns contain typical header names
        String col1 = firstRow[0].toLowerCase().trim();
        String col2 = firstRow[1].toLowerCase().trim();
        String col3 = firstRow[2].toLowerCase().trim();
        
        return col1.contains("question") || col2.contains("difficulty") || col3.contains("job") || col3.contains("code");
    }
    
    /**
     * Get all questions by job title ID
     */
    @Transactional(readOnly = true)
    public List<Question> getQuestionsByJobTitleId(Long jobTitleId) {
        return questionRepository.findByJobTitleId(jobTitleId);
    }
    
    /**
     * Get questions count by job title ID
     */
    @Transactional(readOnly = true)
    public long getQuestionsCountByJobTitleId(Long jobTitleId) {
        return questionRepository.countByJobTitleId(jobTitleId);
    }
}