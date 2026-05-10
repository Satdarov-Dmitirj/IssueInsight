package com.example.demo.service;

import com.example.demo.entity.AnaliseMethod;
import com.example.demo.entity.AnalysisResult;
import com.example.demo.entity.KeywordWeight;
import com.example.demo.entity.Ticket;
import com.example.demo.repository.AnalisRepository;
import com.example.demo.repository.KeywordWeightRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AnalisService {

    private static final Logger logger = LoggerFactory.getLogger(AnalisService.class);

    private static final int TOP_N_FOR_NORMALIZATION = 5;

    private final AnalisRepository analisRepository;
    private final KeywordWeightRepository keywordWeightRepository;
    private final KeywordLearningService keywordLearningService;

    public AnalysisResult analysisTicket(Ticket ticket) {
        String text = (ticket.getSubject() + " " + ticket.getDescription()).toLowerCase();
        logger.debug("Анализ тикета id={}", ticket.getId());

        List<KeywordWeight> allKeywords = keywordWeightRepository.findAll();

        Map<String, List<KeywordWeight>> keywordsByCategory = new HashMap<>();
        for (KeywordWeight kw : allKeywords) {
            keywordsByCategory
                    .computeIfAbsent(kw.getCategoryName(), k -> new ArrayList<>())
                    .add(kw);
        }

        Map<String, Double> causeScores = new HashMap<>();

        for (Map.Entry<String, List<KeywordWeight>> entry : keywordsByCategory.entrySet()) {
            String category = entry.getKey();
            List<KeywordWeight> keywords = entry.getValue();

            double matchedScore = 0;
            int matchedCount = 0;
            for (KeywordWeight kw : keywords) {
                if (text.contains(kw.getKeyword().toLowerCase())) {
                    matchedScore += kw.getWeight();
                    matchedCount++;
                }
            }

            if (matchedScore > 0) {
                double topNScore = keywords.stream()
                        .mapToDouble(KeywordWeight::getWeight)
                        .boxed()
                        .sorted(Comparator.reverseOrder())
                        .limit(TOP_N_FOR_NORMALIZATION)
                        .mapToDouble(Double::doubleValue)
                        .sum();

                double confidence = (matchedScore / topNScore) * 100.0;

                double matchBonus = Math.min(matchedCount * 3.0, 15.0);
                confidence = Math.min(confidence + matchBonus, 100.0);

                causeScores.put(category, confidence);
            }
        }

        String detectedCause;
        double confidence;
        String description;

        if (causeScores.isEmpty()) {
            detectedCause = "Другое";
            confidence = 10.0;
            description = "Автоматически не удалось определить причину. Требуется ручной анализ.";
        } else {
            Map.Entry<String, Double> best = causeScores.entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue())
                    .orElseThrow(() -> new RuntimeException("Ошибка определения категории"));

            detectedCause = best.getKey();
            confidence = Math.min(best.getValue(), 100.0);
            description = String.format(
                    "Определена причина: %s (уверенность %.1f%%)", detectedCause, confidence
            );
        }

        logger.info("Тикет id={}: категория='{}', уверенность={}%",
                ticket.getId(), detectedCause, String.format("%.1f", confidence));

        AnalysisResult result = new AnalysisResult();
        result.setTicket(ticket);
        result.setDetectedCause(detectedCause);
        result.setCauseDescription(description);
        result.setAnaliseScore(confidence);
        result.setAnaliseDate(LocalDateTime.now());
        result.setAnaliseMethod(AnaliseMethod.AUTOMATIC);

        AnalysisResult savedResult = analisRepository.save(result);

        String ticketText = ticket.getSubject() + " " + ticket.getDescription();
        keywordLearningService.learnFromTicket(ticketText, detectedCause, confidence);

        return savedResult;
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
        keywordWeightRepository
                .findByCategoryNameAndKeyword(categoryName, keyword)
                .ifPresentOrElse(
                        existing -> {
                            existing.setWeight(weight);
                            keywordWeightRepository.save(existing);
                            logger.info("Обновлён вес слова '{}' в категории '{}': {}",
                                    keyword, categoryName, weight);
                        },
                        () -> {
                            KeywordWeight newWeight = new KeywordWeight();
                            newWeight.setCategoryName(categoryName);
                            newWeight.setKeyword(keyword);
                            newWeight.setWeight(weight);
                            newWeight.setCreatedAt(LocalDateTime.now());
                            keywordWeightRepository.save(newWeight);
                            logger.info("Добавлено слово '{}' в категорию '{}' с весом {}",
                                    keyword, categoryName, weight);
                        }
                );
    }
}