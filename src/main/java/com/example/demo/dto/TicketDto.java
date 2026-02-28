package com.example.demo.dto;

import com.example.demo.entity.TicketStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(description = "DTO тикета")
public class TicketDto {

    @Schema(description = "ID тикета", example = "24")
    private Long id;

    @Schema(description = "Тема обращения", example = "Проблемы с оплатой")
    private String subject;

    @Schema(description = "Описание проблемы", example = "Клиент не может завершить оплату")
    private String description;

    @Schema(description = "Email клиента", example = "customer@mail.com")
    private String customerEmail;

    @Schema(description = "Статус тикета", example = "OPEN")
    private TicketStatus status;

    @Schema(description = "Дата создания тикета", example = "2026-02-13T17:29:09")
    private LocalDateTime createdAt;

    @Schema(description = "Дата закрытия тикета", example = "2026-02-13T18:00:00", nullable = true)
    private LocalDateTime resolvedAt;

    @Schema(description = "Название категории", example = "Техническая проблема", nullable = true)
    private String categoryName;
}
