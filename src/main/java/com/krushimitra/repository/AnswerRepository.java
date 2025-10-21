package com.krushimitra.repository;

import com.krushimitra.entity.Answer;
import com.krushimitra.entity.Question;
import com.krushimitra.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    
    List<Answer> findByQuestion(Question question);
    
    List<Answer> findByExpert(User expert);
    
    List<Answer> findByIsApproved(Boolean isApproved);
    
    List<Answer> findByIsAccepted(Boolean isAccepted);
    
    @Query("SELECT a FROM Answer a WHERE a.question = :question AND a.isApproved = true ORDER BY a.upvotes DESC, a.createdAt ASC")
    List<Answer> findApprovedAnswersByQuestionOrderByVotes(@Param("question") Question question);
    
    @Query("SELECT a FROM Answer a WHERE a.expert = :expert AND a.isApproved = true ORDER BY a.createdAt DESC")
    List<Answer> findApprovedAnswersByExpert(@Param("expert") User expert);
    
    @Query("SELECT COUNT(a) FROM Answer a WHERE a.expert = :expert AND a.isApproved = true")
    Long countApprovedAnswersByExpert(@Param("expert") User expert);
}
