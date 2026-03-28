
package com.example.demo.service;

import com.example.demo.entity.AnaliseMethod;
import com.example.demo.entity.AnalysisResult;
import com.example.demo.entity.KeywordWeight;
import com.example.demo.entity.Ticket;
import com.example.demo.repository.AnalisRepository;
import com.example.demo.repository.KeywordWeightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AnalisService {
    private static final Logger logger = LoggerFactory.getLogger(AnalisService.class);
    private final AnalisRepository analisRepository;
    private final KeywordWeightRepository keywordWeightRepository;

    @Autowired
    public AnalisService(AnalisRepository analisRepository, KeywordWeightRepository keywordWeightRepository) {
        this.analisRepository = analisRepository;
        this.keywordWeightRepository = keywordWeightRepository;
    }

    public AnalysisResult analysisTicket(Ticket ticketCreateRequest) {
        String text = (ticketCreateRequest.getSubject() + " " + ticketCreateRequest.getDescription()).toLowerCase();
        logger.debug("Analyzing ticket: {}", ticketCreateRequest.getId());

        List<KeywordWeight> allKeywords = keywordWeightRepository.findAll();

        Map<String, List<KeywordWeight>> keywordsByCategory = new HashMap<>();
        for (KeywordWeight kw : allKeywords) {
            keywordsByCategory.computeIfAbsent(kw.getCategoryName(), k -> new ArrayList<>()).add(kw);
        }

        Map<String, Double> causeScores = new HashMap<>();

        for (Map.Entry<String, List<KeywordWeight>> entry : keywordsByCategory.entrySet()) {
            String category = entry.getKey();
            List<KeywordWeight> keywords = entry.getValue();

            double score = 0;
            double maxPossibleScore = keywords.stream()
                    .mapToDouble(KeywordWeight::getWeight)
                    .sum();

            for (KeywordWeight keywordWeight : keywords) {
                String keyword = keywordWeight.getKeyword().toLowerCase();
                if (text.contains(keyword)) {
                    score += keywordWeight.getWeight();
                }
            }

            if (score > 0) {
                causeScores.put(category, (score / maxPossibleScore) * 100);
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
                    .orElseThrow(() -> new RuntimeException("No cause found"));

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

    public AnalysisResult saveResult(AnalysisResult analysisResult) {
        return analisRepository.save(analysisResult);
    }

    public void addOrUpdateKeywordWeight(String categoryName, String keyword, Double weight) {
        KeywordWeight existing = keywordWeightRepository.findByCategoryNameAndKeywordContainingIgnoreCase(categoryName, keyword)
                .stream()
                .findFirst()
                .orElse(null);

        if (existing != null) {
            existing.setWeight(weight);
            keywordWeightRepository.save(existing);
        } else {
            KeywordWeight newWeight = new KeywordWeight();
            newWeight.setCategoryName(categoryName);
            newWeight.setKeyword(keyword);
            newWeight.setWeight(weight);
            newWeight.setCreatedAt(LocalDateTime.now());
            keywordWeightRepository.save(newWeight);
        }
    }
}