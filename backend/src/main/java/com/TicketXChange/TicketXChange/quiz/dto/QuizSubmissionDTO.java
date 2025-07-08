package com.TicketXChange.TicketXChange.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizSubmissionDTO {

    private String quizNumber;
    private List<QuestionDTO> questions;
}
