package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeywordExtractorService {

    private static final int MIN_WORD_LENGTH = 3;

    private final StopWordService stopWordService;

    public List<String> extract(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        Set<String> stopWords = stopWordService.getAllWords();

        return Arrays.stream(
                        text.toLowerCase()
                                .replaceAll("[^а-яёa-z\\s]", " ")
                                .split("\\s+")
                )
                .map(String::trim)
                .filter(w -> w.length() >= MIN_WORD_LENGTH)
                .filter(w -> !stopWords.contains(w))
                .filter(w -> w.matches("[а-яёa-z]+"))
                .distinct()
                .collect(Collectors.toList());
    }
}
