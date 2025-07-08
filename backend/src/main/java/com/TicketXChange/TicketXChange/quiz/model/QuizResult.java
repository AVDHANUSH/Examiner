package com.TicketXChange.TicketXChange.quiz.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.TicketXChange.TicketXChange.auth.model.UserProfile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "quiz_results")
public class QuizResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quiz_number", nullable = false)
    private String quizNumber;

    @ElementCollection
    @CollectionTable(name = "quiz_marks", joinColumns = @JoinColumn(name = "quiz_result_id"))
    @Column(name = "marks")
    private List<Double> marksForEachQuestion;

    @Column(name = "total_marks", nullable = false)
    private Double totalMarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id", nullable = false)
    private UserProfile user;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}

