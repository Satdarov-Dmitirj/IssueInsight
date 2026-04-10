package com.example.demo.service;

import com.example.demo.dto.KeywordWeightDto;
import com.example.demo.dto.KeywordWeightRequest;
import com.example.demo.entity.KeywordWeight;
import com.example.demo.mapper.KeywordWeightMapper;
import com.example.demo.repository.KeywordWeightRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeywordWeightService {

    private static final Logger logger = LoggerFactory.getLogger(KeywordWeightService.class);

    private final KeywordWeightRepository keywordWeightRepository;
    private final KeywordWeightMapper keywordWeightMapper;

    public List<KeywordWeightDto> getAll() {
        return keywordWeightRepository.findAll()
                .stream()
                .map(keywordWeightMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<KeywordWeightDto> getByCategory(String categoryName) {
        return keywordWeightRepository.findByCategoryName(categoryName)
                .stream()
                .map(keywordWeightMapper::toDto)
                .collect(Collectors.toList());
    }

    public KeywordWeightDto findById(Long id) {
        KeywordWeight entity = keywordWeightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ключевое слово не найдено с id: " + id));
        return keywordWeightMapper.toDto(entity);
    }

    @Transactional
    public KeywordWeightDto createWeight(KeywordWeightRequest request) {
        KeywordWeight entity = keywordWeightMapper.toEntity(request);
        KeywordWeight saved = keywordWeightRepository.save(entity);
        logger.info("Создано ключевое слово: {} для категории: {}", saved.getKeyword(), saved.getCategoryName());
        return keywordWeightMapper.toDto(saved);
    }

    @Transactional
    public void removeById(Long id) {
        if (!keywordWeightRepository.existsById(id)) {
            throw new RuntimeException("Ключевое слово не найдено с id: " + id);
        }
        keywordWeightRepository.deleteById(id);
        logger.info("Удалено ключевое слово с id: {}", id);
    }

    @Transactional
    public KeywordWeightDto updateWeight(Long id, KeywordWeightRequest request) {
        KeywordWeight existingWeight = keywordWeightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ключевое слово не найдено с id: " + id));

        keywordWeightMapper.updateEntityFromRequest(existingWeight, request);
        KeywordWeight updated = keywordWeightRepository.save(existingWeight);

        logger.info("Обновлено ключевое слово с id: {}", id);
        return keywordWeightMapper.toDto(updated);
    }

    @Transactional
    public void deleteByCategoryName(String categoryName) {
        List<KeywordWeight> keywords = keywordWeightRepository.findByCategoryName(categoryName);
        keywordWeightRepository.deleteAll(keywords);
        logger.info("Удалены все ключевые слова для категории: {}", categoryName);
    }
}