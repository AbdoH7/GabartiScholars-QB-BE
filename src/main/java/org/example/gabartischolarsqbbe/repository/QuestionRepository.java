package org.example.gabartischolarsqbbe.repository;

import org.example.gabartischolarsqbbe.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    /**
     * Find all questions by job code
     * @param jobCode the job code
     * @return list of questions for the given job code
     */
    List<Question> findByJobCode(String jobCode);
    
    /**
     * Find questions by job code and difficulty
     * @param jobCode the job code
     * @param difficulty the difficulty level
     * @return list of questions matching the criteria
     */
    List<Question> findByJobCodeAndDifficulty(String jobCode, Integer difficulty);
    
    /**
     * Count questions by job code
     * @param jobCode the job code
     * @return number of questions for the given job code
     */
    long countByJobCode(String jobCode);
}