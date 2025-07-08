package com.TicketXChange.TicketXChange.quiz.repository;

import com.TicketXChange.TicketXChange.auth.model.UserProfile;
import com.TicketXChange.TicketXChange.quiz.model.Quiz;
import com.TicketXChange.TicketXChange.quiz.model.QuizResult;
import com.TicketXChange.TicketXChange.quiz.model.UserAnswers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAnswersRepository extends JpaRepository<UserAnswers, Integer> {
    Optional<List<UserAnswers>> findByQuizNumber(String quizNumber);

    List<UserAnswers> findByUser(UserProfile userProfile);
}