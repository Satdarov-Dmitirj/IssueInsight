package com.example.demo.service;

import com.example.demo.dto.AnalisResultRespouns;
import com.example.demo.dto.TicketCreateRequest;
import com.example.demo.entity.*;
import com.example.demo.mapper.AnalysisResultMapper;
import com.example.demo.repository.AnalisRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.TicketRepository;
import com.example.demo.repository.TicketServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketService implements TicketServiceInterface {

    private final TicketRepository ticketRepository;
    private final CategoryRepository categoryRepository;
    private final AnalisRepository analisRepository;
    private final AnalisService analisService;

    @Override
    public Ticket createTicket(TicketCreateRequest ticketCreateRequest) {
        Ticket ticket = new Ticket();
        ticket.setSubject(ticketCreateRequest.getSubject());
        ticket.setCustomerEmail(ticketCreateRequest.getEmail());
        ticket.setDescription(ticketCreateRequest.getDescription());
        ticket.setTicketStatus(TicketStatus.OPEN);
        ticket.setCreatedAt(LocalDateTime.now());

        Ticket savedTicket = ticketRepository.save(ticket);

        AnalysisResult analysisResult = analisService.analysisTicket(savedTicket);

        Category category = categoryRepository
                .findByName(analysisResult.getDetectedCause())
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(analysisResult.getDetectedCause());
                    newCategory.setDescription("Автоматически созданная категория");
                    return categoryRepository.save(newCategory);
                });

        savedTicket.setCategory(category);

        return ticketRepository.save(savedTicket);
    }

    @Override
    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Override
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

    @Override
    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    @Override
    public Ticket changeTicketStatus(Long id, TicketStatus status) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Тикет с id " + id + " не найден"));

        ticket.setTicketStatus(status);

        if (status == TicketStatus.CLOSED) {
            ticket.setResolvedAt(LocalDateTime.now());
        }

        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket closeTicket(Long id, String resolution) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Тикет с id " + id + " не найден"));

        ticket.setTicketStatus(TicketStatus.CLOSED);
        ticket.setResolvedAt(LocalDateTime.now());
        ticket.setDescription(ticket.getDescription() + "Решение: " + resolution);

        return ticketRepository.save(ticket);
    }

    @Override
    public List<Ticket> getTicketsByCategory(Category category) {
        return ticketRepository.findAll()
                .stream()
                .filter(t -> t.getCategory() != null && t.getCategory().equals(category))
                .toList();
    }

    @Override
    public List<Ticket> getTicketsByStatus(TicketStatus status) {
        return ticketRepository.findAll()
                .stream()
                .filter(t -> t.getTicketStatus() == status)
                .toList();
    }

    @Override
    public AnalysisResult analyzeTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Тикет с id " + ticketId + " не найден"));
        return analisService.analysisTicket(ticket);
    }

    @Override
    public List<AnalisResultRespouns> getAnalysisResults(Long ticketId) {
        return analisRepository.findByTicketId(ticketId)
                .stream()
                .map(AnalysisResultMapper.INSTANCE::toDto)
                .toList();
    }
}
