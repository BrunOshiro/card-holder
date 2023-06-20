CREATE TABLE IF NOT EXISTS card_holder (
  id UUID NOT NULL,
   client_id UUID,
   credit_analysis_id UUID,
   status INTEGER,
   credit_limit NUMERIC(10,2),
   bank_account VARCHAR(30),
   bank_agency VARCHAR(30),
   bank_code VARCHAR(30),
   created_at TIMESTAMP WITHOUT TIME ZONE,
   CONSTRAINT pk_card_holder PRIMARY KEY (id)
);