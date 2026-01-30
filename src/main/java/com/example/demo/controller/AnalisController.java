package com.example.demo.controller;

import com.example.demo.dto.AnalisResultRespouns;
import com.example.demo.service.TicketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tickets")
public class AnalisController {

    private final TicketService ticketService;

    public AnalisController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/{id}/analysis")
    public List<AnalisResultRespouns> getAllAnalysisTicket(@PathVariable Long id) {
        return ticketService.getAnalysisResults(id);
    }
}
