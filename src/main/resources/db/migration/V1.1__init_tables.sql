CREATE TABLE bank_account (
    id VARCHAR (36) PRIMARY KEY,
    account_holder VARCHAR (250) NOT NULL,
    account_balance INTEGER,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    version INTEGER
)