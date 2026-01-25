package com.example.demo.service;

import com.example.demo.dto.AnalisResultRespouns;
import com.example.demo.entity.*;
import com.example.demo.repository.AnalisRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final CategoryRepository categoryRepository;
    private final AnalisRepository analisRepository;
    private final AnalisService analisService;

    public Ticket createTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket updateTicket(Long id, Ticket updatedTicket) {
        return ticketRepository.findById(id)
                .map(ticket -> {
                    ticket.setSubject(updatedTicket.getSubject());
                    ticket.setDescription(updatedTicket.getDescription());
                    ticket.setCustomerEmail(updatedTicket.getCustomerEmail());
                    ticket.setCategory(updatedTicket.getCategory());
                    ticket.setTicketStatus(updatedTicket.getTicketStatus());
                    return ticketRepository.save(ticket);
                })
                .orElseThrow(() -> new RuntimeException("Тикет с id " + id + " не найден"));
    }

    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    public Ticket changeTicketStatus(Long id, TicketStatus status) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Тикет с id " + id + " не найден"));

        ticket.setTicketStatus(status);

        if (status == TicketStatus.CLOSED) {
            ticket.setResolvedAt(java.time.LocalDateTime.now());
        }

        return ticketRepository.save(ticket);
    }

    public Ticket closeTicket(Long id, String resolution) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Тикет с id " + id + " не найден"));

        ticket.setTicketStatus(TicketStatus.CLOSED);
        ticket.setResolvedAt(java.time.LocalDateTime.now());
        ticket.setDescription(ticket.getDescription() + "\n\nРешение: " + resolution);

        return ticketRepository.save(ticket);
    }

    public List<Ticket> getTicketsByCategory(Category category) {
        return ticketRepository.findAll()
                .stream()
                .filter(t -> t.getCategory() != null && t.getCategory().equals(category))
                .toList();
    }

    public List<Ticket> getTicketsByStatus(TicketStatus status) {
        return ticketRepository.findAll()
                .stream()
                .filter(t -> t.getTicketStatus() == status)
                .toList();
    }

    public AnalysisResult analyzeTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Тикет с id " + ticketId + " не найден"));
        return analisService.analysisTicket(ticket);
    }

    public List<AnalisResultRespouns> getAnalysisResults(Long ticketId) {
        return analisRepository.findByTicketId(ticketId)
                .stream()
                .map(AnalisResultRespouns::fromEntity)
                .toList();
    }
}
