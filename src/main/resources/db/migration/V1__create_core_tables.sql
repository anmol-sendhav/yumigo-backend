CREATE TABLE users (
                       id             BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name           VARCHAR(100) NOT NULL,
                       email          VARCHAR(150) NOT NULL,
                       phone          VARCHAR(15)  NOT NULL,
                       password_hash  VARCHAR(255) NOT NULL,
                       email_verified BOOLEAN      NOT NULL DEFAULT FALSE,
                       role           VARCHAR(30)  NOT NULL,
                       status         VARCHAR(30)  NOT NULL,
                       created_at     DATETIME(6)  NOT NULL,
                       updated_at     DATETIME(6)  NOT NULL,
                       version        BIGINT       NOT NULL DEFAULT 0,
                       CONSTRAINT uk_users_email UNIQUE (email),
                       CONSTRAINT uk_users_phone UNIQUE (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE restaurants(
                    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
                    owner_id       BIGINT NOT NULL ,
                    name           varchar(500)  NOT NULL,
                    description    varchar(500),
                    phone          varchar(500)   NOT NULL,
                    city           varchar(500)   NOT NULL,
                    cuisine_type   varchar(100)  NOT NULL,
                    address_line   varchar(255)  NOT NULL,
                    state          varchar(500)   NOT NULL,
                    pin_code       varchar(6)    NOT NULL,
                    opening_time   TIME          NOT NULL,
                    closing_time   TIME          NOT NULL,
                    is_open        BOOLEAN       NOT NULL DEFAULT FALSE,
                    latitude       DECIMAL(10,7),
                    longitude      DECIMAL(10,7),
                    min_order_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                    avg_rating     DECIMAL(2,1)  NOT NULL DEFAULT 0.0,
                    status         varchar(30)   NOT NULL,
                    created_at     DATETIME(6)   NOT NULL,
                    updated_at     DATETIME(6)   NOT NULL,
                    version        BIGINT        NOT NULL DEFAULT 0,
                    CONSTRAINT fk_restaurants_owner FOREIGN KEY(owner_id) references users(id),
                    INDEX idx_restaurants_city (city),
                    INDEX idx_restaurants_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE addresses (
                           id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                           user_id     BIGINT         NOT NULL,
                           label       VARCHAR(30)    NOT NULL,
                           line1       VARCHAR(255)   NOT NULL,
                           line2       VARCHAR(255),
                           city        VARCHAR(80)    NOT NULL,
                           state       VARCHAR(80)    NOT NULL,
                           pin_code    VARCHAR(6)     NOT NULL,
                           latitude    DECIMAL(10,7),
                           longitude   DECIMAL(10,7),
                           is_default  BOOLEAN  NOT NULL DEFAULT FALSE,
                           created_at  DATETIME(6)    NOT NULL,
                           updated_at  DATETIME(6)    NOT NULL,
                           version     BIGINT         NOT NULL DEFAULT 0,
                           CONSTRAINT fk_addresses_user FOREIGN KEY (user_id) REFERENCES users(id),
                           INDEX idx_addresses_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE menu_categories (
                                 id             BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 restaurant_id  BIGINT        NOT NULL,
                                 name           VARCHAR(100)  NOT NULL,
                                 display_order  INT           NOT NULL DEFAULT 0,
                                 created_at     DATETIME(6)   NOT NULL,
                                 updated_at     DATETIME(6)   NOT NULL,
                                 version        BIGINT        NOT NULL DEFAULT 0,
                                 CONSTRAINT fk_menu_categories_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants(id),
                                 CONSTRAINT uk_menu_categories_restaurant_name UNIQUE (restaurant_id, name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE menu_items (
                            id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
                            restaurant_id           BIGINT         NOT NULL,
                            category_id             BIGINT         NOT NULL,
                            name                    VARCHAR(150)   NOT NULL,
                            description             VARCHAR(500),
                            price                   DECIMAL(10,2)  NOT NULL,
                            is_veg                  BOOLEAN        NOT NULL DEFAULT TRUE,
                            is_available            BOOLEAN        NOT NULL DEFAULT TRUE,
                            image_url               VARCHAR(500),
                            preparation_time_mins   INT,
                            created_at              DATETIME(6)    NOT NULL,
                            updated_at              DATETIME(6)    NOT NULL,
                            version                 BIGINT         NOT NULL DEFAULT 0,
                            CONSTRAINT fk_menu_items_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants(id),
                            CONSTRAINT fk_menu_items_category   FOREIGN KEY (category_id)   REFERENCES menu_categories(id),
                            INDEX idx_menu_items_restaurant (restaurant_id),
                            INDEX idx_menu_items_category (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4

