# MIRA 業務要件

## アクター

| アクター | 説明 |
| --- | --- |
| ユーザー管理担当者 | MIRA にログインし、画面からユーザー管理業務を行う |
| 他システム | REST API を呼び出し、ユーザー登録などの処理を依頼する |
| MIRA | ログイン・画面・REST API・バッチを提供するアプリケーション |
| DB | ユーザー情報を登録・参照・更新・削除し、永続化する |

## ユーザー管理の業務フロー

| ID | 業務名 |
| --- | --- |
| a | ログイン業務 |
| b | ユーザー新規登録業務 |
| c | ユーザー確認業務 |
| d | ユーザー編集業務 |
| e | ユーザー削除業務 |
| f | 長期間ログインしていないユーザーの自動削除 |

### a. ログイン業務

本業務は画面でのみ実施する（メインメニューへの遷移までを含む）。

```mermaid
sequenceDiagram
    actor Staff as ユーザー管理担当者
    participant Mira as MIRA
    participant DB as DB

    Mira-->>Staff: ログイン画面を表示
    Staff->>Mira: ログイン情報を入力して送信
    Mira->>DB: 認証情報を照合
    DB-->>Mira: 照合結果
    alt 認証成功
        Mira->>DB: 最終ログイン日などを更新
        Mira-->>Staff: メインメニューを表示
    else 認証失敗
        Mira-->>Staff: エラーを表示し、再入力を受け付ける
    end
```

### b. ユーザー新規登録業務

画面（ログイン後のメニュー）または REST API のいずれからも、新規ユーザーを登録できる。登録処理はいずれも MIRA が受け付け、DB に保存する。

```mermaid
sequenceDiagram
    actor Staff as ユーザー管理担当者
    participant Client as 他システム
    participant Mira as MIRA
    participant DB as DB

    alt 画面経由
        Staff->>Mira: メインメニューで新規登録を選択
        Mira-->>Staff: 登録画面を表示
        Staff->>Mira: ユーザー情報を入力して登録を依頼
        Mira->>DB: ユーザーを登録
        DB-->>Mira: 登録結果
        Mira-->>Staff: 登録結果を表示
    else API 経由
        Client->>Mira: ユーザー登録 API にユーザー情報を送信
        Mira->>DB: ユーザーを登録
        DB-->>Mira: 登録結果
        Mira-->>Client: 登録結果を応答（HTTP レスポンス）
    end
```

### c. ユーザー確認業務

ユーザー一覧の把握は画面のみ行う。ユーザー詳細の取得は、画面または REST API のいずれからも行える。

```mermaid
sequenceDiagram
    actor Staff as ユーザー管理担当者
    participant Client as 他システム
    participant Mira as MIRA
    participant DB as DB

    alt 画面経由
        Staff->>Mira: メインメニューでユーザー一覧を選択
        Mira->>DB: ユーザー一覧を取得
        DB-->>Mira: 一覧データ
        Mira-->>Staff: 登録済みユーザーの一覧を表示
        Staff->>Mira: 確認するユーザーを選択
        Mira->>DB: ユーザー詳細を取得
        DB-->>Mira: 詳細データ
        Mira-->>Staff: ユーザー詳細を表示
        Staff->>Staff: ユーザー情報を確認
    else API 経由
        Client->>Mira: ユーザー詳細取得 API を呼び出し
        Mira->>DB: ユーザー詳細を取得
        DB-->>Mira: 詳細データ
        Mira-->>Client: ユーザー詳細を応答（HTTP レスポンス）
    end
```

### d. ユーザー編集業務

画面からの編集または REST API による更新のいずれでも、ユーザー情報を変更できる。

```mermaid
sequenceDiagram
    actor Staff as ユーザー管理担当者
    participant Client as 他システム
    participant Mira as MIRA
    participant DB as DB

    alt 画面経由
        Note over Staff,Mira: 業務フロー c で対象ユーザーの詳細を表示済み
        Staff->>Mira: ユーザー詳細から編集を選択
        Mira-->>Staff: 現在のユーザー情報を編集画面に表示
        Staff->>Mira: 変更内容を入力して保存を依頼
        Mira->>DB: ユーザー情報を更新
        DB-->>Mira: 更新結果
        Mira-->>Staff: 保存結果を表示
    else API 経由
        Client->>Mira: ユーザー更新 API に対象ユーザーと変更内容を送信
        Mira->>DB: ユーザー情報を更新
        DB-->>Mira: 更新結果
        Mira-->>Client: 更新結果を応答（HTTP レスポンス）
    end
```

### e. ユーザー削除業務

画面からの削除（確認あり）または REST API による削除のいずれでも、ユーザーを削除できる。

```mermaid
sequenceDiagram
    actor Staff as ユーザー管理担当者
    participant Client as 他システム
    participant Mira as MIRA
    participant DB as DB

    alt 画面経由
        Note over Staff,Mira: 業務フロー c で対象ユーザーの詳細を表示済み
        Staff->>Mira: ユーザー詳細から削除を選択
        Mira-->>Staff: 削除の確認を表示
        alt 削除を確定
            Staff->>Mira: 削除を確定する
            Mira->>DB: 対象ユーザーを削除
            DB-->>Mira: 削除結果
            Mira-->>Staff: 削除結果を表示
        else 削除を取り消す
            Staff->>Mira: キャンセルする
            Mira-->>Staff: ユーザー詳細を表示
        end
    else API 経由
        Client->>Mira: ユーザー削除 API を呼び出し（対象ユーザーを指定）
        Mira->>DB: 対象ユーザーを削除
        DB-->>Mira: 削除結果
        Mira-->>Client: 削除結果を応答（HTTP レスポンス）
    end
```

### f. 長期間ログインしていないユーザーの自動削除

定期バッチは、最終ログイン日から90日以上経過したユーザーを削除する。該当するユーザーがいなければ処理を終了する。

```mermaid
sequenceDiagram
    participant Cron as Cron
    participant Batch as ユーザー自動削除バッチ
    participant DB as DB

    Note over Batch: MIRA アプリ内で実行する想定
    Cron->>Batch: バッチを起動
    Batch->>DB: 最終ログイン日から90日以上経過したユーザーを抽出
    DB-->>Batch: 抽出結果
    opt 対象ユーザーあり
        Batch->>DB: 対象ユーザーを削除
        DB-->>Batch: 削除結果
    end
    Note over Batch: 処理終了
```

バッチの実行時刻と、ログイン履歴がないユーザーの扱いは今後決定する。
