-- MIRA スキーマ DDL（定義は app/docs/database-design/README.md）
-- 初回のみ: PostgreSQL 公式イメージが /docker-entrypoint-initdb.d を実行する

CREATE TABLE departments (
    department_id   BIGSERIAL PRIMARY KEY,
    department_code VARCHAR(32) NOT NULL,
    department_name VARCHAR(128) NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT departments_department_code_key UNIQUE (department_code)
);

CREATE TABLE assignees (
    assignee_id     BIGSERIAL PRIMARY KEY,
    department_id   BIGINT NOT NULL,
    login_id        VARCHAR(64) NOT NULL,
    password_hash   VARCHAR(255) NOT NULL,
    display_name    VARCHAR(128) NOT NULL,
    last_login_at   TIMESTAMPTZ,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT assignees_login_id_key UNIQUE (login_id),
    CONSTRAINT assignees_department_id_fkey
        FOREIGN KEY (department_id)
        REFERENCES departments (department_id)
        ON DELETE RESTRICT
);

CREATE INDEX assignees_department_id_idx ON assignees (department_id);