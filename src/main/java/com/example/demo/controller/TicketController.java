package com.example.demo.controller;

import com.example.demo.dto.TicketCreateRequest;
import com.example.demo.dto.TicketDto;
import com.example.demo.entity.TicketStatus;
import com.example.demo.repository.TicketServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketServiceInterface ticketService;

    @PostMapping
    public ResponseEntity<TicketDto> createTicket(
            @RequestBody TicketCreateRequest request) {

        TicketDto ticket = ticketService.createTicket(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }

    @GetMapping("/id")
    public TicketDto getTicket(@PathVariable Long id) {
        return ticketService.getTicketById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    @GetMapping
    public List<TicketDto> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @PutMapping("/id/status")
    public TicketDto changeStatus(
            @PathVariable Long id,
            @RequestParam TicketStatus status) {

        return ticketService.changeTicketStatus(id, status);
    }

    @PutMapping("/id/close")
    public TicketDto closeTicket(
            @PathVariable Long id,
            @RequestParam String resolution) {

        return ticketService.closeTicket(id, resolution);
    }

    @GetMapping("/by-status")
    public List<TicketDto> getByStatus(@RequestParam TicketStatus status) {
        return ticketService.getTicketsByStatus(status);
    }

    @GetMapping("/by-category/categoryId")
    public List<TicketDto> getByCategory(@PathVariable Long categoryId) {
        return ticketService.getTicketsByCategory(categoryId);
    }
}