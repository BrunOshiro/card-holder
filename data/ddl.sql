CREATE TABLE card_holder (
  id UUID NOT NULL,
   client_id UUID,
   credit_analysis_id UUID,
   status VARCHAR(255) CHECK(status IN ('ACTIVE','INACTIVE')),
   credit_limit NUMERIC(10,2),
   bank_account_id UUID,
   created_at TIMESTAMP WITHOUT TIME ZONE,
   CONSTRAINT pk_card_holder PRIMARY KEY (id)
);

ALTER TABLE card_holder ADD CONSTRAINT FK_CARD_HOLDER_ON_BANK_ACCOUNT FOREIGN KEY (bank_account_id) REFERENCES bank_account (id);

CREATE TABLE bank_account (
  id UUID NOT NULL,
   account VARCHAR(255),
   agency VARCHAR(255),
   bank_code VARCHAR(255),
   created_at TIMESTAMP WITHOUT TIME ZONE,
   CONSTRAINT pk_bank_account PRIMARY KEY (id)
);