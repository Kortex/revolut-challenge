CREATE TABLE bank_account (
    id VARCHAR (36) PRIMARY KEY,
    account_holder VARCHAR (250) NOT NULL,
    account_balance BIGINT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    version INTEGER
);

INSERT INTO bank_account VALUES ('42a238e1-a020-4ef2-a6dd-ab590f35ef12', 'Sharon P. Lawhorn', '10000', NOW(), null, 1);
INSERT INTO bank_account VALUES ('7c74d1d9-99f7-48b5-85b6-1397902dde17', 'Alice D. McCoy', '5000', NOW(), null, 1);