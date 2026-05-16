package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@Schema(description = "DTO для создания/обновления ключевого слова")
public class KeywordWeightRequest {

    @Schema(description = "Название категории", example = "Проблемы с авторизацией", required = true)
    @NotBlank(message = "Название категории обязательно")
    private String categoryName;

    @Schema(description = "Ключевое слово", example = "логин", required = true)
    @NotBlank(message = "Ключевое слово обязательно")
    private String keyword;

    @Schema(description = "Вес ключевого слова (больше 0)", example = "10.0", required = true)
    @NotNull(message = "Вес обязателен")
    @Positive(message = "Вес должен быть положительным числом")
    private Double weight;
}