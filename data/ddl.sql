CREATE TABLE card_holder (
  id UUID NOT NULL,
  client_id UUID,
  credit_analysis_id UUID,
  status VARCHAR(30) CHECK(status IN ('ACTIVE','INACTIVE')),
  credit_limit NUMERIC(10,2),
  bank_account VARCHAR(30),
  bank_agency VARCHAR(30),
  bank_code VARCHAR(30),
  created_at TIMESTAMP(6),
  PRIMARY KEY (id)
);