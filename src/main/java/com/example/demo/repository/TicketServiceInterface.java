package com.example.demo.repository;

import com.example.demo.dto.AnalisResultRespouns;
import com.example.demo.entity.AnalysisResult;
import com.example.demo.entity.Category;
import com.example.demo.entity.Ticket;
import com.example.demo.entity.TicketStatus;

import java.util.List;
import java.util.Optional;

public interface TicketServiceInterface {

    Ticket createTicket(Ticket ticket);

    Optional<Ticket> getTicketById(Long id);

    List<Ticket> getAllTickets();

    Ticket updateTicket(Long id, Ticket updatedTicket);

    void deleteTicket(Long id);

    Ticket changeTicketStatus(Long id, TicketStatus status);

    Ticket closeTicket(Long id, String resolution);

    List<Ticket> getTicketsByCategory(Category category);

    List<Ticket> getTicketsByStatus(TicketStatus status);

    AnalysisResult analyzeTicket(Long ticketId);

    List<AnalisResultRespouns> getAnalysisResults(Long ticketId);
}
