package com.TicketXChange.TicketXChange.quiz.repository;

import com.TicketXChange.TicketXChange.quiz.model.Question;
import com.TicketXChange.TicketXChange.quiz.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}

