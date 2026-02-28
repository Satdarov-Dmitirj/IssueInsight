package com.example.demo.service;

import com.example.demo.entity.AnaliseMethod;
import com.example.demo.entity.AnalysisResult;
import com.example.demo.entity.Ticket;
import com.example.demo.repository.AnalisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalisService {

    private static final Logger logger = LoggerFactory.getLogger(AnalisService.class);

    private final AnalisRepository analisRepository;

    private static final Map<String, Map<String, Double>> CAUSE_KEYWORDS = new HashMap<>();

    static {
        CAUSE_KEYWORDS.put("Проблемы с авторизацией", Map.of(
                "не могу войти", 2.0,
                "ошибка входа", 1.5,
                "не пускает в аккаунт", 1.8,
                "забыл пароль", 1.6,
                "аккаунт заблокирован", 2.0,
                "не приходит код", 1.7,
                "двухфакторная аутентификация", 1.5,
                "сессия истекла", 1.3,
                "неверный логин", 1.4,
                "не могу сбросить пароль", 1.9
        ));

        CAUSE_KEYWORDS.put("Проблемы с оплатой", Map.of(
                "не проходит платеж", 2.0,
                "ошибка оплаты", 1.7,
                "списали деньги", 1.3,
                "двойное списание", 2.0,
                "возврат средств", 1.8,
                "карта отклонена", 1.9,
                "не могу оплатить", 1.7,
                "платеж завис", 1.5,
                "не пришел чек", 1.2,
                "неверная сумма", 1.6
        ));

        CAUSE_KEYWORDS.put("Технические неполадки", Map.of(
                "сайт не работает", 2.0,
                "приложение вылетает", 1.9,
                "ошибка 500", 1.8,
                "страница не загружается", 1.7,
                "белый экран", 1.6,
                "зависает", 1.5,
                "не открывается", 1.7,
                "ошибка соединения", 1.6,
                "не обновляется", 1.3,
                "баг", 1.4
        ));

        CAUSE_KEYWORDS.put("Проблемы с доставкой", Map.of(
                "не пришел заказ", 2.0,
                "задержка доставки", 1.8,
                "потерян заказ", 2.0,
                "неверный адрес", 1.7,
                "курьер не приехал", 1.9,
                "трек номер не работает", 1.5,
                "посылка повреждена", 1.8,
                "не тот товар", 1.7,
                "доставка отменена", 1.6,
                "долго везут", 1.4
        ));

        CAUSE_KEYWORDS.put("Проблемы с аккаунтом", Map.of(
                "удалили аккаунт", 2.0,
                "взломали аккаунт", 2.0,
                "изменили данные", 1.8,
                "не могу удалить аккаунт", 1.6,
                "не приходит письмо", 1.5,
                "не могу изменить email", 1.7,
                "профиль не сохраняется", 1.4,
                "аватар не загружается", 1.2,
                "история заказов пропала", 1.8,
                "бонусы не начислены", 1.6
        ));

        CAUSE_KEYWORDS.put("Проблемы с контентом", Map.of(
                "не воспроизводится видео", 1.9,
                "нет звука", 1.7,
                "картинки не загружаются", 1.6,
                "файл не скачивается", 1.7,
                "документ не открывается", 1.6,
                "неверная информация", 1.5,
                "контент недоступен", 1.8,
                "ссылка не работает", 1.6,
                "не могу скачать", 1.7,
                "формат не поддерживается", 1.4
        ));

        CAUSE_KEYWORDS.put("Проблемы с подпиской", Map.of(
                "не могу отменить подписку", 2.0,
                "списывают за подписку", 1.9,
                "подписка не активирована", 1.8,
                "промокод не работает", 1.6,
                "не применилась скидка", 1.7,
                "тариф не тот", 1.5,
                "не могу изменить тариф", 1.6,
                "подписка истекла", 1.4,
                "не получил доступ после оплаты", 2.0,
                "автопродление не отключается", 1.8
        ));

        CAUSE_KEYWORDS.put("Проблемы с поддержкой", Map.of(
                "долго не отвечают", 1.8,
                "грубый оператор", 1.9,
                "не решили проблему", 2.0,
                "закрыли заявку без решения", 2.0,
                "не перезвонили", 1.7,
                "потеряли обращение", 1.8,
                "некомпетентная поддержка", 1.9,
                "не помогли", 1.6,
                "игнорируют", 1.7,
                "плохой сервис", 1.5
        ));
    }

    public AnalisService(AnalisRepository analisRepository) {
        this.analisRepository = analisRepository;
    }

    public AnalysisResult analysisTicket(Ticket ticketCreateRequest) {
        String text = (ticketCreateRequest.getSubject() + " " + ticketCreateRequest.getDescription()).toLowerCase();
        logger.debug("Analyzing ticket", ticketCreateRequest.getId());

        Map<String, Double> causeScores = new HashMap<>();

        for (Map.Entry<String, Map<String, Double>> entry : CAUSE_KEYWORDS.entrySet()) {
            String cause = entry.getKey();
            Map<String, Double> keywords = entry.getValue();

            double score = 0;
            double maxScore = keywords.values().stream().mapToDouble(Double::doubleValue).sum();

            for (Map.Entry<String, Double> keywordEntry : keywords.entrySet()) {
                if (text.contains(keywordEntry.getKey())) {
                    score += keywordEntry.getValue();
                }
            }

            if (score > 0) {
                causeScores.put(cause, (score / maxScore) * 100);
            }
        }

        String detectedCause;
        double confidence;
        String description;

        if (causeScores.isEmpty()) {
            detectedCause = "Другое";
            confidence = 10;
            description = "Автоматически не удалось определить причину. Требуется ручной анализ.";
        } else {
            Map.Entry<String, Double> best = causeScores.entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue())
                    .orElseThrow();

            detectedCause = best.getKey();
            confidence = Math.min(best.getValue(), 100);
            description = "Определена причина: " + detectedCause + " (уверенность " + confidence + "%)";
        }

        AnalysisResult result = new AnalysisResult();
        result.setTicket(ticketCreateRequest);
        result.setDetectedCause(detectedCause);
        result.setCauseDescription(description);
        result.setAnaliseScore(confidence);
        result.setAnaliseDate(LocalDateTime.now());
        result.setAnaliseMethod(AnaliseMethod.AUTOMATIC);

        return analisRepository.save(result);
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
}