CREATE TABLE IF NOT EXISTS audit_log (
    id BIGSERIAL PRIMARY KEY,
    event_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    event_type VARCHAR(20) NOT NULL, -- CREATE, UPDATE, DELETE
    entity_type VARCHAR(100) NOT NULL, -- Product, Category
    entity_id BIGINT NOT NULL,
    user_info VARCHAR(255), -- Можно добавить информацию о пользователе
    change_details TEXT, -- JSON или текст с деталями изменений
    ip_address VARCHAR(45)
);