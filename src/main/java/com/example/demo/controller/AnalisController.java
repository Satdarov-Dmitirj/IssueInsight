package com.example.demo.controller;

import com.example.demo.dto.AnalisResultRespouns;
import com.example.demo.service.AnalisService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tickets")
public class AnalisController {

    private final AnalisService analisService;

    public AnalisController(AnalisService analisService) {
        this.analisService = analisService;
    }

    @GetMapping("/{id}/analysis")
    public List<AnalisResultRespouns> getAllAnalysisTicket(@PathVariable Long id) {
        return analisService.getTicketAnalysisById(id)
                .stream()
                .map(AnalisResultRespouns::fromEntity)
                .collect(Collectors.toList());
    }
}
