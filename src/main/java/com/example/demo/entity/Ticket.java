package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String subject;
    @Column
    private String description;
    @Column
    private String customerEmail;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    TicketStatus ticketStatus =  TicketStatus.OPEN;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;

    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "fk_ticket_category"))
    @ManyToOne(fetch = FetchType.LAZY) //изучить
    private Category category;
    @Version
    private Long version;

}
