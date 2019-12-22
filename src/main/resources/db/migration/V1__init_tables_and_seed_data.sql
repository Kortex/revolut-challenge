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
INSERT INTO bank_account VALUES ('471c830e-b223-4106-8d8c-b07d53ff0361', 'Franziska Schreiner', '150000', NOW(), null, 1);
INSERT INTO bank_account VALUES ('c091c90e-d5a4-4d7f-971e-130edb3cddc4', 'Austin Platt', '3000', NOW(), null, 1);
INSERT INTO bank_account VALUES ('02239beb-1273-40c0-8206-9de764d8bebc', 'Cooper Wirth', '8000', NOW(), null, 1);
INSERT INTO bank_account VALUES ('fd23cb79-8b8d-48d5-a591-3e11fbfb7494', 'Amy W. Broadhead', '25000', NOW(), null, 1);