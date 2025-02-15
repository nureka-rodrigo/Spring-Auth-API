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

CREATE TABLE password_reset_tokens
(
    id         BIGINT AUTO_INCREMENT  NOT NULL,
    user_id    BIGINT                 NOT NULL,
    token      VARCHAR(255)           NOT NULL,
    created_at datetime DEFAULT NOW() NOT NULL,
    expired_at datetime               NOT NULL,
    CONSTRAINT pk_password_reset_tokens PRIMARY KEY (id),
    CONSTRAINT fk_password_reset_tokens_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE = INNODB;

