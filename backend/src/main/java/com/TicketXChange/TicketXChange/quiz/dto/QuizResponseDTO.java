package com.TicketXChange.TicketXChange.quiz.dto;

import com.TicketXChange.TicketXChange.quiz.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QuizResponseDTO {
    private String text;
    private QuestionType type;
    private List<String> options;
}