package com.example.demo.controller;

import com.example.demo.dto.AnalisResultRespouns;
import com.example.demo.repository.TicketServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tickets")
@RequiredArgsConstructor
public class AnalisController {

    private final TicketServiceInterface ticketService;

    @GetMapping("id.analysis")
    public List<AnalisResultRespouns> getTicketAnalysis(@PathVariable Long id) {
        return ticketService.getAnalysisResults(id);
    }
}
