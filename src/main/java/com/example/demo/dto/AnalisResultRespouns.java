package com.example.demo.dto;

import com.example.demo.entity.AnaliseMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalisResultRespouns {

    private Long id;
    private String detectedCause;
    private String causeDescription;
    private double analiseScore;
    private LocalDateTime analiseDate;
    private AnaliseMethod analiseMethod;

}
