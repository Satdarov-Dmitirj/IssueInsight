package com.example.demo.dto;

import lombok.Data;

@Data
public class TicketCreateRequest {
    private String subject;
    private String description;
    private String email;
}
