package com.example.demo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets",
        indexes = {
                @Index(name = "idx_ticket_status", columnList = "ticket_status"),
                @Index(name = "idx_ticket_category", columnList = "category_id")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность тикета поддержки")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID тикета", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Тема обращения")
    private String subject;

    @Column(nullable = false)
    @Schema(description = "Описание проблемы")
    private String description;

    @Column(nullable = false)
    @Schema(description = "Email клиента")
    private String customerEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_status", nullable = false)
    @Schema(description = "Статус тикета")
    private TicketStatus ticketStatus = TicketStatus.OPEN;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime resolvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Version
    private Long version;
}
