CREATE TABLE payment_transactions (
    id SERIAL PRIMARY KEY,
    correlation_id VARCHAR(100),
    endpoint_type VARCHAR(20),
    amount DOUBLE PRECISION NOT NULL,
    processed_at TIMESTAMP NOT NULL
);
