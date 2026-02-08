package com.example.demo.dto;

import com.example.demo.entity.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TicketDto {

    private Long id;
    private String subject;
    private String description;
    private String customerEmail;
    private TicketStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;
    private String categoryName;
}