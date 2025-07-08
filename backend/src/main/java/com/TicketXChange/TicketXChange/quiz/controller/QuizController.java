package com.TicketXChange.TicketXChange.quiz.controller;


import com.TicketXChange.TicketXChange.auth.dtos.UserProfileResponse;
import com.TicketXChange.TicketXChange.auth.model.User;
import com.TicketXChange.TicketXChange.auth.model.UserProfile;
import com.TicketXChange.TicketXChange.auth.repository.UserProfileRepository;
import com.TicketXChange.TicketXChange.quiz.dto.*;
import com.TicketXChange.TicketXChange.quiz.model.Quiz;
import com.TicketXChange.TicketXChange.quiz.model.QuizResult;
import com.TicketXChange.TicketXChange.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;
    private final UserProfileRepository userProfileRepository;

    @PostMapping
    public ResponseEntity<Quiz> createQuiz(@RequestBody QuizDTO quizDTO) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = currentUser.getUsername();

        // Retrieve the user's profile from the repository
        Optional<UserProfile> userProfileOptional = userProfileRepository.findByUserEmail(userEmail);

        UserProfile userProfile = userProfileOptional.get();

        Quiz quiz = quizService.createQuiz(quizDTO, userProfile);
        return ResponseEntity.ok(quiz);
    }

    @GetMapping("/{quizId}/questions")
    public ResponseEntity<List<QuizResponseDTO>> getQuestionsByQuizId(@PathVariable String quizId) {
        List<QuizResponseDTO> questions = quizService.getQuestionsByQuizId(quizId);
        return ResponseEntity.ok(questions);
    }

    // Retrieve all available quiz IDs on the platform
    @GetMapping("/available")
    public ResponseEntity<List<String>> getAllQuizIds() {
        List<String> quizIds = quizService.getAllQuizIds();
        return ResponseEntity.ok(quizIds);
    }

    @PostMapping("/evaluate")
    public ResponseEntity<String> evaluateQuiz(@RequestBody QuizSubmissionDTO quizSubmissionDTO) {
        // Handle the quiz submission logic here.
        // You can call a service method to process the submission.

        // quizService.evaluateQuiz(quizSubmissionDTO);

        // For now, just return a success message
        String result = quizService.evaluateQuiz(quizSubmissionDTO);
        return new ResponseEntity<>("Quiz submitted successfully!", HttpStatus.OK);
    }

    @GetMapping("/quiz-results")
    public ResponseEntity<List<QuizResultDTO>> getQuizResults() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = currentUser.getUsername();

        // Retrieve the user's profile from the repository
        Optional<UserProfile> userProfileOptional = userProfileRepository.findByUserEmail(userEmail);

        if (userProfileOptional.isPresent()) {
            UserProfile userProfile = userProfileOptional.get();
            List<QuizResultDTO> quizResults = quizService.getQuizResults(userProfile);
            return ResponseEntity.ok(quizResults);
        } else {
            return ResponseEntity.status(404).body(null); // or throw a custom exception
        }
    }


}
