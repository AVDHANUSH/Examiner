package com.TicketXChange.TicketXChange.quiz.repository;

import com.TicketXChange.TicketXChange.quiz.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findByQuizNumber(String quizId);
}
