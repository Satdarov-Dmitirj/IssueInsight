package com.example.demo.service;

import com.example.demo.repository.AnalisRepository;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class AnalisService {
    static final Logger logger = LoggerFactory.getLogger(AnalisService.class);
    final AnalisRepository analisRepository;
    public AnalisService(AnalisRepository analisRepository){
        this.analisRepository = analisRepository;
    }

    static{
        Map<String, Double> authCeyWrods = new HashMap<>();
        authCeyWrods.put("Не могу войти", 2.0);
        causeCeyWord.put("Пробелы с авторизацией", authCeyWrods);
    }

    private static final Map <String, Map<String, Double>> causeCeyWord = new HashMap<>();

}
