package com.example.demo.controller;

import com.example.demo.dto.CategoryCreateRequest;
import com.example.demo.dto.CategoryDto;
import com.example.demo.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Category Controller", description = "Управление категориями")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Получить все категории")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список категорий получен"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @GetMapping
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAll();
    }

    @Operation(summary = "Создать категорию")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Категория создана"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    @PostMapping
    public CategoryDto createCategory(
            @RequestBody
            @Parameter(description = "Данные для создания категории")
            CategoryCreateRequest request) {

        return categoryService.create(request);
    }

    @Operation(summary = "Получить категорию по id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Категория найдена"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена")
    })
    @GetMapping("/by-id")
    public CategoryDto getCategory(
            @Parameter(description = "ID категории", example = "1")
            @RequestParam Long id) {

        return categoryService.getById(id);
    }
}
