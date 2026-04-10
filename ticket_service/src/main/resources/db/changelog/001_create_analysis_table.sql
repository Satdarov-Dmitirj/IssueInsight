--liquibase formatted sql

--changeset dsatdarov:1
CREATE TABLE IF NOT EXISTS tickets (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    subject VARCHAR(255),
    description VARCHAR(1000),
    status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--rollback DROP TABLE tickets;

--changeset dsatdarov:2
CREATE TABLE IF NOT EXISTS keyword_weights (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    category_name VARCHAR(255) NOT NULL,
    keyword VARCHAR(255) NOT NULL,
    weight DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--rollback DROP TABLE keyword_weights;

--changeset dsatdarov:3
CREATE TABLE IF NOT EXISTS analysis (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ticket_id BIGINT NOT NULL,
    detected_cause VARCHAR(255),
    cause_description TEXT,
    analise_score DOUBLE PRECISION,
    analise_date TIMESTAMP,
    analise_method VARCHAR(50),
    CONSTRAINT fk_analysis_ticket FOREIGN KEY (ticket_id) REFERENCES tickets(id)
);

--rollback DROP TABLE analysis;