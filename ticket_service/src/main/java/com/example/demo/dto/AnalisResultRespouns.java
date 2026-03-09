package com.example.demo.dto;

import com.example.demo.entity.AnaliseMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Результат анализа тикета")
public class AnalisResultRespouns {

    @Schema(description = "ID анализа", example = "55")
    private Long id;

    @Schema(description = "Определённая причина", example = "Проблемы с авторизацией")
    private String detectedCause;

    @Schema(description = "Описание причины", example = "Не удалось войти в аккаунт")
    private String causeDescription;

    @Schema(description = "Уверенность анализа (%)", example = "75.5")
    private double analiseScore;

    @Schema(description = "Дата анализа", example = "2026-02-08T12:34:56")
    private LocalDateTime analiseDate;

    @Schema(description = "Метод анализа", example = "AUTOMATIC")
    private AnaliseMethod analiseMethod;
}
