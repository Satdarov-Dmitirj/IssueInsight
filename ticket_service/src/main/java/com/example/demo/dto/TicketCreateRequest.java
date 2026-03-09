package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO для создания тикета")
public class TicketCreateRequest {

    @Schema(description = "Тема обращения", example = "Проблемы с оплатой")
    private String subject;

    @Schema(description = "Описание проблемы", example = "Клиент не может завершить оплату")
    private String description;

    @Schema(description = "Email клиента", example = "customer@mail.com")
    private String email;
}
