package com.example.demo.service;

import com.example.demo.dto.AnalisResultRespouns;
import com.example.demo.dto.TicketCreateRequest;
import com.example.demo.dto.TicketDto;
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
    public TicketDto createTicket(TicketCreateRequest request) {
        Ticket ticket = new Ticket();
        ticket.setSubject(request.getSubject());
        ticket.setDescription(request.getDescription());
        ticket.setCustomerEmail(request.getEmail());
        ticket.setTicketStatus(TicketStatus.OPEN);

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


        Ticket finalTicket = ticketRepository.save(savedTicket);

        return toDto(finalTicket);
    }


    @Override
    public Optional<TicketDto> getTicketById(Long id) {
        return ticketRepository.findById(id)
                .map(this::toDto);
    }

    @Override
    public List<TicketDto> getAllTickets() {
        return ticketRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }


    @Override
    public TicketDto changeTicketStatus(Long id, TicketStatus status) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Тикет с id " + id + " не найден"));

        ticket.setTicketStatus(status);

        if (status == TicketStatus.CLOSED) {
            ticket.setResolvedAt(LocalDateTime.now());
        }

        return toDto(ticketRepository.save(ticket));
    }

    @Override
    public TicketDto closeTicket(Long id, String resolution) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Тикет с id " + id + " не найден"));

        ticket.setTicketStatus(TicketStatus.CLOSED);
        ticket.setResolvedAt(LocalDateTime.now());
        ticket.setDescription(ticket.getDescription() + "Решение: " + resolution);

        return toDto(ticketRepository.save(ticket));
    }


    @Override
    public List<TicketDto> getTicketsByCategory(Long categoryId) {
        return ticketRepository.findAll()
                .stream()
                .filter(t -> t.getCategory() != null && t.getCategory().getId().equals(categoryId))
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<TicketDto> getTicketsByStatus(TicketStatus status) {
        return ticketRepository.findAll()
                .stream()
                .filter(t -> t.getTicketStatus() == status)
                .map(this::toDto)
                .toList();
    }


    @Override
    public List<AnalisResultRespouns> getAnalysisResults(Long ticketId) {
        return analisRepository.findByTicketId(ticketId)
                .stream()
                .map(AnalysisResultMapper.INSTANCE::toDto)
                .toList();
    }


    private TicketDto toDto(Ticket ticket) {
        return new TicketDto(
                ticket.getId(),
                ticket.getSubject(),
                ticket.getDescription(),
                ticket.getCustomerEmail(),
                ticket.getTicketStatus(),
                ticket.getCreatedAt(),
                ticket.getResolvedAt(),
                ticket.getCategory() != null ? ticket.getCategory().getName() : null
        );
    }
}
