package org.example.gabartischolarsqbbe.service;

import org.example.gabartischolarsqbbe.dto.CsvUploadResponse;
import org.example.gabartischolarsqbbe.entity.Question;
import org.example.gabartischolarsqbbe.repository.QuestionRepository;
import org.example.gabartischolarsqbbe.util.QuestionsCSVImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
public class QuestionService {
    
    private final QuestionRepository questionRepository;
    private final JobService jobService;
    private final QuestionsCSVImporter csvImporter;
    
    @Autowired
    public QuestionService(QuestionRepository questionRepository, JobService jobService) {
        this.questionRepository = questionRepository;
        this.jobService = jobService;
        this.csvImporter = new QuestionsCSVImporter(jobService);
    }
    
    /**
     * Process CSV file and save questions to database
     * CSV format: question_text,difficulty,job_code,option1,option1_correct,option2,option2_correct,option3,option3_correct,option4,option4_correct
     * @param file the CSV file to process
     * @return CsvUploadResponse with processing results
     */
    public CsvUploadResponse processCsvFile(MultipartFile file) {
        try {
            // Use the CSV importer utility to parse the file
            QuestionsCSVImporter.ImportResult importResult = csvImporter.importQuestionsFromCSV(file);
            
            // Bulk create the successfully parsed questions
            int successfullyInserted = 0;
            if (!importResult.getQuestions().isEmpty()) {
                successfullyInserted = bulkCreateQuestions(importResult.getQuestions());
            }
            
            // Build response message
            String message = String.format("Processing completed. %d successful, %d failed out of %d total records.", 
                    successfullyInserted, importResult.getFailedCount(), importResult.getTotalProcessed());
            
            if (importResult.hasErrors()) {
                message += " Errors: " + String.join(" ", importResult.getErrors());
            }
            
            return new CsvUploadResponse(
                importResult.getTotalProcessed(), 
                successfullyInserted, 
                importResult.getFailedCount(), 
                message
            );
            
        } catch (Exception e) {
            return new CsvUploadResponse(0, 0, 0, "Error processing CSV file: " + e.getMessage());
        }
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
    
    /**
     * Bulk create questions
     * @param questions the list of questions to create
     * @return the number of successfully created questions
     */
    @Transactional
    public int bulkCreateQuestions(List<Question> questions) {
        if (questions == null || questions.isEmpty()) {
            return 0;
        }
        
        List<Question> savedQuestions = questionRepository.saveAll(questions);
        return savedQuestions.size();
    }
}
