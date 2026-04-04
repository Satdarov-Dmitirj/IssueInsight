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