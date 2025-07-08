package com.TicketXChange.TicketXChange.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizResultDTO {
    private String quizNumber;
    private List<Double> marksForEachQuestion;
    private Double totalMarks;
    private LocalDateTime createdAt;
}
