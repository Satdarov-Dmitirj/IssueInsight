package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "analysis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column(nullable = false, length = 255)
    private String detectedCause;

    @Column(nullable = false, length = 500)
    private String causeDescription;

    @Column(nullable = false)
    private double analiseScore;

    @Column(nullable = false)
    private LocalDateTime analiseDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AnaliseMethod analiseMethod;
}
