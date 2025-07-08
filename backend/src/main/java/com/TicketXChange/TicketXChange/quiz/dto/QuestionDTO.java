package com.TicketXChange.TicketXChange.quiz.dto;

import com.TicketXChange.TicketXChange.quiz.enums.QuestionType;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {

    private String text;
    private QuestionType type;
    private List<String> options;
    private String answer;
}

