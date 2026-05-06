package com.example.demo.controller;

import com.example.demo.entity.StopWord;
import com.example.demo.service.StopWordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stop-words")
@RequiredArgsConstructor
@Tag(name = "Stop Words Controller", description = "Управление стоп-словами")
public class StopWordController {

    private final StopWordService stopWordService;

    @Operation(summary = "Получить все стоп-слова")
    @ApiResponse(responseCode = "200", description = "Список получен")
    @GetMapping
    public ResponseEntity<List<StopWord>> getAll() {
        return ResponseEntity.ok(stopWordService.getAll());
    }

    @Operation(summary = "Добавить стоп-слово")
    @ApiResponse(responseCode = "201", description = "Стоп-слово добавлено")
    @ApiResponse(responseCode = "400", description = "Слово уже существует")
    @PostMapping
    public ResponseEntity<StopWord> add(
            @Parameter(description = "Стоп-слово", example = "пожалуйста")
            @RequestParam String word) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stopWordService.add(word));
    }

    @Operation(summary = "Удалить стоп-слово по ID")
    @ApiResponse(responseCode = "204", description = "Удалено")
    @ApiResponse(responseCode = "404", description = "Не найдено")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID стоп-слова", example = "1")
            @PathVariable Long id) {
        stopWordService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
