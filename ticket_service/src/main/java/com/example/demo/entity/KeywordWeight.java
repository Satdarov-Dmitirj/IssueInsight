
package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "keyword_weights")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeywordWeight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String categoryName;

    @Column(nullable = false)
    private String keyword;

    @Column(nullable = false)
    private Double weight;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
}