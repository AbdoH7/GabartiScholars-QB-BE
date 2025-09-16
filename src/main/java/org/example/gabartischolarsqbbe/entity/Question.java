package org.example.gabartischolarsqbbe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Table(name = "questions")
public class Question {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Question text is required")
    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;
    
    @NotNull(message = "Difficulty is required")
    @Min(value = 1, message = "Difficulty must be at least 1")
    @Max(value = 10, message = "Difficulty must be at most 10")
    @Column(name = "difficulty", nullable = false)
    private Integer difficulty;
    
    @NotBlank(message = "Job code is required")
    @Size(max = 50, message = "Job code must not exceed 50 characters")
    @Column(name = "job_code", nullable = false)
    private String jobCode;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "mcqs", columnDefinition = "JSON")
    private List<McqOption> mcqs;
    
    // Default constructor
    public Question() {
    }
    
    // Constructor with parameters
    public Question(String questionText, Integer difficulty, String jobCode, List<McqOption> mcqs) {
        this.questionText = questionText;
        this.difficulty = difficulty;
        this.jobCode = jobCode;
        this.mcqs = mcqs;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getQuestionText() {
        return questionText;
    }
    
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
    
    public Integer getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }
    
    public String getJobCode() {
        return jobCode;
    }
    
    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }
    
    public List<McqOption> getMcqs() {
        return mcqs;
    }
    
    public void setMcqs(List<McqOption> mcqs) {
        this.mcqs = mcqs;
    }
    
    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", questionText='" + questionText + '\'' +
                ", difficulty=" + difficulty +
                ", jobCode='" + jobCode + '\'' +
                ", mcqs=" + mcqs +
                '}';
    }
    
    // Inner class for MCQ options
    public static class McqOption {
        private String value;
        private Boolean correct;
        
        public McqOption() {
        }
        
        public McqOption(String value, Boolean correct) {
            this.value = value;
            this.correct = correct;
        }
        
        public String getValue() {
            return value;
        }
        
        public void setValue(String value) {
            this.value = value;
        }
        
        public Boolean getCorrect() {
            return correct;
        }
        
        public void setCorrect(Boolean correct) {
            this.correct = correct;
        }
        
        @Override
        public String toString() {
            return "McqOption{" +
                    "value='" + value + '\'' +
                    ", correct=" + correct +
                    '}';
        }
    }
}