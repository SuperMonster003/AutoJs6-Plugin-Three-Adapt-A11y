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
  <p>面向支援應用程式的隱私最小化無障礙相容觸發器</p>
  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/issues"><img alt="GitHub issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y?color=534BAE&label=License"/></a>
  </p>
</div>

> 本頁語言: 台灣繁體中文

### 語言 (Languages)

[简体中文](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-zh-Hans.md) | [香港繁體](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-zh-Hant-HK.md) | [台灣繁體](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-zh-Hant-TW.md) | [English](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-en.md) | [Français](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-fr.md) | [Español](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-es.md) | [日本語](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-ja.md) | [한국어](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-ko.md) | [Русский](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-ru.md) | [العربية](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-ar.md)

### 專案狀態

3-Adapt A11y 是獨立的實驗性伴生 APK. 它嘗試協助已發布支援設定涵蓋的應用程式向 AutoJs6 無障礙服務提供控制項樹. 它不是通用自動化引擎, 也不會代替 AutoJs6 讀取或操作控制項.

> 相容效果由各目標應用程式版本, 頁面, 裝置及遠端設定決定. 本專案不保證在所有環境中有效. 請先完成 A-B-A 驗收再用於任何腳本.

### No-op 伴生架構

專案將應用程式相容設定隔離於獨立 APK, 保持 AutoJs6 核心服務名稱及通用行為不變:

- AutoJs6 自己的無障礙服務仍是唯一讀取, 查詢及操作節點的元件.
- 伴生 APK 註冊一個已獲公開實證識別的服務實作類別名稱, 但應用程式 ID, 圖示, 標籤, 說明及簽章均如實標識為 3-Adapt A11y.
- 相容服務的事件回呼是 no-op. 它不讀取 `event.source`, `event.text`, `rootInActiveWindow`, 螢幕截圖或頁面內容.
- 本版本不是節點代理或 Binder 橋接. 它只嘗試觸發支援設定中記錄的條件性節點提供行為.

### 安裝及使用

1. 確認裝置為 Android 7.0 (API 24) 或以上, AutoJs6 內部版本號不低於 3923.
2. 只從本專案 [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/releases) 或可信的 AutoJs6 外掛入口安裝 APK.
3. 開啟 3-Adapt A11y, 核對真實應用程式名稱及用途, 再點選狀態卡片開啟相容服務管理器. 預設策略為跟隨 AutoJs6, 只在 AutoJs6 無障礙服務已啟用時保持相容服務啟用.
4. 沒有 root, WRITE_SECURE_SETTINGS 或 Shizuku 時, 由使用者在 Android 設定中手動啟用 3-Adapt A11y 對應的無障礙服務, Android 顯示無障礙風險提示是正常現象. 主動授予其中一種能力後, 應用程式會自行套用所選策略, 不觸碰其他設定.
5. 同時保持 AutoJs6 無障礙服務啟用, 重新進入支援設定所列目標頁面, 再從 AutoJs6 版面分析器或腳本讀取節點.

只安裝 APK 不會生效. 不使用時請選擇停用策略或在系統無障礙設定關閉相容服務, 如不再需要可直接解除安裝.

### 設定與服務控制

設定頁面集中了可跟隨 AutoJs6 的通用選項, 相容服務管理器則決定服務何時執行:

- 語言, 深色模式與主題色預設為跟隨 AutoJs6, 透過 AutoJs6 設定契約讀取; 未安裝 AutoJs6 時回退到系統或內建值.
- 控制策略為三選一: 跟隨 AutoJs6 (預設), 啟用或停用. 跟隨 AutoJs6 使相容服務與 AutoJs6 無障礙服務保持相同狀態, 並在 AutoJs6 通知變化, 系統已啟用服務清單變化以及應用程式開啟時重新檢查.
- 自動變更透過 root, WRITE_SECURE_SETTINGS (使用 `adb shell pm grant` 授予) 或 Shizuku 完成, 每種方式都可以單獨關閉. 三者都不可用時, 應用程式只會開啟系統無障礙設定.
- 管理器顯示兩個服務與每種方式的即時狀態, 可複製診斷報告, 並可開啟此服務的 Android 設定頁面.
- 統一獨立設定的平面分組, 列規格與置中圓角對話框. 語言, 夜間模式, 主題色與啟動器圖示均在確定後生效, 取消不改變已儲存的設定. 主題色預設跟隨 AutoJs6, 提供統一色盤, HEX/RGB 輸入與局部預覽; 中性底色保持穩定, 控制項遵循所選主題. 啟動器預設自適應自動, 升級保留明確儲存的選擇.

### A-B-A 裝置驗收

不要把一次成功 dump 當作結論. 在同一靜止頁面執行 A-B-A 對照, 才可把變化與相容服務關聯:

- A: 關閉相容服務, 保持 AutoJs6 服務啟用, 連續收集至少 5 次去識別化指標.
- B: 只啟用相容服務, 返回同一頁面, 再連續收集至少 5 次.
- A2: 再次關閉相容服務並重複收集, 確認變化可以逆轉, 排除頁面載入及快取偶然性.
- 記錄節點數, 非空 text/desc/resource-id 數量, clickable 數及結構 hash, 不保留聊天文字, 聯絡人名稱或螢幕截圖.

[查看完整 A-B-A 驗收協定](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/testing/aba-device-validation.md)

### 實機驗證結果

2026-09-02 在已授權裝置 Sony XQ-AT72, Android 12/API 31 上完成可逆 A-B-A 驗收. 微信為 8.0.72 code 3085, AutoJs6 為 6.8.0 code 5277, 原有 6 個無障礙服務維持不變. 每個階段都強制停止並重新啟動微信 LauncherUI, 等待超過 5 秒, 再由 AutoJs6 本身連續取樣 7 次:

- A 相容服務關閉: nodes 1, text 0, desc 0, id 0, clickable 0, 7 次結構 hash 一致.
- B 相容服務開啟且兩個目標服務均已 bound: nodes 244, text 31, desc 13, id 157, clickable 38, 7 次結構 hash 一致.
- A2 再次關閉: 精確回到 nodes 1 且其餘指標為 0, 7 次結構 hash 一致. 測試後系統無障礙設定已精確還原.

這證明相容觸發器在該裝置, 微信版本及 LauncherUI 頁面組合上有效. 結果不能外推到其他版本, 帳號, 裝置, 小程式, XWeb 或 Canvas 頁面.

[查看 QV710AF65F 完整去識別報告](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/testing/device-QV710AF65F.md)

### 已知限制

- 目前支援設定只包含微信 (`com.tencent.mm`). 這是明確的支援集合, 不代表宣稱通用於所有應用程式.
- WebView, 小程式, Canvas 及自繪控制項可能根本沒有原生語意節點. 本外掛無法憑空補出缺少的 `text` 或 `content-desc`.
- 即使根節點恢復, 部分頁面仍可能返回空屬性, 過時節點, 隨機樹或只有邊界資訊.
- 目標應用程式升級或遠端設定變更可隨時使對應支援設定失效. 降級, OCR 或座標備援方案也各有安全及穩定性代價.
- 本外掛只處理可觀察性, 不會繞過登入, 風控, 驗證碼, 權限, 帳號限制或平台反濫用機制.
- Android 17 進階保護可能限制非輔助工具的無障礙服務, 包括此相容服務. 僅憑全域模式無法判斷此服務是否遭到封鎖. 請檢查實際服務狀態及 Android 無障礙設定. 授予安全設定權限不會繞過系統限制.

### 隱私邊界

- 相容回呼不讀取事件來源, 事件文字, 活動視窗根節點或螢幕影像.
- 應用程式不會上傳, 持久儲存或記錄目標應用程式頁面內容, 也不包含網路或分析功能.
- 應用程式不申請儲存空間, 懸浮視窗, 相機, 麥克風或媒體權限. 宣告 WRITE_SECURE_SETTINGS 與 Shizuku 權限只為可選的服務控制, 在你授予之前均不生效.
- 外掛資訊 Binder 只回報版本, 身分及能力中繼資料, 不傳輸節點樹.
- 相容服務只依你選擇的控制策略變更, 而且只在你允許時透過 root, 安全設定或 Shizuku 完成. 應用程式不會修改任何其他系統設定, 也不會啟用 AutoJs6 服務.

### 倫理及合規

無障礙相容能力只應協助使用者自動化其獲授權操作的介面. 使用者須對腳本行為及帳號後果負責.

- 只在自己的裝置, 帳號及獲明確授權的流程使用.
- 不得用於騷擾, 群發垃圾訊息, 未經同意的資料收集, 監控他人或規避安全控制.
- 遵守適用法律, 目標平台規則及組織政策, 並為介面變更及誤操作設定人工確認及停止條件.
- 分享診斷時只發布彙總統計及去識別化結構, 不公開聊天內容, 聯絡人, token, APK 或原始 dex.

### 相容資訊

服務元件名稱包含公開實驗使用的相容類別名稱. 這不是 Google Select to Speak, 不提供朗讀功能, 也不冒充 Google 應用程式或簽章. 真實身分始終由本專案的應用程式 ID, 標籤, 圖示, 資訊頁及簽章公開展示.

```text
application id: io.github.supermonster003.autojs6.plugin.three.adapt.a11y
accessibility service: io.github.supermonster003.autojs6.plugin.three.adapt.a11y/com.google.android.accessibility.selecttospeak.SelectToSpeakService
supported packages: com.tencent.mm
minimum Android: Android 7.0 (API 24)
minimum AutoJs6 build: 3923
```

### 常見問題

#### 這個專案是否冒充 Google 應用程式?

不是. 只有無障礙服務的實作類別名稱用於相容實驗. 應用程式套件名稱, 應用程式及服務標籤, 圖示, 文件及簽章均保持真實, 並明確聲明它不是 Google Select to Speak.

#### 為何安裝後沒有任何變化?

沒有 root, WRITE_SECURE_SETTINGS 或 Shizuku 時, Android 不允許應用程式自行啟用無障礙服務, 使用者必須在系統設定啟用相容服務, 同時啟用 AutoJs6 服務; 具備其中一種能力後, 相容服務管理器會替你套用所選策略. 如仍無變化, 依 A-B-A 協定確認目前目標應用程式版本及頁面是否支援.

#### 為何支援頁面仍然沒有文字節點?

WebView, 小程式, Canvas 或自繪頁面可能沒有對應 Android 語意節點. 相容服務只能嘗試恢復被條件性隱藏的樹, 不能產生頁面原本沒有的資訊.

#### 使用外掛是否保證帳號安全?

不保證. 外掛不會繞過目標平台風控, 也無法為任何自動化行為提供安全承諾. 請使用低風險, 可稽核, 有人工確認的腳本, 並自行遵守平台規則.

### 研究依據

研究文件整理 AutoJs6 #289, #382, #432, #463, #520, #521, GKD `47267c7`, 以及測試裝置上微信 8.0.72 的去識別化靜態證據. 文件區分公開事實, 可重現實驗及推論, 不把微信內部實作描述為官方承諾.

[查看無障礙服務身分相容研究記錄](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/research/accessibility-service-identity-compat.md)

### 發行歷史

#### v1.4.0 - 2026/09/30

##### 提示

- 應用程式 ID 由 `io.github.supermonster003.autojs6.plugin.accessibilitycompat` 改為 `io.github.supermonster003.autojs6.plugin.three.adapt.a11y`, Android 會將本版本視為新應用程式: 請先解除安裝 Accessibility Compat 1.3.1 及更早版本, 再重新啟用 3-Adapt A11y 無障礙服務

##### 新增

- 統一獨立設定的平面分組, 列規格與置中圓角對話框. 語言, 夜間模式, 主題色與啟動器圖示均在確定後生效, 取消不改變已儲存的設定. 主題色預設跟隨 AutoJs6, 提供統一色盤, HEX/RGB 輸入與局部預覽; 中性底色保持穩定, 控制項遵循所選主題. 啟動器預設自適應自動, 升級保留明確儲存的選擇.

##### 修復

- 修復自動啟動器圖示在安裝時被固定為單一配色, 使啟動器仍可按設定讀取明暗資源.

##### 優化

- 外掛程式由 Accessibility Compat 更名為 3-Adapt A11y, 應用程式標題, 無障礙服務標籤, 外掛程式 ID `three-adapt-a11y`, 套件名稱與元件名稱, 發行產物, 文件及 GitHub 儲存庫同步更新
- 啟動器與外掛中心圖示按統一視覺尺寸標準調整, 外掛中心採用透明背景和黑白或中性灰階圖案

##### 相依性

- Material Components 1.13.0 / AppCompat 1.7.1 (Material 3).

[查看完整發行歷史](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/changelog/CHANGELOG-zh-Hant-TW.md)

### 建置及文件檢查

一般使用者應安裝 Releases 的成品 APK. 開發者可使用儲存庫 Gradle Wrapper 建置及驗證專案:

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug :app:assembleDebugAndroidTest :app:lintDebug
py .python\generate_markdown.py --check
```

README 及 changelog 由 JSON 文案源產生. 修改 `.readme/lang_*.json`, `.changelog/lang_*.json` 或範本後執行:

```powershell
py .python\generate_markdown.py
py .python\generate_markdown.py --check
```

### 授權

專案程式碼使用 [Mozilla Public License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/LICENSE). 微信, WeChat, Google 及 Select to Speak 名稱屬各自權利人所有, 本專案與這些公司沒有隸屬或認可關係.

### 相關連結

- [AutoJs6](https://github.com/SuperMonster003/AutoJs6)
- [查看無障礙服務身分相容研究記錄](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/research/accessibility-service-identity-compat.md)
- [查看完整 A-B-A 驗收協定](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/testing/aba-device-validation.md)
- [查看 QV710AF65F 完整去識別報告](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/testing/device-QV710AF65F.md)


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/16kb.md)
