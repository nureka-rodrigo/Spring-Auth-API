CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT                 NOT NULL,
    first_name VARCHAR(255)                          NOT NULL,
    last_name  VARCHAR(255)                          NOT NULL,
    email      VARCHAR(255)                          NOT NULL,
    password   VARCHAR(255)                          NOT NULL,
    role       ENUM ('USER', 'ADMIN') DEFAULT 'USER' NOT NULL,
    created_at datetime               DEFAULT NOW()  NOT NULL,
    updated_at datetime                              NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
) ENGINE = INNODB;

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);