package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO ключевого слова")
public class KeywordWeightDto {

    @Schema(description = "ID ключевого слова", example = "1")
    private Long id;

    @Schema(description = "Название категории", example = "Проблемы с авторизацией")
    private String categoryName;

    @Schema(description = "Ключевое слово", example = "логин")
    private String keyword;

    @Schema(description = "Вес ключевого слова", example = "10.0")
    private Double weight;

    @Schema(description = "Дата создания", example = "2026-04-10T12:00:00")
    private LocalDateTime createdAt;
}