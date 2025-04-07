CREATE SEQUENCE IF NOT EXISTS notifier_email_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE;

CREATE TABLE IF NOT EXISTS notifier_email
(
    id BIGINT PRIMARY KEY DEFAULT nextval('notifier_email_seq'),
    external_id UUID NOT NULL UNIQUE,
    email VARCHAR NOT NULL,
    required_sending_datetime TIMESTAMP WITH TIME ZONE,
    sending_datetime TIMESTAMP WITH TIME ZONE,
    status VARCHAR,
    subject VARCHAR NOT NULL,
    text VARCHAR NOT NULL
);
