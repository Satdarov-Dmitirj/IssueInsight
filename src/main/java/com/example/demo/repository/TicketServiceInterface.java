package com.example.demo.repository;

import com.example.demo.dto.AnalisResultRespouns;
import com.example.demo.dto.TicketCreateRequest;
import com.example.demo.dto.TicketDto;
import com.example.demo.entity.TicketStatus;

import java.util.List;
import java.util.Optional;

public interface TicketServiceInterface {

    TicketDto createTicket(TicketCreateRequest ticketCreateRequest);

    Optional<TicketDto> getTicketById(Long id);

    List<TicketDto> getAllTickets();

    TicketDto changeTicketStatus(Long id, TicketStatus status);

    TicketDto closeTicket(Long id, String resolution);

    List<TicketDto> getTicketsByCategory(Long categoryId);

    List<TicketDto> getTicketsByStatus(TicketStatus status);

    List<AnalisResultRespouns> getAnalysisResults(Long ticketId);
}
