package com.example.demo.controller;

import com.example.demo.dto.CategoryCreateRequest;
import com.example.demo.dto.CategoryDto;
import com.example.demo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAll();
    }

    @PostMapping
    public CategoryDto createCategory(@RequestBody CategoryCreateRequest request) {
        return categoryService.create(request);
    }

    @GetMapping("/id")
    public CategoryDto getCategory(@PathVariable Long id) {
        return categoryService.getById(id);
    }
}
