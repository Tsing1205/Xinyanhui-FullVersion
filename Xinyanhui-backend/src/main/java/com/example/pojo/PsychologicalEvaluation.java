package com.example.pojo;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class PsychologicalEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer evaluationId;

    @ManyToOne
    private User user;

    private LocalDateTime evaluationDate;
    private String questionnaireResults;
    private String autoReport;
    private Integer riskLevel;
    private String suggestions;

    public PsychologicalEvaluation() {
    }

    public PsychologicalEvaluation(User user, LocalDateTime evaluationDate,String questionnaireResults, String autoReport, Integer riskLevel, String suggestions) {
        this.user = user;
        this.evaluationDate = evaluationDate;
        this.questionnaireResults = questionnaireResults;
        this.autoReport = autoReport;
        this.riskLevel = riskLevel;
        this.suggestions = suggestions;
    }

}