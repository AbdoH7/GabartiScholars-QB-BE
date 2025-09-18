package org.example.gabartischolarsqbbe.repository;

import org.example.gabartischolarsqbbe.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    /**
     * Find all questions by job title ID
     * @param jobTitleId the job title ID
     * @return list of questions for the given job title
     */
    List<Question> findByJobTitleId(Long jobTitleId);
    
    /**
     * Find questions by job title ID and difficulty
     * @param jobTitleId the job title ID
     * @param difficulty the difficulty level
     * @return list of questions matching the criteria
     */
    List<Question> findByJobTitleIdAndDifficulty(Long jobTitleId, Integer difficulty);
    
    /**
     * Count questions by job title ID
     * @param jobTitleId the job title ID
     * @return number of questions for the given job title
     */
    long countByJobTitleId(Long jobTitleId);
}
