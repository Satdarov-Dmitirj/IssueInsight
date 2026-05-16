package com.example.demo.repository;

import com.example.demo.entity.Ticket;
import com.example.demo.entity.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByCategoryId(Long categoryId);

    List<Ticket> findByTicketStatus(TicketStatus status);
}
