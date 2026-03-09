package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "DTO категории")
public class CategoryDto {

    @Schema(description = "ID категории", example = "1")
    private Long id;

    @Schema(description = "Название категории", example = "Техническая проблема")
    private String name;

    @Schema(description = "Описание категории", example = "Категория для технических проблем")
    private String description;
}
