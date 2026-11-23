-- MIRA スキーマ DDL（定義は app/docs/database-design/README.md）
-- 初回のみ: PostgreSQL 公式イメージが /docker-entrypoint-initdb.d を実行する

CREATE TABLE department (
    department_id   BIGSERIAL PRIMARY KEY,
    department_code VARCHAR(32) NOT NULL,
    department_name VARCHAR(128) NOT NULL,
    create_datetime      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_datetime      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT department_department_code_key UNIQUE (department_code)
);

CREATE TABLE "user" (
    user_id         BIGSERIAL PRIMARY KEY,
    department_id   BIGINT NOT NULL,
    login_id        VARCHAR(64) NOT NULL,
    password_hash   VARCHAR(255) NOT NULL,
    user_name       VARCHAR(128) NOT NULL,
    authority_code  VARCHAR(32) NOT NULL DEFAULT 'GENERAL',
    last_login_datetime   TIMESTAMPTZ,
    create_datetime      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_datetime      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT user_login_id_key UNIQUE (login_id),
    CONSTRAINT user_department_id_fkey
        FOREIGN KEY (department_id)
        REFERENCES department (department_id)
        ON DELETE RESTRICT
);

CREATE INDEX user_department_id_idx ON "user" (department_id);