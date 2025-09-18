package org.example.gabartischolarsqbbe.util;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.example.gabartischolarsqbbe.dto.JobResponse;
import org.example.gabartischolarsqbbe.entity.McqOption;
import org.example.gabartischolarsqbbe.entity.Question;
import org.example.gabartischolarsqbbe.service.JobService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Utility class for importing questions from CSV files
 * Handles CSV parsing and conversion to Question entities
 */
public class QuestionsCSVImporter {
    
    private final JobService jobService;
    
    public QuestionsCSVImporter(JobService jobService) {
        this.jobService = jobService;
    }
    
    /**
     * Import questions from CSV file
     * @param file the CSV file to import
     * @return ImportResult containing the parsed questions and any errors
     * @throws IOException if there's an error reading the file
     * @throws CsvException if there's an error parsing the CSV
     */
    public ImportResult importQuestionsFromCSV(MultipartFile file) throws IOException, CsvException {
        validateFile(file);
        
        List<Question> questions = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        int totalProcessed = 0;
        
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            List<String[]> records = csvReader.readAll();
            
            if (records.isEmpty()) {
                throw new IllegalArgumentException("CSV file contains no records");
            }
            
            // Skip header row if exists
            boolean hasHeader = isHeaderRow(records.get(0));
            int startIndex = hasHeader ? 1 : 0;
            
            for (int i = startIndex; i < records.size(); i++) {
                String[] record = records.get(i);
                totalProcessed++;
                
                try {
                    Question question = parseRecordToQuestion(record);
                    questions.add(question);
                } catch (Exception e) {
                    errors.add("Row " + (i + 1) + ": " + e.getMessage());
                }
            }
        }
        
        return new ImportResult(questions, errors, totalProcessed);
    }
    
    /**
     * Validate the uploaded file
     * @param file the file to validate
     * @throws IllegalArgumentException if the file is invalid
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        
        if (!isValidCsvFile(file)) {
            throw new IllegalArgumentException("Invalid file format. Please upload a CSV file");
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
            
            validateQuestionFields(questionText, difficulty, jobCode);
            
            // Convert job code to job ID
            Long jobTitleId = getJobIdFromCode(jobCode);
            
            // Parse MCQ options
            List<McqOption> mcqOptions = parseMcqOptions(record);
            
            return new Question(questionText, difficulty, jobTitleId, mcqOptions);
            
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in CSV record");
        }
    }
    
    /**
     * Validate question fields
     */
    private void validateQuestionFields(String questionText, Integer difficulty, String jobCode) {
        if (questionText.isEmpty()) {
            throw new IllegalArgumentException("Question text cannot be empty");
        }
        
        if (difficulty < 1 || difficulty > 10) {
            throw new IllegalArgumentException("Difficulty must be between 1 and 10");
        }
        
        if (jobCode.isEmpty()) {
            throw new IllegalArgumentException("Job code cannot be empty");
        }
    }
    
    /**
     * Get job ID from job code
     */
    private Long getJobIdFromCode(String jobCode) {
        Optional<JobResponse> jobResponse = jobService.getJobByCode(jobCode);
        if (jobResponse.isEmpty()) {
            throw new IllegalArgumentException("Job with code '" + jobCode + "' does not exist");
        }
        return jobResponse.get().getId();
    }
    
    /**
     * Parse MCQ options from CSV record
     */
    private List<McqOption> parseMcqOptions(String[] record) {
        List<McqOption> mcqOptions = new ArrayList<>();
        
        for (int i = 0; i < 4; i++) {
            int valueIndex = 3 + (i * 2);
            int correctIndex = 4 + (i * 2);
            
            if (valueIndex < record.length && correctIndex < record.length) {
                String value = record[valueIndex].trim();
                Boolean correct = Boolean.parseBoolean(record[correctIndex].trim());
                
                if (!value.isEmpty()) {
                    mcqOptions.add(new McqOption(value, correct));
                }
            }
        }
        
        validateMcqOptions(mcqOptions);
        return mcqOptions;
    }
    
    /**
     * Validate MCQ options
     */
    private void validateMcqOptions(List<McqOption> mcqOptions) {
        if (mcqOptions.size() < 2) {
            throw new IllegalArgumentException("At least 2 MCQ options are required");
        }
        
        boolean hasCorrectOption = mcqOptions.stream().anyMatch(McqOption::getCorrect);
        if (!hasCorrectOption) {
            throw new IllegalArgumentException("At least one MCQ option must be marked as correct");
        }
    }
    
    /**
     * Result class for CSV import operation
     */
    public static class ImportResult {
        private final List<Question> questions;
        private final List<String> errors;
        private final int totalProcessed;
        
        public ImportResult(List<Question> questions, List<String> errors, int totalProcessed) {
            this.questions = questions;
            this.errors = errors;
            this.totalProcessed = totalProcessed;
        }
        
        public List<Question> getQuestions() {
            return questions;
        }
        
        public List<String> getErrors() {
            return errors;
        }
        
        public int getTotalProcessed() {
            return totalProcessed;
        }
        
        public int getSuccessfulCount() {
            return questions.size();
        }
        
        public int getFailedCount() {
            return errors.size();
        }
        
        public boolean hasErrors() {
            return !errors.isEmpty();
        }
    }
}
