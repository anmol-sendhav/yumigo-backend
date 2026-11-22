CREATE TABLE refresh_tokens (
                                id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                                user_id     BIGINT        NOT NULL,
                                token       VARCHAR(255)  NOT NULL,
                                expires_at  DATETIME(6)   NOT NULL,
                                revoked     BOOLEAN       NOT NULL DEFAULT FALSE,
                                created_at  DATETIME(6)   NOT NULL,
                                updated_at  DATETIME(6)   NOT NULL,
                                version     BIGINT        NOT NULL DEFAULT 0,
                                CONSTRAINT fk_refresh_tokens_user FOREIGN KEY (user_id) REFERENCES users(id),
                                CONSTRAINT uk_refresh_tokens_token UNIQUE (token),
                                INDEX idx_refresh_tokens_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;