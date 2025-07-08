package com.TicketXChange.TicketXChange.quiz.dto;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizDTO {

    private String quizNumber;
    private List<QuestionDTO> questions;
}
