# ログイン失敗（admin / admin）— Tomcat 複数台でのセッション非共有

| 項目 | 内容 |
| --- | --- |
| 状態 | 解決済み |
| 発生日 | 2026-11-24（調査・解消） |
| 影響機能 | wa01 ログイン（`/login`） |
| 環境 | Docker Compose（HAProxy → httpd×2 → Tomcat×2 → PostgreSQL） |
| アクセス先 | http://127.0.0.1:8080/login |

## 事象

- 画面で **ログイン ID `admin` / パスワード `admin`** を入力してもメインメニュー（`/menu`）に進めない。
- 画面上は「認証情報が違う」ように見えることがある（`/login?error` やログイン画面へのリダイレクト）。
- **DB 接続用のパスワード**（`docker/confidential/db.env`）とは無関係。画面ログイン用の初期ユーザーの資格情報である。

## 根本原因

Docker 構成上、ブラウザからのリクエストは **HAProxy が roundrobin で httpd-1 / httpd-2 に振り分け**、各 httpd は **別々の Tomcat** にプロキシしている。

1. **`GET /login`** … いずれかの Tomcat が **HTTP セッション（`JSESSIONID`）** と **CSRF トークン** を発行する。
2. **`POST /login`** … 振り分け先が別の Tomcat になると、そのインスタンスには **同じセッションが存在しない**。
3. Spring Security では CSRF 検証・フォーム認証が同一セッションを前提とするため、**認証に失敗**する。

アプリ単体（Tomcat 1 台への直接アクセス）では `admin` / `admin` でログインでき、DB 上の `password_hash` と平文 `admin` の照合（pgcrypto / BCrypt）も問題なかった。  
**資格情報や認証ロジックの不備ではなく、ロードバランサ配下でのセッション（スティッキー）不足が主因**。

### あわせて整理した点（副次要因になり得る）

- `login.html` で `th:action` による CSRF 自動付与と、手書きの hidden CSRF が **二重** になっていた。手書きを削除し、1 つの CSRF フィールドに統一した。
- 調査過程で `DaoAuthenticationProvider` に `BCryptPasswordEncoder` を明示する修正を入れている（パスワード照合の明示化）。今回の主因は上記セッション振り分けである。

## 対応内容

### 1. HAProxy で JSESSIONID によるスティッキーセッション

`docker/haproxy/haproxy.cfg` のバックエンドで、クライアントが持つ `JSESSIONID` に基づき **同一 httpd（＝同一 Tomcat）** へ寄せる。

```text
cookie JSESSIONID prefix nocache
server httpd1 httpd-1:80 check cookie s1
server httpd2 httpd-2:80 check cookie s2
```

### 2. ログイン画面の CSRF

`app/src/main/resources/templates/login.html` から手動の CSRF hidden を削除（`th:action` 付与分のみ）。

### 3. デプロイ

設定変更後は Tomcat イメージの再ビルドおよび HAProxy の再起動が必要。

```powershell
docker compose up --build -d
```

Tomcat 起動直後は数十秒ほど 503 になることがある。

## 検証

- PostgreSQL で `admin` 行の存在および `crypt('admin', password_hash) = password_hash` が真であることを確認。
- Tomcat 単体（コンテナ内から `http://tomcat-1:8080/login`）で POST 後 `302` → `/menu` を確認。
- HAProxy 経由（`http://127.0.0.1:8080/login`）で同一 cookie  jar を用いた GET → POST の連続試行で `/menu` へ遷移することを確認。

## 再発防止・留意

- **Tomcat（またはアプリサーバー）を水平スケールする場合**は、次のいずれかが必要になる。
  - ロードバランサでのセッションアフィニティ（本件の方式）
  - セッションの外部共通化（例: Spring Session + Redis 等）
  - ステートレス認証（例: JWT）への変更
- 本番相当の構成変更（インスタンス数・LB 設定）時は、**ログイン（GET + POST）を必ず E2E で確認**する。
- 画面のログイン ID/パスワードと、`db.env` の DB パスワードは別物であることをドキュメント・オンボーディングで区別する。

## 関連ファイル

| ファイル | 備考 |
| --- | --- |
| `docker/haproxy/haproxy.cfg` | スティッキーセッション |
| `app/src/main/resources/templates/login.html` | CSRF 二重の解消 |
| `app/src/main/java/com/examplecompany/mira/common/config/SecurityConfig.java` | 認証プロバイダ・フォームログイン |
| `docker/postgres/init/02_seed_admin.sql` | 初期ユーザー `admin` |
| `compose.yaml` | httpd / Tomcat 2 系構成 |

## 参考（設計）

- [wa01 ログイン](../app/docs/functional-design/wa01-login.md)