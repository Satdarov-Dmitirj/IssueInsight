package com.example.demo.service;

import com.example.demo.entity.AnalysisResult;
import com.example.demo.entity.Ticket;
import com.example.demo.repository.AnalisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service

public class AnalisService {
    static final Logger logger = LoggerFactory.getLogger(AnalisService.class);
    final AnalisRepository analisRepository;
    private static final Map<String, Map<String, Double>> CAUSE_KEYWORDS = new HashMap<>();

    public AnalisService(AnalisRepository analisRepository) {
        this.analisRepository = analisRepository;
    }

    static {
        CAUSE_KEYWORDS.put( //проблемы при авторизации
                "Проблемы с авторизацией",
                new HashMap<>(Map.of(
                        "Не могу войти", 2.0,
                        "Ошибка входа", 1.5,
                        "Не пускает в аккаунт", 1.8
                ))
        );

        CAUSE_KEYWORDS.put( // проблемы при оплате
                "Проблемы с оплатой",
                new HashMap<>(Map.of(
                        "Не проходит платеж", 2.0,
                        "Ошибка оплаты", 1.7,
                        "Списали деньги", 1.3
                ))
        );
    }

    public AnalysisResult analysisTicket(Ticket ticket){
        String text = (ticket.getSubject() + " " + ticket.getDescription()).toLowerCase();
        logger.debug("Analyzing ticket: {}", ticket.getSubject());

        Map<String, Double> causeScores = new HashMap<>();
        for (Map.Entry<String, Map<String, Double>> entry : CAUSE_KEYWORDS.entrySet()) {
            String cause = entry.getKey();
            Map<String, Double> keywords = entry.getValue();

            double score = 0.0;
            double maxPossibleScore = keywords.values().stream().mapToDouble(Double::doubleValue).sum();

            for (Map.Entry<String, Double> keywordEntry : keywords.entrySet()) {
                String keyword = keywordEntry.getKey();
                Double weight = keywordEntry.getValue();
                if (text.contains(" " + keyword.toLowerCase() + " ") ||
                        text.startsWith(keyword.toLowerCase() + " ") ||
                        text.endsWith(" " + keyword.toLowerCase())) {
                    score += weight * 1.2; // Бонус за точное совпадение
                } else if (text.contains(keyword.toLowerCase())) {
                    score += weight;
                }
            }

            if (score > 0) {
                double normalizedScore = (score / maxPossibleScore) * 100;
                causeScores.put(cause, normalizedScore);
            }
        }


        String detectedCause;
        double confidenceScore;
        String causeDescription;
        List<String> matchedKeywords = new ArrayList<>();

        if (causeScores.isEmpty()) {
            detectedCause = "Другое";
            confidenceScore = 10.0;
            causeDescription = "Не удалось автоматически определить причину обращения. Требуется ручной анализ.";
        } else {
            Map.Entry<String, Double> bestMatch = causeScores.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .orElseThrow();

            detectedCause = bestMatch.getKey();
            confidenceScore = Math.min(bestMatch.getValue(), 100.0);

            Map<String, Double> matchedKeywordsMap = CAUSE_KEYWORDS.get(detectedCause);
            for (String keyword : matchedKeywordsMap.keySet()) {
                if (text.contains(keyword.toLowerCase())) {
                    matchedKeywords.add(keyword);
                }
            }

            causeDescription = generateDescription(detectedCause, matchedKeywords, confidenceScore);
        }


        AnalysisResult analysisResult = new AnalysisResult(); // проверка

        analysisResult.setTicket(ticket);
        analysisResult.setDetectedCause(detectedCause);
        analysisResult.setCauseDescription(causeDescription);
        analysisResult.setAnaliseScore(confidenceScore);
        analysisResult.setAnaliseDate(LocalDateTime.now());
        analysisResult.setAnaliseMethod(analysisResult.getAnaliseMethod().AUTOMATIC);
        return  analysisResult;
    }

    private  String generateDescription(String cause, List <String> machedKeyWords, double confidenc){

        StringBuilder discripriprion = new StringBuilder();
        discripriprion.append("Автоматическое определение причины " + cause);

        if (confidenc >= 70){
            discripriprion.append("Высокая уверенность определения причины");

        } else if (confidenc >= 40) {
            discripriprion.append("Средняя уверенность. Рекомендуется дополнительная проверка");
        }else{
            discripriprion.append("Низкая уверенность");
        }

        if (machedKeyWords.isEmpty() && machedKeyWords.size() <= 5){
            discripriprion.append("Найдены ключевые слова");
            discripriprion.append(String.join(" , " + machedKeyWords));
            discripriprion.append(" . ");

        }

        return discripriprion.toString();
    }

    public List <AnalysisResult> getAllTickets(){
        return analisRepository.findAll();
    }
}
