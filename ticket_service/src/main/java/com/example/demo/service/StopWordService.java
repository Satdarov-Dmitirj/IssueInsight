package com.example.demo.service;

import com.example.demo.entity.StopWord;
import com.example.demo.repository.StopWordRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StopWordService {

    private static final Logger logger = LoggerFactory.getLogger(StopWordService.class);

    private final StopWordRepository stopWordRepository;

    @Cacheable("stopWords")
    public Set<String> getAllWords() {
        logger.debug("Загрузка стоп-слов из БД");
        return stopWordRepository.findAllWords();
    }

    public List<StopWord> getAll() {
        return stopWordRepository.findAll();
    }

    @Transactional
    @CacheEvict(value = "stopWords", allEntries = true)
    public StopWord add(String word) {
        String normalized = word.toLowerCase().trim();

        if (stopWordRepository.existsByWord(normalized)) {
            throw new RuntimeException("Стоп-слово уже существует: " + normalized);
        }

        StopWord entity = new StopWord();
        entity.setWord(normalized);
        StopWord saved = stopWordRepository.save(entity);
        logger.info("Добавлено стоп-слово: '{}'", normalized);
        return saved;
    }

    @Transactional
    @CacheEvict(value = "stopWords", allEntries = true)
    public void delete(Long id) {
        if (!stopWordRepository.existsById(id)) {
            throw new RuntimeException("Стоп-слово не найдено с id: " + id);
        }
        stopWordRepository.deleteById(id);
        logger.info("Удалено стоп-слово с id: {}", id);
    }
}