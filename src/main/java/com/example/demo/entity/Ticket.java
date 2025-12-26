package com.example.demo.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String subject;
    @Column
    private String description;
    @Column
    private String custamerEmail;

    TicketStatus ticketStatus =  TicketStatus.OPEN;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;
    @JoinColumn(name = "category_id")
    @ManyToOne()
    private Category category;
    private Long version;
}
