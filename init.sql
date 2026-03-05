CREATE DATABASE IF NOT EXISTS banking;
USE banking;

CREATE TABLE customer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    identifier VARCHAR(20) NOT NULL,
    email VARCHAR(150) NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_business BOOLEAN NOT NULL,

    CONSTRAINT uk_customer_identifier UNIQUE (identifier),
    CONSTRAINT uk_customer_email UNIQUE (email)
);

CREATE TABLE balance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    value DECIMAL(15,2) NOT NULL DEFAULT 0.00,

    CONSTRAINT fk_balance_customer
        FOREIGN KEY (customer_id)
        REFERENCES customer(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT uk_balance_customer UNIQUE (customer_id)
);

CREATE TABLE transference (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    payer_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_transference_payer
        FOREIGN KEY (payer_id)
        REFERENCES customer(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_transference_receiver
        FOREIGN KEY (receiver_id)
        REFERENCES customer(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

INSERT INTO customer (identifier, email, password, isBusiness) VALUES
('12345678901', 'joao@email.com', '123456', FALSE),
('98765432100', 'maria@email.com', '123456', FALSE),
('11222333000199', 'empresa@email.com', '123456', TRUE),
('55667788990', 'carlos@email.com', '123456', FALSE),
('99887766554', 'ana@email.com', '123456', FALSE);

INSERT INTO balance (customer_id, value) VALUES
(1, 1000.00),
(2, 500.00),
(3, 10000.00),
(4, 750.00),
(5, 1200.00);

INSERT INTO transference (payer, receiver, value) VALUES
(1, 2, 150.00),
(3, 1, 500.00),
(2, 4, 100.00),
(5, 1, 200.00),
(1, 4, 50.00);