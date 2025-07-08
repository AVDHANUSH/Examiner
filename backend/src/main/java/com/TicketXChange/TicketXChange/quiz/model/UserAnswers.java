package com.TicketXChange.TicketXChange.quiz.model;

import com.TicketXChange.TicketXChange.auth.model.UserProfile;
import com.TicketXChange.TicketXChange.quiz.enums.QuestionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.mapping.ToOne;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "useranswers")
public class UserAnswers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "quiz_number", nullable = false)
    private String quizNumber;

    @ElementCollection
    @CollectionTable(name = "useranswers_questions", joinColumns = @JoinColumn(name = "useranswers_id"))
    @Column(name = "question")
    private List<String> questions;

    @ElementCollection
    @CollectionTable(name = "useranswers_answers", joinColumns = @JoinColumn(name = "useranswers_id"))
    @Column(name = "answer")
    private List<String> answers;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserProfile user;
}
