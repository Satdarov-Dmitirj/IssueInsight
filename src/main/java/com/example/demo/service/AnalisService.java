package com.example.demo.service;

import com.example.demo.dto.TicketCreateRequest;
import com.example.demo.entity.AnaliseMethod;
import com.example.demo.entity.AnalysisResult;
import com.example.demo.entity.Ticket;
import com.example.demo.repository.AnalisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalisService {

    private static final Logger logger = LoggerFactory.getLogger(AnalisService.class);

    private final AnalisRepository analisRepository;

    private static final Map<String, Map<String, Double>> CAUSE_KEYWORDS = new HashMap<>();

    public AnalisService(AnalisRepository analisRepository) {
        this.analisRepository = analisRepository;
    }

    static {
        CAUSE_KEYWORDS.put("Проблемы с авторизацией", Map.of(
                "не могу войти", 2.0,
                "ошибка входа", 1.5,
                "не пускает в аккаунт", 1.8
        ));

        CAUSE_KEYWORDS.put("Проблемы с оплатой", Map.of(
                "не проходит платеж", 2.0,
                "ошибка оплаты", 1.7,
                "списали деньги", 1.3
        ));
    }

    public AnalysisResult analysisTicket(Ticket ticketCreateRequest) {

        String text = (ticketCreateRequest.getSubject() + " " + ticketCreateRequest.getDescription()).toLowerCase();
        logger.debug("Analyzing ticket", ticketCreateRequest.getId());

        Map<String, Double> causeScores = new HashMap<>();

        for (Map.Entry<String, Map<String, Double>> entry : CAUSE_KEYWORDS.entrySet()) {

            String cause = entry.getKey();
            Map<String, Double> keywords = entry.getValue();

            double score = 0;
            double maxScore = keywords.values().stream().mapToDouble(Double::doubleValue).sum();

            for (Map.Entry<String, Double> keywordEntry : keywords.entrySet()) {
                if (text.contains(keywordEntry.getKey())) {
                    score += keywordEntry.getValue();
                }
            }

            if (score > 0) {
                causeScores.put(cause, (score / maxScore) * 100);
            }
        }

        String detectedCause;
        double confidence;
        String description;

        if (causeScores.isEmpty()) {
            detectedCause = "Другое";
            confidence = 10;
            description = "Автоматически не удалось определить причину. Требуется ручной анализ.";
        } else {
            Map.Entry<String, Double> best = causeScores.entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue())
                    .orElseThrow();

            detectedCause = best.getKey();
            confidence = Math.min(best.getValue(), 100);
            description = "Определена причина: " + detectedCause + " (уверенность " + confidence + "%)";
        }

        AnalysisResult result = new AnalysisResult();
        result.setTicket(ticketCreateRequest);
        result.setDetectedCause(detectedCause);
        result.setCauseDescription(description);
        result.setAnaliseScore(confidence);
        result.setAnaliseDate(LocalDateTime.now());
        result.setAnaliseMethod(AnaliseMethod.AUTOMATIC);

        return analisRepository.save(result);
    }

    public Page<AnalysisResult> getAllTickets(Pageable pageable) {
        return analisRepository.findAll(pageable);
    }

    public List<AnalysisResult> getTicketAnalysisById(Long id) {
        return analisRepository.findByTicketId(id);
    }

    public AnalysisResult saveResult(AnalysisResult analysisResult){
        return analisRepository.save(analysisResult);
    }
}
