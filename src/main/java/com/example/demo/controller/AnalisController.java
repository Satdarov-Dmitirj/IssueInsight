package com.example.demo.controller;

import com.example.demo.dto.AnalisResultRespouns;
import com.example.demo.repository.TicketServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
@Tag(name = "Analysis Controller", description = "Анализ тикетов")
public class AnalisController {

    private final TicketServiceInterface ticketService;

    @Operation(summary = "Получить результаты анализа тикета")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Результаты анализа получены"),
            @ApiResponse(responseCode = "404", description = "Анализ не найден")
    })
    @GetMapping("/by-ticket")
    public List<AnalisResultRespouns> getTicketAnalysis(
            @Parameter(description = "ID тикета", example = "1")
            @RequestParam Long ticketId) {

        return ticketService.getAnalysisResults(ticketId);
    }
}
