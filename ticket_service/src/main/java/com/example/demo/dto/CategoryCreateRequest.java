package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO для создания категории")
public class CategoryCreateRequest {

    @Schema(description = "Название категории", example = "Техническая проблема")
    private String name;

    @Schema(description = "Описание категории", example = "Категория для технических проблем")
    private String description;
}
