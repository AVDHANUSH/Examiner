package com.TicketXChange.TicketXChange.quiz.repository;

import com.TicketXChange.TicketXChange.auth.model.UserProfile;
import com.TicketXChange.TicketXChange.quiz.model.Quiz;
import com.TicketXChange.TicketXChange.quiz.model.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
    Optional<Quiz> findByQuizNumber(String quizId);

    List<QuizResult> findByUser(UserProfile userProfile);
}
