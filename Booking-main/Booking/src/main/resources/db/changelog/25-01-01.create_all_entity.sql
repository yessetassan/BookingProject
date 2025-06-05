-- Таблица ролей пользователей
CREATE TABLE t_category (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            name_kz VARCHAR(255),
                            name_ru VARCHAR(255),
                            parent_id INT REFERENCES t_category(id) ON DELETE SET NULL
);

-- Таблица ролей пользователей
CREATE TABLE t_translate (
                            name VARCHAR(100) NOT NULL,
                            name_kz VARCHAR(255),
                            name_ru VARCHAR(255)
);

-- Таблица микрорайонов Алматы
CREATE TABLE t_role (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) UNIQUE NOT NULL,
                        name_kz VARCHAR(255),
                        name_ru VARCHAR(255) ,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица пользователей
CREATE TABLE t_microdistrict (
                                 id SERIAL PRIMARY KEY,
                                 name VARCHAR(100) UNIQUE NOT NULL,
                                 name_kz VARCHAR(100),
                                 name_ru VARCHAR(100)
);

-- Таблица карты
CREATE TABLE t_user (
                        id SERIAL PRIMARY KEY,
                        username VARCHAR(255) UNIQUE NOT NULL,
                        password_hash TEXT NOT NULL,
                        role_id INT REFERENCES t_role(id) ON DELETE CASCADE,
                        phone VARCHAR(20),
                        firstname VARCHAR(50) NOT NULL,
                        lastname VARCHAR(50) NOT NULL,
                        middlename VARCHAR(50),
                        profile_photo_url TEXT,
                        birth_date DATE,
                        microdistrict_id INT REFERENCES t_microdistrict(id),
                        street VARCHAR(255),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица категорий (иерархия категорий)
CREATE TABLE user_cards
(
    id               SERIAL PRIMARY KEY,
    user_id INT REFERENCES t_user(id) ON DELETE CASCADE,
    card_holder_name VARCHAR(100) NOT NULL,
    card_number      VARCHAR(19)  NOT NULL,
    expiry_date      VARCHAR(7)   NOT NULL,
    cvv              VARCHAR(4)   NOT NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица товаров (вещей)
CREATE TABLE t_item (
                        id SERIAL PRIMARY KEY,
                        category_id INT REFERENCES t_category(id) NOT NULL,
                        owner_id INT REFERENCES t_user(id) NOT NULL,
                        name VARCHAR(255) NOT NULL,
                        description TEXT,
                        price_per_day DECIMAL(10,2) NOT NULL,
                        available BOOLEAN DEFAULT TRUE,
                        attributes JSONB, -- Динамические характеристики товара
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        is_deleted_by_admin boolean not null default false,
                        is_approved boolean not null default false,
                        is_rejected boolean not null default false,
                        status varchar(20) not null
);

-- Таблица файлов
CREATE TABLE t_location (
                            id SERIAL PRIMARY KEY,
                            item_id INT REFERENCES t_item(id) ON DELETE CASCADE NOT NULL,
                            microdistrict_id INT REFERENCES t_microdistrict(id),
                            street VARCHAR(255) NOT NULL
);

-- Таблица фильтров (параметры для категорий)
CREATE TABLE t_file (
                        id SERIAL PRIMARY KEY,
                        item_id INT REFERENCES t_item(id) ON DELETE CASCADE NOT NULL,
                        file_url TEXT NOT NULL,
                        file_type VARCHAR(50), -- Например, 'image', 'video', 'document'
                        uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица бронирований (аренды)
CREATE TABLE t_filter (
                          id SERIAL PRIMARY KEY,
                          category_id INT REFERENCES t_category(id) ON DELETE CASCADE,
                          name VARCHAR(100) NOT NULL,
                          name_kz VARCHAR(255),
                          name_ru VARCHAR(255),
                          type VARCHAR(50) CHECK (type IN ('range', 'select', 'multi-select', 'boolean')),
                          options JSONB, -- Возможные значения для фильтра
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP------
);


-- Таблица штрафов за просрочку
CREATE TABLE t_rental (
                          id SERIAL PRIMARY KEY,
                          item_id INT REFERENCES t_item(id) ON DELETE CASCADE,
                          owner_id INT REFERENCES t_user(id) ON DELETE CASCADE,
                          renter_id INT REFERENCES t_user(id) ON DELETE CASCADE,
                          start_datetime date NOT NULL,
                          end_datetime date NOT NULL,
                          total_price DECIMAL(15,2) NOT NULL,
                          status VARCHAR(50) CHECK (status IN ('PENDING', 'APPROVED', 'CANCELED', 'COMPLETED', 'LATE', 'RETURNED_DAMAGED')),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица отзывов (рейтинг варов)
CREATE TABLE t_penalty (
                           id SERIAL PRIMARY KEY,
                           rental_id INT REFERENCES t_rental(id) ON DELETE CASCADE,
                           penalty_amount DECIMAL(15,2) NOT NULL,
                           reason TEXT NOT NULL,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица сообщений (чаты между пользователями)
CREATE TABLE t_review (
                          id SERIAL PRIMARY KEY,
                          item_id INT REFERENCES t_item(id) ON DELETE CASCADE,
                          user_id INT REFERENCES t_user(id) ON DELETE CASCADE,
                          rating INT CHECK (rating BETWEEN 1 AND 5),
                          comment TEXT,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица избранных
CREATE TABLE t_chat (
                        id SERIAL PRIMARY KEY,
                        sender_id INT REFERENCES t_user(id) ON DELETE CASCADE,
                        receiver_id INT REFERENCES t_user(id) ON DELETE CASCADE,
                        message TEXT NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- Таблица избранных
CREATE TABLE t_favourite (
                             id SERIAL PRIMARY KEY,
                             user_id INT REFERENCES t_user(id) ON DELETE CASCADE,
                             item_id INT REFERENCES t_user(id) ON DELETE CASCADE
);

-- Таблица истории действий пользователя
CREATE TABLE t_verification (
                                id SERIAL PRIMARY KEY,
                                file_url TEXT NOT NULL,
                                item_id INT REFERENCES t_item(id) ON DELETE CASCADE
);

-- Таблица рекомендаций
CREATE TABLE t_user_history (
                                id SERIAL PRIMARY KEY,
                                user_id INT REFERENCES t_user(id) ON DELETE CASCADE,
                                item_id INT REFERENCES t_item(id) ON DELETE CASCADE,
                                action_type VARCHAR(50) NOT NULL,  -- Например: 'view', 'book', 'favorite'
                                action_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица местоположений товаров
CREATE TABLE t_recommendations (
                                   id SERIAL PRIMARY KEY,
                                   user_id INT REFERENCES t_user(id) ON DELETE CASCADE,
                                   recommended_item_id INT REFERENCES t_item(id) ON DELETE CASCADE,
                                   score FLOAT NOT NULL,
                                   microdistrict_id INT REFERENCES t_microdistrict(id),
                                   generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE t_category_admin (
                                  id SERIAL PRIMARY KEY,
                                  user_id INTEGER NOT NULL,
                                  category_id INTEGER NOT NULL,
                                  FOREIGN KEY (user_id) REFERENCES t_user(id),
                                  FOREIGN KEY (category_id) REFERENCES t_category(id)
);

CREATE TABLE t_pay_for_view (
                                id SERIAL PRIMARY KEY,
                                view_from DATE NOT NULL,
                                view_till DATE NOT NULL,
                                item_id INT REFERENCES t_item(id) ON DELETE SET NULL,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                status VARCHAR(25) not null
);

CREATE TABLE t_reason (
                          id SERIAL PRIMARY KEY,
                          user_id INT REFERENCES t_user(id) ON DELETE CASCADE,
                          item_id INT REFERENCES t_item(id) ON DELETE SET NULL,
                          file_url TEXT,
                          file_type VARCHAR(50),
                          status VARCHAR(50)  NOT NULL,
                          reason_text VARCHAR(255)  NOT NULL,
                          is_active BOOLEAN DEFAULT TRUE,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX unique_user_item ON t_recommendations (user_id, recommended_item_id);
