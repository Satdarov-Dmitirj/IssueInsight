
--liquibase formatted sql

--changeset dsatdarov:1
CREATE TABLE IF NOT EXISTS analysis (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ticket_id BIGINT NOT NULL,
    detected_cause VARCHAR(255) NOT NULL,
    cause_description VARCHAR(500) NOT NULL,
    analise_score DOUBLE PRECISION NOT NULL,
    analise_date TIMESTAMP NOT NULL,
    analise_method VARCHAR(20) NOT NULL,
    CONSTRAINT fk_analysis_ticket FOREIGN KEY (ticket_id) REFERENCES tickets(id)  -- Изменено с ticket на tickets
);
--rollback DROP TABLE
