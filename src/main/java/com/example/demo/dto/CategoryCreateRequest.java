package com.example.demo.dto;

import lombok.Data;

@Data
public class CategoryCreateRequest {
    private String name;
    private String description;
}