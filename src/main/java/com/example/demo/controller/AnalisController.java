package com.example.demo.controller;

import com.example.demo.dto.AnalisResultRespouns;
import com.example.demo.repository.TicketServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
@Tag(name = "Analysis Controller")
public class AnalisController {

    private final TicketServiceInterface ticketService;

    @Operation(summary = "Получить результаты анализа тикета по id")
    @GetMapping("/ticket/id")
    public List<AnalisResultRespouns> getTicketAnalysis(@PathVariable Long id) {
        return ticketService.getAnalysisResults(id);
    }
}
