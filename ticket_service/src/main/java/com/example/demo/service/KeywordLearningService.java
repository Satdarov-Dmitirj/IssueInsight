package com.example.demo.service;

import com.example.demo.entity.KeywordWeight;
import com.example.demo.repository.KeywordWeightRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeywordLearningService {

    private static final Logger logger = LoggerFactory.getLogger(KeywordLearningService.class);

    private static final double BASE_WEIGHT = 1.0;

    private static final double WEIGHT_INCREMENT = 0.5;

    private static final double MAX_WEIGHT = 20.0;

    private static final double MIN_CONFIDENCE_TO_LEARN = 40.0;

    private final KeywordWeightRepository keywordWeightRepository;
    private final KeywordExtractorService extractor;

    @Transactional
    public void learnFromTicket(String text, String categoryName, double confidence) {
        if ("Другое".equals(categoryName)) {
            logger.debug("Пропуск обучения: категория 'Другое'");
            return;
        }

        if (confidence < MIN_CONFIDENCE_TO_LEARN) {
            logger.debug("Пропуск обучения: уверенность {}% ниже порога {}%",
                    confidence, MIN_CONFIDENCE_TO_LEARN);
            return;
        }

        List<String> words = extractor.extract(text);

        if (words.isEmpty()) {
            logger.debug("Пропуск обучения: не извлечено ни одного слова из текста");
            return;
        }

        int created = 0;
        int updated = 0;

        for (String word : words) {
            boolean wasUpdated = updateOrCreateKeyword(categoryName, word);
            if (wasUpdated) {
                updated++;
            } else {
                created++;
            }
        }

        logger.info("Обучение завершено для категории '{}': добавлено новых слов — {}, усилено существующих — {}",
                categoryName, created, updated);
    }

    private boolean updateOrCreateKeyword(String categoryName, String word) {
        return keywordWeightRepository
                .findByCategoryNameAndKeyword(categoryName, word)
                .map(existing -> {
                    double newWeight = Math.min(existing.getWeight() + WEIGHT_INCREMENT, MAX_WEIGHT);
                    existing.setWeight(newWeight);
                    keywordWeightRepository.save(existing);
                    logger.debug("Усилено слово '{}' в категории '{}': {} → {}",
                            word, categoryName, existing.getWeight(), newWeight);
                    return true;
                })
                .orElseGet(() -> {
                    KeywordWeight newKeyword = new KeywordWeight();
                    newKeyword.setCategoryName(categoryName);
                    newKeyword.setKeyword(word);
                    newKeyword.setWeight(BASE_WEIGHT);
                    newKeyword.setCreatedAt(LocalDateTime.now());
                    keywordWeightRepository.save(newKeyword);
                    logger.debug("Добавлено новое слово '{}' в категорию '{}' с весом {}",
                            word, categoryName, BASE_WEIGHT);
                    return false;
                });
    }
}