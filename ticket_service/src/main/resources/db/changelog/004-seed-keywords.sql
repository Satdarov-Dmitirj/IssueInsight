
INSERT INTO keyword_weights (category_name, keyword, weight, created_at) VALUES
-- Проблемы с авторизацией
('Проблемы с авторизацией', 'логин', 10.0, CURRENT_TIMESTAMP),
('Проблемы с авторизацией', 'пароль', 10.0, CURRENT_TIMESTAMP),
('Проблемы с авторизацией', 'войти', 9.0, CURRENT_TIMESTAMP),
('Проблемы с авторизацией', 'вход', 8.0, CURRENT_TIMESTAMP),
('Проблемы с авторизацией', 'доступ', 7.0, CURRENT_TIMESTAMP),

-- Технические проблемы
('Технические проблемы', 'ошибка', 10.0, CURRENT_TIMESTAMP),
('Технические проблемы', 'error', 10.0, CURRENT_TIMESTAMP),
('Технические проблемы', 'баг', 9.0, CURRENT_TIMESTAMP),
('Технические проблемы', 'работает', 8.0, CURRENT_TIMESTAMP),
('Технические проблемы', 'сбой', 8.0, CURRENT_TIMESTAMP),

-- Проблемы с оплатой
('Проблемы с оплатой', 'оплата', 10.0, CURRENT_TIMESTAMP),
('Проблемы с оплатой', 'платеж', 10.0, CURRENT_TIMESTAMP),
('Проблемы с оплатой', 'деньги', 9.0, CURRENT_TIMESTAMP),
('Проблемы с оплатой', 'карта', 8.0, CURRENT_TIMESTAMP),
('Проблемы с оплатой', 'счет', 7.0, CURRENT_TIMESTAMP);

--rollback DELETE FROM keyword_weights;