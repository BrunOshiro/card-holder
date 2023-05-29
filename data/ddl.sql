CREATE TABLE IF NOT EXISTS card_holder (
    id uuid PRIMARY KEY,
    client_id uuid,
    credit_analysis_id uuid,
    available_limit NUMERIC(10,2),
    creation_date TIMESTAMP
);