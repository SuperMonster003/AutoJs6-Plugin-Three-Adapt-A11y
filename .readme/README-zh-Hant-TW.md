<!-- This file is generated. Edit .readme/lang_*.json and rerun .python/generate_markdown.py. -->
<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-accessibility-compat-icon" border="0" width="128" />
  </p>
  <h1>Accessibility Compat</h1>
  <p>為受影響的微信版本提供隱私最小化的無障礙相容觸發器</p>
  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/issues"><img alt="GitHub issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat?color=534BAE&label=License"/></a>
  </p>
</div>

> 本頁語言: 台灣繁體中文

### 語言 (Languages)

[简体中文](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-zh-Hans.md) | [香港繁體](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-zh-Hant-HK.md) | [台灣繁體](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-zh-Hant-TW.md) | [English](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-en.md) | [Français](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-fr.md) | [Español](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-es.md) | [日本語](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-ja.md) | [한국어](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-ko.md) | [Русский](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-ru.md) | [العربية](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-ar.md)

### 專案狀態

Accessibility Compat 是獨立的實驗性伴生 APK. 它嘗試讓受影響的微信版本重新向 AutoJs6 無障礙服務提供控制項樹. 它不是通用自動化引擎, 也不會代替 AutoJs6 讀取或操作控制項.

> 相容效果由微信版本及遠端設定決定. 本專案不保證每個版本, 頁面, 帳號或裝置都有效. 請先完成 A-B-A 驗收再用於任何腳本.

### No-op 伴生架構

專案將微信相容實驗隔離於獨立 APK, 保持 AutoJs6 核心服務名稱及通用行為不變:

- AutoJs6 自己的無障礙服務仍是唯一讀取, 查詢及操作節點的元件.
- 伴生 APK 註冊一個已獲公開實證識別的服務實作類別名稱, 但應用程式 ID, 圖示, 標籤, 說明及簽章均如實標識為 Accessibility Compat.
- 相容服務的事件回呼是 no-op. 它不讀取 `event.source`, `event.text`, `rootInActiveWindow`, 螢幕截圖或頁面內容.
- 本版本不是節點代理或 Binder 橋接. 它只嘗試觸發微信的全域節點提供行為.

### 安裝及使用

1. 確認裝置為 Android 7.0 (API 24) 或以上, AutoJs6 內部版本號不低於 3923.
2. 只從本專案 [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/releases) 或可信的 AutoJs6 外掛入口安裝 APK.
3. 開啟 Accessibility Compat, 核對真實應用程式名稱及用途, 再依指引進入 Android 無障礙設定.
4. 由使用者手動啟用 Accessibility Compat 對應的無障礙服務. Android 顯示無障礙風險提示是正常現象, 不要使用 ADB 繞過確認.
5. 同時保持 AutoJs6 無障礙服務啟用, 重新進入目標微信頁面, 再從 AutoJs6 版面分析器或腳本讀取節點.

只安裝 APK 不會生效. 不使用時請在系統無障礙設定關閉相容服務, 如不再需要可直接解除安裝.

### A-B-A 裝置驗收

不要把一次成功 dump 當作結論. 在同一靜止頁面執行 A-B-A 對照, 才可把變化與相容服務關聯:

- A: 關閉相容服務, 保持 AutoJs6 服務啟用, 連續收集至少 5 次去識別化指標.
- B: 只啟用相容服務, 返回同一頁面, 再連續收集至少 5 次.
- A2: 再次關閉相容服務並重複收集, 確認變化可以逆轉, 排除頁面載入及快取偶然性.
- 記錄節點數, 非空 text/desc/resource-id 數量, clickable 數及結構 hash, 不保留聊天文字, 聯絡人名稱或螢幕截圖.

[查看完整 A-B-A 驗收協定](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/aba-device-validation.md)

### 實機驗證結果

2026-09-02 在已授權裝置 Sony XQ-AT72, Android 12/API 31 上完成可逆 A-B-A 驗收. 微信為 8.0.72 code 3085, AutoJs6 為 6.8.0 code 5277, 原有 6 個無障礙服務維持不變. 每個階段都強制停止並重新啟動微信 LauncherUI, 等待超過 5 秒, 再由 AutoJs6 本身連續取樣 7 次:

- A 相容服務關閉: nodes 1, text 0, desc 0, id 0, clickable 0, 7 次結構 hash 一致.
- B 相容服務開啟且兩個目標服務均已 bound: nodes 244, text 31, desc 13, id 157, clickable 38, 7 次結構 hash 一致.
- A2 再次關閉: 精確回到 nodes 1 且其餘指標為 0, 7 次結構 hash 一致. 測試後系統無障礙設定已精確還原.

這證明相容觸發器在該裝置, 微信版本及 LauncherUI 頁面組合上有效. 結果不能外推到其他版本, 帳號, 裝置, 小程式, XWeb 或 Canvas 頁面.

[查看 QV710AF65F 完整去識別報告](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/device-QV710AF65F.md)

### 已知限制

- 相容目標只限 `com.tencent.mm`. 它不應被視為其他應用程式的通用無障礙修補程式.
- 微信小程式, XWeb, Canvas 及自繪控制項可能根本沒有原生語意節點. 本外掛無法憑空補出缺少的 `text` 或 `content-desc`.
- 即使根節點恢復, 部分頁面仍可能返回空屬性, 過時節點, 隨機樹或只有邊界資訊.
- 微信升級或遠端設定變更可隨時使目前實作失效. 降級, OCR 或座標備援方案也各有安全及穩定性代價.
- 本外掛只處理可觀察性, 不會繞過登入, 風控, 驗證碼, 權限, 帳號限制或平台反濫用機制.

### 隱私邊界

- 相容回呼不讀取事件來源, 事件文字, 活動視窗根節點或螢幕影像.
- 應用程式不會上傳, 持久儲存或記錄微信頁面內容, 也不包含網路或分析功能.
- 應用程式不申請儲存空間, 懸浮視窗, 相機, 麥克風或媒體權限.
- 外掛資訊 Binder 只回報版本, 身分及能力中繼資料, 不傳輸節點樹.
- 無障礙服務由使用者在 Android 設定中明確啟用及關閉, 專案不會靜默變更系統設定.

### 倫理及合規

無障礙相容能力只應協助使用者自動化其獲授權操作的介面. 使用者須對腳本行為及帳號後果負責.

- 只在自己的裝置, 帳號及獲明確授權的流程使用.
- 不得用於騷擾, 群發垃圾訊息, 未經同意的資料收集, 監控他人或規避安全控制.
- 遵守適用法律, 微信規則及組織政策, 並為介面變更及誤操作設定人工確認及停止條件.
- 分享診斷時只發布彙總統計及去識別化結構, 不公開聊天內容, 聯絡人, token, APK 或原始 dex.

### 相容資訊

服務元件名稱包含公開實驗使用的相容類別名稱. 這不是 Google Select to Speak, 不提供朗讀功能, 也不冒充 Google 應用程式或簽章. 真實身分始終由本專案的應用程式 ID, 標籤, 圖示, 資訊頁及簽章公開展示.

```text
application id: io.github.supermonster003.autojs6.plugin.accessibilitycompat
accessibility service: io.github.supermonster003.autojs6.plugin.accessibilitycompat/com.google.android.accessibility.selecttospeak.SelectToSpeakService
target package: com.tencent.mm
minimum Android: Android 7.0 (API 24)
minimum AutoJs6 build: 3923
```

### 常見問題

#### 這個專案是否冒充 Google 應用程式?

不是. 只有無障礙服務的實作類別名稱用於相容實驗. 應用程式套件名稱, 應用程式及服務標籤, 圖示, 文件及簽章均保持真實, 並明確聲明它不是 Google Select to Speak.

#### 為何安裝後沒有任何變化?

Android 不允許應用程式自行啟用無障礙服務. 使用者必須在系統設定啟用相容服務, 同時啟用 AutoJs6 服務. 如仍無變化, 依 A-B-A 協定確認目前微信版本及頁面是否支援.

#### 為何微信小程式仍然沒有文字節點?

小程式或 XWeb 頁面可能透過 Canvas 或自繪方式呈現內容, 底層沒有對應 Android 語意節點. 相容服務只能嘗試恢復被條件性隱藏的樹, 不能產生頁面原本沒有的資訊.

#### 使用外掛是否保證帳號安全?

不保證. 外掛不會繞過微信風控, 也無法為任何自動化行為提供安全承諾. 請使用低風險, 可稽核, 有人工確認的腳本, 並自行遵守平台規則.

### 研究依據

研究文件整理 AutoJs6 #289, #382, #432, #463, #520, #521, GKD `47267c7`, 以及測試裝置上微信 8.0.72 的去識別化靜態證據. 文件區分公開事實, 可重現實驗及推論, 不把微信內部實作描述為官方承諾.

[查看微信無障礙相容研究記錄](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/research/wechat-accessibility-compat.md)

### 發行歷史

#### v1.0.0 - 2026/09/02

##### 提示

- 這是實驗性相容方案. 效果取決於微信版本, 頁面及遠端設定, 發行版本不保證在所有裝置或帳號生效
- 相容服務無法產生微信小程式, XWeb 或 Canvas 原本沒有的語意節點, 也不會繞過登入, 風控或平台反濫用機制

##### 新增

- 提供獨立的 no-op 無障礙伴生 APK, 只面向 `com.tencent.mm`, 並保持應用程式 ID, 圖示, 標籤, 說明及簽章真實可辨
- 註冊公開實驗支援的相容服務實作類別名稱, 同時繼續由 AutoJs6 自己的無障礙服務讀取及操作節點, 伴生回呼不讀取使用者內容
- 透過 AutoJs6 外掛資訊介面回報相容模式, 目標套件, 服務元件及最低主程式版本, 並提供使用者主動啟用及關閉服務的介面

##### 優化

- 整理 [AutoJs6#289](https://github.com/SuperMonster003/AutoJs6/issues/289), [#382](https://github.com/SuperMonster003/AutoJs6/issues/382), [#432](https://github.com/SuperMonster003/AutoJs6/issues/432), [#463](https://github.com/SuperMonster003/AutoJs6/issues/463), [#520](https://github.com/SuperMonster003/AutoJs6/issues/520), [#521](https://github.com/SuperMonster003/AutoJs6/issues/521), GKD `47267c7` 及微信 8.0.72 去識別化靜態證據
- 提供不記錄私人節點文字或螢幕截圖的 A-B-A 裝置驗收協定, 以重複統計及可逆結果區分相容效果, 頁面載入及快取偶然性
- 建立 10 種語言的 README 及 changelog 文案源, 可重現 Markdown 產生器, 唯讀一致性檢查及 GitHub Actions 門禁
- 記錄 QV710AF65F 上由 AutoJs6 本身完成的 7 次取樣 A-B-A 實測, 節點數從 1 穩定增加到 244 並在關閉相容服務後回到 1, 同時驗證系統無障礙設定精確還原

[查看完整發行歷史](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/changelog/CHANGELOG-zh-Hant-TW.md)

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

專案程式碼使用 [Mozilla Public License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/LICENSE). 微信, WeChat, Google 及 Select to Speak 名稱屬各自權利人所有, 本專案與這些公司沒有隸屬或認可關係.

### 相關連結

- [AutoJs6](https://github.com/SuperMonster003/AutoJs6)
- [查看微信無障礙相容研究記錄](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/research/wechat-accessibility-compat.md)
- [查看完整 A-B-A 驗收協定](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/aba-device-validation.md)
- [查看 QV710AF65F 完整去識別報告](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/device-QV710AF65F.md)
