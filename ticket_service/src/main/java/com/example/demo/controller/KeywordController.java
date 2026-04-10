package com.example.demo.controller;

import com.example.demo.dto.KeywordWeightDto;
import com.example.demo.dto.KeywordWeightRequest;
import com.example.demo.service.KeywordWeightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/keywords")
@RequiredArgsConstructor
@Tag(name = "Keyword Controller", description = "Управление ключевыми словами для анализа")
public class KeywordController {

    private final KeywordWeightService keywordWeightService;

    @Operation(summary = "Получить все ключевые слова")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список ключевых слов получен"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<List<KeywordWeightDto>> getAllKeywords() {
        List<KeywordWeightDto> keywords = keywordWeightService.getAll();
        return ResponseEntity.ok(keywords);
    }

    @Operation(summary = "Получить ключевое слово по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ключевое слово найдено"),
            @ApiResponse(responseCode = "404", description = "Ключевое слово не найдено")
    })
    @GetMapping("id")
    public ResponseEntity<KeywordWeightDto> getKeywordById(
            @Parameter(description = "ID ключевого слова", example = "1")
            @PathVariable Long id) {

        KeywordWeightDto keyword = keywordWeightService.findById(id);
        return ResponseEntity.ok(keyword);
    }

    @Operation(summary = "Получить ключевые слова по категории")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список ключевых слов получен"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена")
    })
    @GetMapping("by-category")
    public ResponseEntity<List<KeywordWeightDto>> getKeywordsByCategory(
            @Parameter(description = "Название категории", example = "Проблемы с авторизацией")
            @RequestParam String categoryName) {

        List<KeywordWeightDto> keywords = keywordWeightService.getByCategory(categoryName);
        return ResponseEntity.ok(keywords);
    }

    @Operation(summary = "Создать новое ключевое слово")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ключевое слово создано"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    @PostMapping
    public ResponseEntity<KeywordWeightDto> createKeyword(
            @Valid @RequestBody KeywordWeightRequest request) {

        KeywordWeightDto created = keywordWeightService.createWeight(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Обновить ключевое слово")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ключевое слово обновлено"),
            @ApiResponse(responseCode = "404", description = "Ключевое слово не найдено"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    @PutMapping("id")
    public ResponseEntity<KeywordWeightDto> updateKeyword(
            @Parameter(description = "ID ключевого слова", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody KeywordWeightRequest request) {

        KeywordWeightDto updated = keywordWeightService.updateWeight(id, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Удалить ключевое слово")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Ключевое слово удалено"),
            @ApiResponse(responseCode = "404", description = "Ключевое слово не найдено")
    })
    @DeleteMapping("id")
    public ResponseEntity<Void> deleteKeyword(
            @Parameter(description = "ID ключевого слова", example = "1")
            @PathVariable Long id) {

        keywordWeightService.removeById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Удалить все ключевые слова категории")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Ключевые слова категории удалены"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена")
    })
    @DeleteMapping("by-category")
    public ResponseEntity<Void> deleteKeywordsByCategory(
            @Parameter(description = "Название категории", example = "Проблемы с авторизацией")
            @RequestParam String categoryName) {

        keywordWeightService.deleteByCategoryName(categoryName);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Массовое создание ключевых слов")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ключевые слова созданы"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    @PostMapping("bulk")
    public ResponseEntity<List<KeywordWeightDto>> createKeywordsBulk(
            @Valid @RequestBody List<KeywordWeightRequest> requests) {

        List<KeywordWeightDto> created = requests.stream()
                .map(keywordWeightService::createWeight)
                .toList();

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}