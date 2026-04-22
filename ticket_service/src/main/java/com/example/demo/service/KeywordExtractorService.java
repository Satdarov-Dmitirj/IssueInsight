package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class KeywordExtractorService {

    private static final int MIN_WORD_LENGTH = 3;

    private static final Set<String> STOP_WORDS = Set.of(
            "без", "во", "для", "до", "за", "из", "ко",
            "меж", "над", "обо", "от", "перед",
            "под", "при", "про", "со", "через",
            "а", "и", "в", "к", "у", "о", "с", "на", "по", "об",
            "но", "или", "либо", "да", "же", "ли", "бы", "не", "ни",
            "то", "как", "что", "чтобы", "когда", "если", "хотя",
            "однако", "зато", "причём", "притом",
            "вот", "вон", "уже", "ещё", "тоже", "также", "очень", "так",
            "здесь", "там", "тут", "где", "куда", "откуда",
            "я", "ты", "он", "она", "оно", "мы", "вы", "они",
            "мне", "мой", "моя", "моё", "мои", "твой", "свой",
            "этот", "эта", "это", "эти", "тот", "та", "те",
            "всё", "все", "весь", "вся", "сам", "сама", "само",
            "который", "которая", "которое", "которые",
            "есть", "был", "была", "были", "будет", "быть",
            "стал", "стала", "стало", "стали",
            "нет", "привет", "здравствуйте", "пожалуйста",
            "спасибо", "хорошо", "плохо", "можно", "нужно",
            "надо", "хочу", "хочет", "могу", "может"
    );

    public List<String> extract(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        return Arrays.stream(
                        text.toLowerCase()
                                .replaceAll("[^а-яёa-z\\s]", " ")
                                .split("\\s+")
                )
                .map(String::trim)
                .filter(w -> w.length() >= MIN_WORD_LENGTH)
                .filter(w -> !STOP_WORDS.contains(w))
                .filter(w -> w.matches("[а-яёa-z]+"))
                .distinct()
                .collect(Collectors.toList());
    }
}