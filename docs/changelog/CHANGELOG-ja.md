<!-- This file is generated. Edit .changelog/lang_*.json and rerun .python/generate_markdown.py. -->

# Accessibility Compat リリース履歴

> このページの言語: 日本語

## v1.1.0 - 2026/09/03

### 改善

- アプリ, アクセシビリティサービス, プラグインメタデータ, 組み込み説明, README の表現をアプリに依存しないサポートプロファイルへ一般化し, WeChat (`com.tencent.mm`) は現在唯一の検証済みプロファイルとして維持
- 対象固有のプラグインバリアントを `service-identity` に置き換え, 対応パッケージをコレクションとして公開し, 汎用 UI 操作でインストール済み対応アプリを一覧表示または起動
- A-B-A 手順, 調査入口, プライバシー安全な測定ツールを一般化し, 将来のプロファイル向けに明示的な `targetPackage` 入力を追加
- IDE とツールチェーンの互換性を中央で選択するため, 古い Android Studio および IntelliJ IDEA 最小バージョンプロパティを削除

## v1.0.0 - 2026/09/02

### ヒント

- これは実験的な互換方式です. 結果は WeChat のバージョン, ページ, リモート設定に依存し, すべての端末やアカウントでの成功は保証できません
- WeChat Mini Program, XWeb, Canvas が元から公開しない意味ノードを生成できず, ログイン, リスク制御, 不正利用対策も迂回しません

### 機能

- `com.tencent.mm` だけを対象とする独立 no-op アクセシビリティコンパニオン APK を提供し, 実際のアプリ ID, アイコン, ラベル, 説明, 署名を明確に表示
- 公開実験で支持された互換サービス実装クラス名を登録し, ノードの読み取りと操作は AutoJs6 自身のサービスが継続して担当し, コンパニオンのコールバックはユーザー内容を読み取らない設計
- AutoJs6 プラグイン情報インターフェースで互換モード, 対象パッケージ, サービスコンポーネント, 最小ホストビルドを報告し, ユーザーがサービスを有効化または無効化する画面を提供

### 改善

- [AutoJs6#289](https://github.com/SuperMonster003/AutoJs6/issues/289), [#382](https://github.com/SuperMonster003/AutoJs6/issues/382), [#432](https://github.com/SuperMonster003/AutoJs6/issues/432), [#463](https://github.com/SuperMonster003/AutoJs6/issues/463), [#520](https://github.com/SuperMonster003/AutoJs6/issues/520), [#521](https://github.com/SuperMonster003/AutoJs6/issues/521), GKD `47267c7`, WeChat 8.0.72 の匿名化静的証拠を文書化
- 個人情報を含むノードテキストやスクリーンショットを保存せず, 反復統計と可逆性で互換効果を読み込みやキャッシュの偶然から区別する A-B-A 端末手順を提供
- 10 言語の README と changelog 文案, 再現可能な Markdown 生成器, 読み取り専用整合性検査, GitHub Actions ゲートを追加
- QV710AF65F で AutoJs6 自身から 7 回採取した A-B-A 実測を記録し, ノード数が 1 から 244 に安定して増え, 互換サービス無効化後に 1 へ戻ることとシステムのアクセシビリティ設定の正確な復元を確認
