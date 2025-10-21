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

import com.krushimitra.entity.Answer;
import com.krushimitra.entity.Question;
import com.krushimitra.entity.User;
import com.krushimitra.repository.AnswerRepository;
import com.krushimitra.repository.QuestionRepository;
import com.krushimitra.service.UserService;

@RestController
@RequestMapping("/api/answers")
@CrossOrigin(origins = "*")
public class AnswerController {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<Answer>> getAnswersByQuestion(@PathVariable Long questionId) {
        Optional<Question> questionOpt = questionRepository.findById(questionId);
        if (questionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(answerRepository.findApprovedAnswersByQuestionOrderByVotes(questionOpt.get()));
    }

    @GetMapping("/expert/{expertId}")
    public ResponseEntity<List<Answer>> getAnswersByExpert(@PathVariable Long expertId) {
        Optional<User> expertOpt = userService.getUserById(expertId);
        if (expertOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(answerRepository.findApprovedAnswersByExpert(expertOpt.get()));
    }

    @PostMapping
    @PreAuthorize("hasRole('EXPERT')")
    public ResponseEntity<?> createAnswer(@RequestBody CreateAnswerRequest request, Authentication authentication) {
        try {
            User expert = userService.getByUsername(authentication.getName()).orElse(null);
            if (expert == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
            }

            // Check if expert is approved
            if (!expert.getIsApproved()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Expert account not approved"));
            }

            Optional<Question> questionOpt = questionRepository.findById(request.getQuestionId());
            if (questionOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Question not found"));
            }

            Answer answer = new Answer();
            answer.setQuestion(questionOpt.get());
            answer.setExpert(expert);
            answer.setContent(request.getContent());
            answer.setIsApproved(true);
            answer.setIsAccepted(false);
            answer.setUpvotes(0);
            answer.setDownvotes(0);

            Answer savedAnswer = answerRepository.save(answer);
            return ResponseEntity.ok(savedAnswer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to create answer: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EXPERT') or hasRole('ADMIN')")
    public ResponseEntity<?> updateAnswer(@PathVariable Long id, @RequestBody UpdateAnswerRequest request, Authentication authentication) {
        try {
            Optional<Answer> answerOpt = answerRepository.findById(id);
            if (answerOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Answer answer = answerOpt.get();
            User currentUser = userService.getByUsername(authentication.getName()).orElse(null);
            
            // Only the answer owner or admin can update
            if (!answer.getExpert().getId().equals(currentUser.getId()) && !currentUser.getRole().name().equals("ADMIN")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Not authorized to update this answer"));
            }

            if (request.getContent() != null) answer.setContent(request.getContent());
            if (request.getIsApproved() != null) answer.setIsApproved(request.getIsApproved());

            Answer updatedAnswer = answerRepository.save(answer);
            return ResponseEntity.ok(updatedAnswer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to update answer: " + e.getMessage()));
        }
    }

    @PostMapping("/{id}/accept")
    @PreAuthorize("hasRole('FARMER') or hasRole('ADMIN')")
    public ResponseEntity<?> acceptAnswer(@PathVariable Long id, Authentication authentication) {
        try {
            Optional<Answer> answerOpt = answerRepository.findById(id);
            if (answerOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Answer answer = answerOpt.get();
            User currentUser = userService.getByUsername(authentication.getName()).orElse(null);
            
            // Only the question owner or admin can accept answers
            if (!answer.getQuestion().getFarmer().getId().equals(currentUser.getId()) && !currentUser.getRole().name().equals("ADMIN")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Not authorized to accept this answer"));
            }

            // Unaccept all other answers for this question
            List<Answer> allAnswers = answerRepository.findByQuestion(answer.getQuestion());
            for (Answer a : allAnswers) {
                a.setIsAccepted(false);
                answerRepository.save(a);
            }

            // Accept this answer
            answer.setIsAccepted(true);
            answer.getQuestion().setIsResolved(true);
            questionRepository.save(answer.getQuestion());
            Answer updatedAnswer = answerRepository.save(answer);

            return ResponseEntity.ok(updatedAnswer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to accept answer: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EXPERT') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteAnswer(@PathVariable Long id, Authentication authentication) {
        try {
            Optional<Answer> answerOpt = answerRepository.findById(id);
            if (answerOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Answer answer = answerOpt.get();
            User currentUser = userService.getByUsername(authentication.getName()).orElse(null);
            
            // Only the answer owner or admin can delete
            if (!answer.getExpert().getId().equals(currentUser.getId()) && !currentUser.getRole().name().equals("ADMIN")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Not authorized to delete this answer"));
            }

            answerRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Answer deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to delete answer: " + e.getMessage()));
        }
    }

    // Inner classes for request/response
    public static class CreateAnswerRequest {
        private Long questionId;
        private String content;

        public Long getQuestionId() { return questionId; }
        public void setQuestionId(Long questionId) { this.questionId = questionId; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    public static class UpdateAnswerRequest {
        private String content;
        private Boolean isApproved;

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public Boolean getIsApproved() { return isApproved; }
        public void setIsApproved(Boolean isApproved) { this.isApproved = isApproved; }
    }
}
