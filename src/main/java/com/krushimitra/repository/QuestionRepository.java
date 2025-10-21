package com.krushimitra.repository;

import com.krushimitra.entity.Question;
import com.krushimitra.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    List<Question> findByFarmer(User farmer);
    
    List<Question> findByIsApproved(Boolean isApproved);
    
    List<Question> findByIsResolved(Boolean isResolved);
    
    List<Question> findByCategory(String category);
    
    @Query("SELECT q FROM Question q WHERE q.isApproved = true ORDER BY q.createdAt DESC")
    List<Question> findApprovedQuestionsOrderByDate();
    
    @Query("SELECT q FROM Question q WHERE q.title LIKE %:title% OR q.content LIKE %:content% AND q.isApproved = true")
    List<Question> searchQuestions(@Param("title") String title, @Param("content") String content);
    
    @Query("SELECT q FROM Question q WHERE q.category = :category AND q.isApproved = true")
    List<Question> findByCategoryAndApproved(@Param("category") String category);
    
    @Query("SELECT q FROM Question q WHERE q.isResolved = false AND q.isApproved = true ORDER BY q.createdAt DESC")
    List<Question> findUnresolvedQuestions();
}
