package com.example.demo.controller;

import com.example.demo.dto.TicketCreateRequest;
import com.example.demo.entity.Category;
import com.example.demo.entity.Ticket;
import com.example.demo.entity.TicketStatus;
import com.example.demo.repository.TicketServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketServiceInterface ticketService;

    @PostMapping("create")
    public ResponseEntity<Ticket> createTicket(@RequestBody TicketCreateRequest request) {
        try {
            Ticket createdTicket = ticketService.createTicket(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("getById")
    public ResponseEntity<Ticket> getTicket(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(
                    ticketService.getTicketById(id).orElseThrow()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("getAll")
    public ResponseEntity<List<Ticket>> getAllTickets() {
        try {
            return ResponseEntity.ok(ticketService.getAllTickets());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("update")
    public ResponseEntity<Ticket> updateTicket(@RequestParam Long id, @RequestBody Ticket updatedTicket) {
        try {
            return ResponseEntity.ok(ticketService.updateTicket(id, updatedTicket));
        } catch (Exception e) {return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("delete")
    public ResponseEntity<Void> deleteTicket(@RequestParam Long id) {
        try {
            ticketService.deleteTicket(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("changeStatus")
    public ResponseEntity<Ticket> changeTicketStatus(@RequestParam Long id,
                                                     @RequestParam TicketStatus status) {
        try {
            return ResponseEntity.ok(ticketService.changeTicketStatus(id, status));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("close")
    public ResponseEntity<Ticket> closeTicket(@RequestParam Long id, @RequestParam String resolution) {
        try {
            return ResponseEntity.ok(ticketService.closeTicket(id, resolution));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("byCategory")
    public ResponseEntity<List<Ticket>> getTicketsByCategory(@RequestParam Category category) {
        try {
            return ResponseEntity.ok(ticketService.getTicketsByCategory(category));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("byStatus")
    public ResponseEntity<List<Ticket>> getTicketsByStatus(@RequestParam TicketStatus status) {
        try {
            return ResponseEntity.ok(ticketService.getTicketsByStatus(status));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
