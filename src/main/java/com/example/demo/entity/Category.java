package com.example.demo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "categories",
        indexes = {
                @Index(name = "idx_category_name", columnList = "name")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Категория тикетов")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID категории")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Название категории")
    private String name;

    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Ticket> tickets = new ArrayList<>();
}
