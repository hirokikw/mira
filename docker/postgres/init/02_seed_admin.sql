-- 初期管理者ユーザー（01_schema.sql 適用後に実行）
-- 開発用ログインパスワード（平文）: admin … アプリ実装前の検証用。本番では使用しないこと。
-- password_hash は pgcrypto の bcrypt（crypt）で生成する。

CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO department (department_code, department_name)
VALUES ('ADMIN', '管理者部門')
ON CONFLICT (department_code) DO NOTHING;

INSERT INTO "user" (
    department_id,
    login_id,
    password_hash,
    user_name,
    authority_code
)
SELECT
    d.department_id,
    'admin',
    crypt('admin', gen_salt('bf', 10)),
    'システム管理者',
    'ADMIN'
FROM department d
WHERE d.department_code = 'ADMIN'
ON CONFLICT (login_id) DO NOTHING;