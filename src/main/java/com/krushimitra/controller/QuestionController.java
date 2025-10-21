package com.krushimitra.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krushimitra.entity.Question;
import com.krushimitra.entity.User;
import com.krushimitra.repository.QuestionRepository;
import com.krushimitra.service.UserService;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "*")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/public")
    public ResponseEntity<List<Question>> getPublicQuestions() {
        return ResponseEntity.ok(questionRepository.findApprovedQuestionsOrderByDate());
    }

    @GetMapping
    @PreAuthorize("hasRole('FARMER') or hasRole('EXPERT') or hasRole('ADMIN')")
    public ResponseEntity<List<Question>> getAllQuestions(Authentication authentication) {
        User user = userService.getByUsername(authentication.getName()).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        if (user.getRole().name().equals("FARMER")) {
            return ResponseEntity.ok(questionRepository.findByFarmer(user));
        } else {
            return ResponseEntity.ok(questionRepository.findApprovedQuestionsOrderByDate());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        Optional<Question> question = questionRepository.findById(id);
        return question.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/unresolved")
    @PreAuthorize("hasRole('EXPERT') or hasRole('ADMIN')")
    public ResponseEntity<List<Question>> getUnresolvedQuestions() {
        return ResponseEntity.ok(questionRepository.findUnresolvedQuestions());
    }

    @PostMapping
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<?> createQuestion(@RequestBody CreateQuestionRequest request, Authentication authentication) {
        try {
            User farmer = userService.getByUsername(authentication.getName()).orElse(null);
            if (farmer == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
            }

            Question question = new Question();
            question.setFarmer(farmer);
            question.setTitle(request.getTitle());
            question.setContent(request.getContent());
            question.setCategory(request.getCategory());
            question.setTags(request.getTags());
            question.setIsApproved(true);
            question.setIsResolved(false);
            question.setViewCount(0);

            Question savedQuestion = questionRepository.save(question);
            return ResponseEntity.ok(savedQuestion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to create question: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('FARMER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateQuestion(@PathVariable Long id, @RequestBody UpdateQuestionRequest request, Authentication authentication) {
        try {
            Optional<Question> questionOpt = questionRepository.findById(id);
            if (questionOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Question question = questionOpt.get();
            User currentUser = userService.getByUsername(authentication.getName()).orElse(null);
            
            // Only the question owner or admin can update
            if (!question.getFarmer().getId().equals(currentUser.getId()) && !currentUser.getRole().name().equals("ADMIN")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Not authorized to update this question"));
            }

            if (request.getTitle() != null) question.setTitle(request.getTitle());
            if (request.getContent() != null) question.setContent(request.getContent());
            if (request.getCategory() != null) question.setCategory(request.getCategory());
            if (request.getTags() != null) question.setTags(request.getTags());
            if (request.getIsResolved() != null) question.setIsResolved(request.getIsResolved());

            Question updatedQuestion = questionRepository.save(question);
            return ResponseEntity.ok(updatedQuestion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to update question: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('FARMER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id, Authentication authentication) {
        try {
            Optional<Question> questionOpt = questionRepository.findById(id);
            if (questionOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Question question = questionOpt.get();
            User currentUser = userService.getByUsername(authentication.getName()).orElse(null);
            
            // Only the question owner or admin can delete
            if (!question.getFarmer().getId().equals(currentUser.getId()) && !currentUser.getRole().name().equals("ADMIN")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Not authorized to delete this question"));
            }

            questionRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Question deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to delete question: " + e.getMessage()));
        }
    }

    // Inner classes for request/response
    public static class CreateQuestionRequest {
        private String title;
        private String content;
        private String category;
        private String tags;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getTags() { return tags; }
        public void setTags(String tags) { this.tags = tags; }
    }

    public static class UpdateQuestionRequest {
        private String title;
        private String content;
        private String category;
        private String tags;
        private Boolean isResolved;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getTags() { return tags; }
        public void setTags(String tags) { this.tags = tags; }
        public Boolean getIsResolved() { return isResolved; }
        public void setIsResolved(Boolean isResolved) { this.isResolved = isResolved; }
    }
}
