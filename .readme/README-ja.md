<!-- This file is generated. Edit .readme/lang_*.json and rerun .python/generate_markdown.py. -->
<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-accessibility-compat-icon" border="0" width="128" />
  </p>
  <h1>Accessibility Compat</h1>
  <p>対応アプリ向けのプライバシー最小化アクセシビリティ互換トリガー</p>
  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/issues"><img alt="GitHub issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat?color=534BAE&label=License"/></a>
  </p>
</div>

> このページの言語: 日本語

### 言語 (Languages)

[简体中文](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-zh-Hans.md) | [香港繁體](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-zh-Hant-HK.md) | [台灣繁體](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-zh-Hant-TW.md) | [English](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-en.md) | [Français](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-fr.md) | [Español](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-es.md) | [日本語](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-ja.md) | [한국어](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-ko.md) | [Русский](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-ru.md) | [العربية](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-ar.md)

### プロジェクトの状態

Accessibility Compat は独立した実験的コンパニオン APK です. 公開済みサポートプロファイルの対象アプリがコントロールツリーを AutoJs6 のアクセシビリティサービスへ公開できるよう支援します. 汎用自動化エンジンではなく, コントロールの読み取りや操作で AutoJs6 を置き換えません.

> 互換性は各対象アプリのバージョン, ページ, 端末, リモート設定に依存します. すべての環境での成功は保証できません. スクリプトで利用する前に A-B-A 検証を完了してください.

### No-op コンパニオン構成

アプリ互換プロファイルを別 APK に分離し, AutoJs6 コアサービスの名前と一般動作を変更しません:

- ノードを読み取り, 検索し, 操作する唯一のコンポーネントは引き続き AutoJs6 のアクセシビリティサービスです.
- コンパニオンは公開実験で確認されたサービス実装クラス名を登録しますが, アプリ ID, アイコン, ラベル, 説明, 署名は Accessibility Compat の実体を正しく示します.
- 互換サービスのコールバックは no-op です. `event.source`, `event.text`, `rootInActiveWindow`, スクリーンショット, ページ内容を読み取りません.
- このリリースはノードプロキシでも Binder ブリッジでもありません. サポートプロファイルに記録された条件付きノード公開動作をトリガーすることだけを試みます.

### インストールと使用方法

1. 端末が Android 7.0 (API 24) 以降で, AutoJs6 の内部ビルド番号が 3923 以上であることを確認します.
2. APK は本プロジェクトの [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/releases) または信頼できる AutoJs6 プラグイン入口からのみインストールします.
3. Accessibility Compat を開き, 実際の名前と目的を確認してから Android のアクセシビリティ設定への案内に従います.
4. Accessibility Compat の下に表示されるアクセシビリティサービスを手動で有効にします. Android のリスク警告は正常です. ADB で同意を迂回しないでください.
5. AutoJs6 のアクセシビリティサービスも有効にしたままサポートプロファイルに記載された対象ページを開き直し, AutoJs6 のレイアウト解析またはスクリプトでノードを確認します.

APK をインストールしただけでは効果はありません. 不要なときは Android 設定でサービスを無効にし, 使用を終えたらアプリをアンインストールできます.

### A-B-A 端末検証

1 回成功した dump を証明として扱わないでください. 変化をサービスに帰属する前に, 同じ静止ページで A-B-A 比較を行います:

- A: AutoJs6 を有効, 互換サービスを無効にして, 匿名化したサンプルを 5 回以上収集します.
- B: 互換サービスだけを追加で有効にし, 同じページへ戻って 5 回以上収集します.
- A2: 互換サービスを再び無効にして収集を繰り返します. 変化が元へ戻ることで読み込みやキャッシュの偶然を除外します.
- ノード数, 空でない text/desc/resource-id 数, clickable 数, 構造 hash だけを記録します. チャット本文, 連絡先名, スクリーンショットは保存しません.

[完全な A-B-A 検証手順を読む](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/aba-device-validation.md)

### 実機で確認した結果

2026-09-02 に許可された Sony XQ-AT72, Android 12/API 31, WeChat 8.0.72 code 3085, AutoJs6 6.8.0 code 5277 で可逆な A-B-A 検証を完了しました. 既存の 6 個のアクセシビリティサービスは変更していません. 各段階で WeChat を強制停止して LauncherUI を再起動し, 5 秒以上待機した後, AutoJs6 自身から 7 回連続で採取しました:

- A 互換サービス無効: nodes 1, text 0, desc 0, id 0, clickable 0, 7 回すべて同じ構造 hash.
- B 互換サービス有効かつ対象の 2 サービスが bound: nodes 244, text 31, desc 13, id 157, clickable 38, 7 回すべて同じ構造 hash.
- A2 再び無効: nodes 1 と他の全指標 0 に正確に戻り, 7 回すべて同じ構造 hash. 終了後にシステムのアクセシビリティ設定を正確に復元しました.

この結果は当該端末, WeChat ビルド, LauncherUI ページの組み合わせだけを検証します. 他のバージョン, アカウント, 端末, Mini Program, XWeb, Canvas ページには一般化できません.

[QV710AF65F の完全な匿名化レポートを読む](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/device-QV710AF65F.md)

### 既知の制限

- 現在のサポートプロファイルは WeChat (`com.tencent.mm`) だけです. これは明示的なサポート集合であり, 汎用互換性の主張ではありません.
- WebView, ミニアプリ, Canvas, 独自描画コントロールにはネイティブな意味ノードが存在しない場合があります. 欠落した `text` や `content-desc` を生成できません.
- ルートが戻っても, ページによっては空属性, 古いノード, ランダムなツリー, 境界情報だけが返る場合があります.
- 対象アプリの更新やリモート設定の変更でプロファイルがいつでも無効になる可能性があります. ダウングレード, OCR, 座標操作にも安全性と安定性のコストがあります.
- 対象は可観測性だけです. ログイン, リスク制御, captcha, 権限, アカウント制限, 不正利用対策を迂回しません.

### プライバシー境界

- 互換コールバックはイベントソース, イベントテキスト, アクティブウィンドウのルート, 画面画像を読み取りません.
- アプリは対象アプリのページ内容をアップロード, 保存, ログ記録せず, ネットワーク機能や分析機能も含みません.
- ストレージ, オーバーレイ, カメラ, マイク, メディアの権限を要求しません.
- プラグイン情報 Binder はバージョン, ID, 能力のメタデータだけを報告し, ノードツリーを転送しません.
- ユーザーが Android 設定で明示的に有効化または無効化します. プロジェクトが設定を密かに変更することはありません.

### 倫理とコンプライアンス

アクセシビリティ互換機能は, ユーザーが操作権限を持つ画面の自動化だけを支援すべきです. スクリプトの動作とアカウントへの結果は利用者の責任です.

- 自分の端末とアカウント, または明示的に許可されたワークフローでのみ使用します.
- 嫌がらせ, spam, 同意のない収集, 他人の監視, セキュリティ制御の迂回に使用しません.
- 適用法, 対象プラットフォームの規則, 組織ポリシーを守り, UI 変更や誤操作に対する人の確認と停止条件を設けます.
- 診断共有は集計値と匿名化構造だけにします. チャット, 連絡先, token, APK, 生の dex を公開しません.

### 互換性情報

サービスコンポーネントには公開実験で使われた互換クラス名が含まれます. Google Select to Speak ではなく, 読み上げ機能を提供せず, Google アプリや署名を偽装しません. 実際の ID, ラベル, アイコン, 情報ページ, 署名は常に本プロジェクトの身元を示します.

```text
application id: io.github.supermonster003.autojs6.plugin.accessibilitycompat
accessibility service: io.github.supermonster003.autojs6.plugin.accessibilitycompat/com.google.android.accessibility.selecttospeak.SelectToSpeakService
supported packages: com.tencent.mm
minimum Android: Android 7.0 (API 24)
minimum AutoJs6 build: 3923
```

### よくある質問

#### Google アプリを偽装していますか?

いいえ. 互換実験に使うのはアクセシビリティサービス実装クラス名だけです. パッケージ, アプリとサービスのラベル, アイコン, 文書, 署名は正直な身元を保ち, Google Select to Speak ではないと明示します.

#### インストール後に何も変わらないのはなぜですか?

Android ではアプリ自身がアクセシビリティサービスを有効にできません. ユーザーが設定で互換サービスと AutoJs6 を有効にする必要があります. 変化がない場合は A-B-A 手順で現在の対象アプリのバージョンとページを検証してください.

#### 対応ページでテキストノードがまだないのはなぜですか?

WebView, ミニアプリ, Canvas, 独自描画ページは Android の意味ノードを持たない場合があります. サービスは条件付きで隠されたツリーの復元だけを試みます. 元から公開されない情報は生成できません.

#### アカウントの安全を保証しますか?

保証しません. 対象プラットフォームのリスク制御を迂回せず, どの自動化も安全だとは約束できません. 低リスクで監査可能, 人が確認するスクリプトを使い, プラットフォーム規則を守ってください.

### 調査根拠

調査文書は AutoJs6 #289, #382, #432, #463, #520, #521, GKD `47267c7`, およびテスト端末の WeChat 8.0.72 から得た匿名化済み静的証拠を扱います. 公開事実, 再現可能な実験, 推論を区別し, WeChat の内部動作を公式保証として扱いません.

[アクセシビリティサービス ID 互換調査を読む](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/research/accessibility-service-identity-compat.md)

### リリース履歴

#### v1.1.0 - 2026/09/11

##### 改善

- アプリ, アクセシビリティサービス, プラグインメタデータ, 組み込み説明, README の表現をアプリに依存しないサポートプロファイルへ一般化し, WeChat (`com.tencent.mm`) は現在唯一の検証済みプロファイルとして維持
- 対象固有のプラグインバリアントを `service-identity` に置き換え, 対応パッケージをコレクションとして公開し, 汎用 UI 操作でインストール済み対応アプリを一覧表示または起動
- A-B-A 手順, 調査入口, プライバシー安全な測定ツールを一般化し, 将来のプロファイル向けに明示的な `targetPackage` 入力を追加
- IDE とツールチェーンの互換性を中央で選択するため, 古い Android Studio および IntelliJ IDEA 最小バージョンプロパティを削除
- 意図しないネイティブ依存関係をビルド時に拒否し, JSON レポートを生成

##### 依存関係

- `io.github.supermonster003.autojs6-platform-versions` を 1.7.0 から 1.7.3 に更新し, JDK 25 および 26 のビルドで選択された KGP をルート buildscript classpath に自動整合

[完全なリリース履歴を読む](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/changelog/CHANGELOG-ja.md)

### ビルドと文書検証

一般ユーザーは Releases のビルド済み APK を利用してください. 開発者はリポジトリの Gradle Wrapper でビルドと検証を行えます:

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug :app:assembleDebugAndroidTest :app:lintDebug
py .python\generate_markdown.py --check
```

README と changelog は JSON 文案から生成されます. `.readme/lang_*.json`, `.changelog/lang_*.json`, テンプレートを変更した後に実行します:

```powershell
py .python\generate_markdown.py
py .python\generate_markdown.py --check
```

### ライセンス

プロジェクトコードは [Mozilla Public License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/LICENSE) で提供されます. WeChat, Google, Select to Speak の名称は各権利者に帰属し, 本プロジェクトはいずれの企業とも提携または承認関係にありません.

### リンク

- [AutoJs6](https://github.com/SuperMonster003/AutoJs6)
- [アクセシビリティサービス ID 互換調査を読む](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/research/accessibility-service-identity-compat.md)
- [完全な A-B-A 検証手順を読む](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/aba-device-validation.md)
- [QV710AF65F の完全な匿名化レポートを読む](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/device-QV710AF65F.md)


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/16kb.md)
