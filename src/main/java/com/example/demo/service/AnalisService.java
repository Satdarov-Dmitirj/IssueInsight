package com.example.demo.service;

import com.example.demo.entity.AnalysisResult;
import com.example.demo.entity.Ticket;
import com.example.demo.repository.AnalisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnalisService {
    static final Logger logger = LoggerFactory.getLogger(AnalisService.class);
    final AnalisRepository analisRepository;
    private static final Map<String, Map<String, Double>> causeCeyWord = new HashMap<>();

    public AnalisService(AnalisRepository analisRepository) {
        this.analisRepository = analisRepository;
    }

    static {
        causeCeyWord.put( //проблемы при авторизации
                "Проблемы с авторизацией",
                new HashMap<>(Map.of(
                        "Не могу войти", 2.0,
                        "Ошибка входа", 1.5,
                        "Не пускает в аккаунт", 1.8
                ))
        );

        causeCeyWord.put( // проблемы при оплате
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
        logger.debug("Analise text: {}", text);
        Map <String, Double> causeScore = new HashMap<>();
        for (Map.Entry<String, Map<String, Double>> entry: causeCeyWord.entrySet()){
            String cause = entry.getKey();
            Map<String, Double> keyWords = entry.getValue();
            double score = 0.0;
            double maxPosibleScore = keyWords.values().stream().mapToDouble(Double:: doubleValue).sum();

            for (Map.Entry<String, Double> keyWordEntry: keyWords.entrySet()){
                String keyWord = keyWordEntry.getKey();
                Double weight = keyWordEntry.getValue();
                if (text.contains(" " + keyWord.toLowerCase() + " ") ||
                    text.startsWith(keyWord.toLowerCase() + " ") ||
                    text.endsWith(" " + keyWord.toLowerCase())){
                    score += weight * 1.2;

                }else if (text.contains(keyWord.toLowerCase())) {
                    score += weight;
                }
            }
        }
    }
}
