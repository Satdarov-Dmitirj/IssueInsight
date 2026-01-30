package com.example.demo.repository;

import com.example.demo.entity.AnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnalisRepository extends JpaRepository<AnalysisResult, Long> {
    List<AnalysisResult> findByTicketId(Long ticketId);
}
