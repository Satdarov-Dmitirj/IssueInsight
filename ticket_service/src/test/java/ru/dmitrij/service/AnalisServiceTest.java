package ru.dmitrij.service;

import com.example.demo.entity.AnaliseMethod;
import com.example.demo.entity.AnalysisResult;
import com.example.demo.entity.KeywordWeight;
import com.example.demo.entity.Ticket;
import com.example.demo.repository.AnalisRepository;
import com.example.demo.repository.KeywordWeightRepository;
import com.example.demo.service.AnalisService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalisServiceTest {

    @Mock
    private AnalisRepository analisRepository;

    @Mock
    private KeywordWeightRepository keywordWeightRepository;

    @InjectMocks
    private AnalisService analisService;

    private Ticket ticket;
    private List<KeywordWeight> keywords;

    @BeforeEach
    void setUp() {
        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setSubject("Проблемы с оплатой");
        ticket.setDescription("Не могу оплатить заказ");

        keywords = Arrays.asList(
                new KeywordWeight(1L, "Оплата", "оплата", 10.0, LocalDateTime.now()),
                new KeywordWeight(2L, "Оплата", "оплатить", 8.0, LocalDateTime.now()),
                new KeywordWeight(3L, "Авторизация", "вход", 5.0, LocalDateTime.now())
        );
    }

    @Test
    public void analysisTicket_ShouldReturnAnalysisResult() {
        when(keywordWeightRepository.findAll()).thenReturn(keywords);
        when(analisRepository.save(any(AnalysisResult.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AnalysisResult result = analisService.analysisTicket(ticket);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Оплата", result.getDetectedCause());
        Assertions.assertEquals(AnaliseMethod.AUTOMATIC, result.getAnaliseMethod());
        Assertions.assertTrue(result.getAnaliseScore() > 0);
    }

    @Test
    public void analysisTicket_ShouldReturnOther_WhenNoKeywordsMatch() {
        ticket.setSubject("Непонятная проблема");
        ticket.setDescription("Нет ключевых слов");

        when(keywordWeightRepository.findAll()).thenReturn(Collections.emptyList());
        when(analisRepository.save(any(AnalysisResult.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AnalysisResult result = analisService.analysisTicket(ticket);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Другое", result.getDetectedCause());
        Assertions.assertEquals(10.0, result.getAnaliseScore());
    }

    @Test
    public void getTicketAnalysisById_ShouldReturnResults() {
        AnalysisResult result1 = new AnalysisResult();
        result1.setId(1L);
        AnalysisResult result2 = new AnalysisResult();
        result2.setId(2L);

        when(analisRepository.findByTicketId(1L)).thenReturn(Arrays.asList(result1, result2));

        List<AnalysisResult> results = analisService.getTicketAnalysisById(1L);

        Assertions.assertEquals(2, results.size());
    }

    @Test
    public void saveResult_ShouldSaveAndReturn() {
        AnalysisResult result = new AnalysisResult();
        result.setId(1L);

        when(analisRepository.save(result)).thenReturn(result);

        AnalysisResult saved = analisService.saveResult(result);

        Assertions.assertEquals(result, saved);
    }
}