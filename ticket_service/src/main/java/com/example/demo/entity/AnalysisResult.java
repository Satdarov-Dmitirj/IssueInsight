package com.example.demo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "analysis",
        indexes = {
                @Index(name = "idx_analysis_ticket", columnList = "ticket_id"),
                @Index(name = "idx_analysis_method", columnList = "analise_method")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Результат анализа тикета")
public class AnalysisResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column(nullable = false)
    private String detectedCause;

    @Column(nullable = false)
    private String causeDescription;

    @Column(nullable = false)
    private double analiseScore;

    @Column(nullable = false)
    private LocalDateTime analiseDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "analise_method", nullable = false)
    private AnaliseMethod analiseMethod;
}
