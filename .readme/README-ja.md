<!-- This file is generated. Edit .readme/lang_*.json and rerun .python/generate_markdown.py. -->
<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <source srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/app/src/main/res/mipmap-night/ic_launcher.png?raw=true" media="(prefers-color-scheme: dark)" />
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-three-adapt-a11y-ic-launcher" border="0" width="128" />
    </picture>
  </p>
  <h1>3-Adapt A11y</h1>
  <p>対応アプリ向けのプライバシー最小化アクセシビリティ互換トリガー</p>
  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/issues"><img alt="GitHub issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y?color=534BAE&label=License"/></a>
  </p>
</div>

> このページの言語: 日本語

### 言語 (Languages)

[简体中文](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-zh-Hans.md) | [香港繁體](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-zh-Hant-HK.md) | [台灣繁體](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-zh-Hant-TW.md) | [English](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-en.md) | [Français](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-fr.md) | [Español](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-es.md) | [日本語](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-ja.md) | [한국어](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-ko.md) | [Русский](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-ru.md) | [العربية](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-ar.md)

### プロジェクトの状態

3-Adapt A11y は独立した実験的コンパニオン APK です. 公開済みサポートプロファイルの対象アプリがコントロールツリーを AutoJs6 のアクセシビリティサービスへ公開できるよう支援します. 汎用自動化エンジンではなく, コントロールの読み取りや操作で AutoJs6 を置き換えません.

> 互換性は各対象アプリのバージョン, ページ, 端末, リモート設定に依存します. すべての環境での成功は保証できません. スクリプトで利用する前に A-B-A 検証を完了してください.

### No-op コンパニオン構成

アプリ互換プロファイルを別 APK に分離し, AutoJs6 コアサービスの名前と一般動作を変更しません:

- ノードを読み取り, 検索し, 操作する唯一のコンポーネントは引き続き AutoJs6 のアクセシビリティサービスです.
- コンパニオンは公開実験で確認されたサービス実装クラス名を登録しますが, アプリ ID, アイコン, ラベル, 説明, 署名は 3-Adapt A11y の実体を正しく示します.
- 互換サービスのコールバックは no-op です. `event.source`, `event.text`, `rootInActiveWindow`, スクリーンショット, ページ内容を読み取りません.
- このリリースはノードプロキシでも Binder ブリッジでもありません. サポートプロファイルに記録された条件付きノード公開動作をトリガーすることだけを試みます.

### インストールと使用方法

1. 端末が Android 7.0 (API 24) 以降で, AutoJs6 の内部ビルド番号が 3923 以上であることを確認します.
2. APK は本プロジェクトの [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/releases) または信頼できる AutoJs6 プラグイン入口からのみインストールします.
3. 3-Adapt A11y を開き, 実際の名前と目的を確認してからステータスカードをタップして互換サービスマネージャーを開きます. 既定のポリシーは AutoJs6 に従うで, AutoJs6 のアクセシビリティサービスが有効な間だけ互換サービスを有効に保ちます.
4. root, WRITE_SECURE_SETTINGS, Shizuku のいずれもない場合は, 3-Adapt A11y の下に表示されるアクセシビリティサービスを Android 設定で手動で有効にします. Android のリスク警告は正常です. いずれかを意図的に付与すると, アプリが選択したポリシーを自ら適用し, 他の設定には触れません.
5. AutoJs6 のアクセシビリティサービスも有効にしたままサポートプロファイルに記載された対象ページを開き直し, AutoJs6 のレイアウト解析またはスクリプトでノードを確認します.

APK をインストールしただけでは効果はありません. 不要なときは無効ポリシーを選ぶか Android 設定でサービスを無効にし, 使用を終えたらアプリをアンインストールできます.

### 設定とサービス制御

設定画面には AutoJs6 に従える汎用オプションがまとまり, 互換サービスマネージャーがサービスをいつ動かすかを決めます:

- 言語, ダークモード, テーマカラーは既定で AutoJs6 に従い, AutoJs6 の設定コントラクトから読み取ります. AutoJs6 がない場合はシステムまたは内蔵の値にフォールバックします.
- 制御ポリシーは AutoJs6 に従う (既定), 有効, 無効の三択です. AutoJs6 に従うを選ぶと互換サービスは AutoJs6 のアクセシビリティサービスと同じ状態に保たれ, AutoJs6 からの変更通知, システムの有効サービス一覧の変化, アプリの起動時に再確認されます.
- 自動的な切り替えは root, WRITE_SECURE_SETTINGS (`adb shell pm grant` で付与), または Shizuku を使い, それぞれ個別に無効化できます. いずれも使えない場合, アプリはシステムのアクセシビリティ設定を開くだけです.
- マネージャーは両サービスと各方式のリアルタイムの状態を表示し, 診断レポートをコピーし, このサービスの Android 設定ページを開けます.
- ランチャーアイコンはアダプティブ ライト, アダプティブ ダーク (既定), アダプティブ 自動, 透明な背景から選択できます. 自動はシステムテーマへの追従を試みますが, ランチャーが配色をキャッシュする場合があります. 透明なアイコンにも背景やマスクが追加される場合があります. 切り替えてもアプリは実行を続け, 反映には数秒かかる場合があります.

### A-B-A 端末検証

1 回成功した dump を証明として扱わないでください. 変化をサービスに帰属する前に, 同じ静止ページで A-B-A 比較を行います:

- A: AutoJs6 を有効, 互換サービスを無効にして, 匿名化したサンプルを 5 回以上収集します.
- B: 互換サービスだけを追加で有効にし, 同じページへ戻って 5 回以上収集します.
- A2: 互換サービスを再び無効にして収集を繰り返します. 変化が元へ戻ることで読み込みやキャッシュの偶然を除外します.
- ノード数, 空でない text/desc/resource-id 数, clickable 数, 構造 hash だけを記録します. チャット本文, 連絡先名, スクリーンショットは保存しません.

[完全な A-B-A 検証手順を読む](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/testing/aba-device-validation.md)

### 実機で確認した結果

2026-09-02 に許可された Sony XQ-AT72, Android 12/API 31, WeChat 8.0.72 code 3085, AutoJs6 6.8.0 code 5277 で可逆な A-B-A 検証を完了しました. 既存の 6 個のアクセシビリティサービスは変更していません. 各段階で WeChat を強制停止して LauncherUI を再起動し, 5 秒以上待機した後, AutoJs6 自身から 7 回連続で採取しました:

- A 互換サービス無効: nodes 1, text 0, desc 0, id 0, clickable 0, 7 回すべて同じ構造 hash.
- B 互換サービス有効かつ対象の 2 サービスが bound: nodes 244, text 31, desc 13, id 157, clickable 38, 7 回すべて同じ構造 hash.
- A2 再び無効: nodes 1 と他の全指標 0 に正確に戻り, 7 回すべて同じ構造 hash. 終了後にシステムのアクセシビリティ設定を正確に復元しました.

この結果は当該端末, WeChat ビルド, LauncherUI ページの組み合わせだけを検証します. 他のバージョン, アカウント, 端末, Mini Program, XWeb, Canvas ページには一般化できません.

[QV710AF65F の完全な匿名化レポートを読む](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/testing/device-QV710AF65F.md)

### 既知の制限

- 現在のサポートプロファイルは WeChat (`com.tencent.mm`) だけです. これは明示的なサポート集合であり, 汎用互換性の主張ではありません.
- WebView, ミニアプリ, Canvas, 独自描画コントロールにはネイティブな意味ノードが存在しない場合があります. 欠落した `text` や `content-desc` を生成できません.
- ルートが戻っても, ページによっては空属性, 古いノード, ランダムなツリー, 境界情報だけが返る場合があります.
- 対象アプリの更新やリモート設定の変更でプロファイルがいつでも無効になる可能性があります. ダウングレード, OCR, 座標操作にも安全性と安定性のコストがあります.
- 対象は可観測性だけです. ログイン, リスク制御, captcha, 権限, アカウント制限, 不正利用対策を迂回しません.
- Android 17 の高度な保護は, この互換サービスを含む支援ツール以外のユーザー補助サービスを制限する場合があります. 全体のモードだけではサービスがブロックされたか判断できません. 実際のサービス状態と Android のユーザー補助設定を確認してください. 安全な設定へのアクセス権限はシステム制限を回避しません.

### プライバシー境界

- 互換コールバックはイベントソース, イベントテキスト, アクティブウィンドウのルート, 画面画像を読み取りません.
- アプリは対象アプリのページ内容をアップロード, 保存, ログ記録せず, ネットワーク機能や分析機能も含みません.
- ストレージ, オーバーレイ, カメラ, マイク, メディアの権限を要求しません. WRITE_SECURE_SETTINGS と Shizuku の権限は任意のサービス制御のためだけに宣言され, ユーザーが付与するまで機能しません.
- プラグイン情報 Binder はバージョン, ID, 能力のメタデータだけを報告し, ノードツリーを転送しません.
- 互換サービスはユーザーが選択した制御ポリシーに従ってのみ変化し, ユーザーが許可した場合に限り root, セキュア設定または Shizuku を通じて切り替わります. アプリは他のシステム設定を変更せず, AutoJs6 のサービスを有効にすることもありません.

### 倫理とコンプライアンス

アクセシビリティ互換機能は, ユーザーが操作権限を持つ画面の自動化だけを支援すべきです. スクリプトの動作とアカウントへの結果は利用者の責任です.

- 自分の端末とアカウント, または明示的に許可されたワークフローでのみ使用します.
- 嫌がらせ, spam, 同意のない収集, 他人の監視, セキュリティ制御の迂回に使用しません.
- 適用法, 対象プラットフォームの規則, 組織ポリシーを守り, UI 変更や誤操作に対する人の確認と停止条件を設けます.
- 診断共有は集計値と匿名化構造だけにします. チャット, 連絡先, token, APK, 生の dex を公開しません.

### 互換性情報

サービスコンポーネントには公開実験で使われた互換クラス名が含まれます. Google Select to Speak ではなく, 読み上げ機能を提供せず, Google アプリや署名を偽装しません. 実際の ID, ラベル, アイコン, 情報ページ, 署名は常に本プロジェクトの身元を示します.

```text
application id: io.github.supermonster003.autojs6.plugin.three.adapt.a11y
accessibility service: io.github.supermonster003.autojs6.plugin.three.adapt.a11y/com.google.android.accessibility.selecttospeak.SelectToSpeakService
supported packages: com.tencent.mm
minimum Android: Android 7.0 (API 24)
minimum AutoJs6 build: 3923
```

### よくある質問

#### Google アプリを偽装していますか?

いいえ. 互換実験に使うのはアクセシビリティサービス実装クラス名だけです. パッケージ, アプリとサービスのラベル, アイコン, 文書, 署名は正直な身元を保ち, Google Select to Speak ではないと明示します.

#### インストール後に何も変わらないのはなぜですか?

root, WRITE_SECURE_SETTINGS, Shizuku のいずれもない場合, Android はアプリ自身がアクセシビリティサービスを有効にすることを許可しないため, 設定で互換サービスと AutoJs6 を有効にしてください. いずれかが使える場合は互換サービスマネージャーが選択したポリシーを適用します. 変化がない場合は A-B-A 手順で現在の対象アプリのバージョンとページを検証してください.

#### 対応ページでテキストノードがまだないのはなぜですか?

WebView, ミニアプリ, Canvas, 独自描画ページは Android の意味ノードを持たない場合があります. サービスは条件付きで隠されたツリーの復元だけを試みます. 元から公開されない情報は生成できません.

#### アカウントの安全を保証しますか?

保証しません. 対象プラットフォームのリスク制御を迂回せず, どの自動化も安全だとは約束できません. 低リスクで監査可能, 人が確認するスクリプトを使い, プラットフォーム規則を守ってください.

### 調査根拠

調査文書は AutoJs6 #289, #382, #432, #463, #520, #521, GKD `47267c7`, およびテスト端末の WeChat 8.0.72 から得た匿名化済み静的証拠を扱います. 公開事実, 再現可能な実験, 推論を区別し, WeChat の内部動作を公式保証として扱いません.

[アクセシビリティサービス ID 互換調査を読む](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/research/accessibility-service-identity-compat.md)

### リリース履歴

#### v1.4.0 - 2026/09/29

##### ヒント

- アプリケーション ID が `io.github.supermonster003.autojs6.plugin.accessibilitycompat` から `io.github.supermonster003.autojs6.plugin.three.adapt.a11y` に変わったため, Android はこのバージョンを新しいアプリとして扱います. 先に Accessibility Compat 1.3.1 以前をアンインストールし, 3-Adapt A11y のアクセシビリティサービスを再度有効にしてください

##### 機能

- ランチャーアイコンはアダプティブ ライト, アダプティブ ダーク (既定), アダプティブ 自動, 透明な背景から選択できます. 自動はシステムテーマへの追従を試みますが, ランチャーが配色をキャッシュする場合があります. 透明なアイコンにも背景やマスクが追加される場合があります. 切り替えてもアプリは実行を続け, 反映には数秒かかる場合があります.

##### 改善

- プラグイン名を Accessibility Compat から 3-Adapt A11y に変更し, アプリ名, アクセシビリティサービスのラベル, プラグイン ID `three-adapt-a11y`, パッケージ名とコンポーネント名, リリース成果物, ドキュメント, GitHub リポジトリを更新

[完全なリリース履歴を読む](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/changelog/CHANGELOG-ja.md)

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

プロジェクトコードは [Mozilla Public License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/LICENSE) で提供されます. WeChat, Google, Select to Speak の名称は各権利者に帰属し, 本プロジェクトはいずれの企業とも提携または承認関係にありません.

### リンク

- [AutoJs6](https://github.com/SuperMonster003/AutoJs6)
- [アクセシビリティサービス ID 互換調査を読む](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/research/accessibility-service-identity-compat.md)
- [完全な A-B-A 検証手順を読む](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/testing/aba-device-validation.md)
- [QV710AF65F の完全な匿名化レポートを読む](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/testing/device-QV710AF65F.md)


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/16kb.md)
