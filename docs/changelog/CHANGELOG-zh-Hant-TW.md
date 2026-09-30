<!-- This file is generated. Edit .changelog/lang_*.json and rerun .python/generate_markdown.py. -->

# 3-Adapt A11y 發行歷史

> 本頁語言: 台灣繁體中文

## v1.4.0 - 2026/09/30

### 提示

- 應用程式 ID 由 `io.github.supermonster003.autojs6.plugin.accessibilitycompat` 改為 `io.github.supermonster003.autojs6.plugin.three.adapt.a11y`, Android 會將本版本視為新應用程式: 請先解除安裝 Accessibility Compat 1.3.1 及更早版本, 再重新啟用 3-Adapt A11y 無障礙服務

### 新增

- 統一獨立設定的平面分組, 列規格與置中圓角對話框. 語言, 夜間模式, 主題色與啟動器圖示均在確定後生效, 取消不改變已儲存的設定. 主題色預設跟隨 AutoJs6, 提供統一色盤, HEX/RGB 輸入與局部預覽; 中性底色保持穩定, 控制項遵循所選主題. 啟動器預設自適應自動, 升級保留明確儲存的選擇.

### 修復

- 修復自動啟動器圖示在安裝時被固定為單一配色, 使啟動器仍可按設定讀取明暗資源.

### 優化

- 外掛程式由 Accessibility Compat 更名為 3-Adapt A11y, 應用程式標題, 無障礙服務標籤, 外掛程式 ID `three-adapt-a11y`, 套件名稱與元件名稱, 發行產物, 文件及 GitHub 儲存庫同步更新

### 相依性

- Material Components 1.13.0 / AppCompat 1.7.1 (Material 3).

## v1.3.1 - 2026/09/19

### 修復

- AGP 9.1 建置時的 SDK XML v4 解析警告及 JVM 單元測試組裝工作誤觸發 APK 原生程式庫對齊檢查的問題 (共用建置外掛 1.8.3)

### 優化

- 宿主與相容外掛顯示進階保護說明, 區分全域模式, 實際服務可用性及安全設定控制能力
- 繼 compileSdk 之後將 targetSdk 提升到 37 (Android 17), 外掛程式行為不受新目標版本影響

## v1.3.0 - 2026/09/15

### 新增

- 新增設定頁面, 提供預設跟隨 AutoJs6 的語言, 深色模式與主題色選項, 並收納相容機制, 隱私, 限制, 發行歷史與關於入口
- 新增相容服務管理器, 提供跟隨 AutoJs6, 啟用, 停用三選一控制策略, 可透過 root, WRITE_SECURE_SETTINGS 或 Shizuku 變更服務狀態, 並顯示即時狀態與可複製的報告
- 透過宿主狀態廣播, 監聽已啟用服務清單的內容觸發工作以及服務連線和應用程式開啟時的檢查, 自動跟隨 AutoJs6 無障礙服務
- 透過外掛資訊能力與共享的無障礙伴生契約 (契約版本 3) 發布所選服務策略
- 新增關於應用程式與開發者頁面, 提供版本, 套件名稱, 開放原始碼授權條款, 專案頁面與意見回饋連結

### 修復

- 列標題與摘要依版面方向而非文字方向對齊, 語言對話方塊中的阿拉伯語選項不再遠離其選項按鈕

### 優化

- 重新設計主頁面: 狀態卡片直接開啟管理器, 支援的應用程式附帶啟動按鈕, 原有開啟應用程式, 無障礙設定與重新整理按鈕移入設定頁面
- 編譯目標提升至 Android API 37 以配合更新後的通用外掛 API
- 服務管理器的策略選項與其他列對齊, 移除多餘的立即套用列 (選擇策略後即立即套用), 並重新建構外觀選項對話方塊以統一文字大小, 邊距與間距

### 相依性

- 新增 Shizuku API 13.1.5 與 AndroidX Annotation 1.10.0
- 更新內建通用外掛 API 以包含無障礙伴生契約

## v1.2.0 - 2026/09/13

### 新增

- 介面提供本地發行歷史, 支援多語言及英語回退

### 優化

- 校驗發行簽章設定, 預期 APK 集合與可重現文件

## v1.1.0 - 2026/09/12

### 優化

- 將應用程式, 無障礙服務, 外掛中繼資料, 內嵌說明及 README 文案統一改為與應用程式無關的支援設定表達, 同時保留微信 (`com.tencent.mm`) 作為目前唯一通過驗證的設定
- 將特定目標外掛變體改為 `service-identity`, 以集合形式發布支援套件名稱, 並透過通用介面操作列出或開啟已安裝的支援應用程式
- 完善自適應啟動圖示, 提供明暗主題變體及主題圖示所需的單色圖層
- 通用化 A-B-A 協定, 研究入口及隱私安全度量工具, 包括為未來支援設定提供明確的 `targetPackage` 輸入
- 移除已過時的 Android Studio 與 IntelliJ IDEA 最低版本屬性, IDE 與工具鏈相容性現由中央機制選擇
- 建置階段阻止意外引入原生相依套件, 並輸出 JSON 校驗報告

### 相依性

- 將 `io.github.supermonster003.autojs6-platform-versions` 從 1.7.0 升級至 1.7.3, 讓 JDK 25 與 26 建置自動在根 buildscript classpath 對齊所選 KGP

## v1.0.0 - 2026/09/02

### 提示

- 這是實驗性相容方案. 效果取決於微信版本, 頁面及遠端設定, 發行版本不保證在所有裝置或帳號生效
- 相容服務無法產生微信小程式, XWeb 或 Canvas 原本沒有的語意節點, 也不會繞過登入, 風控或平台反濫用機制

### 新增

- 提供獨立的 no-op 無障礙伴生 APK, 只面向 `com.tencent.mm`, 並保持應用程式 ID, 圖示, 標籤, 說明及簽章真實可辨
- 註冊公開實驗支援的相容服務實作類別名稱, 同時繼續由 AutoJs6 自己的無障礙服務讀取及操作節點, 伴生回呼不讀取使用者內容
- 透過 AutoJs6 外掛資訊介面回報相容模式, 目標套件, 服務元件及最低主程式版本, 並提供使用者主動啟用及關閉服務的介面

### 優化

- 整理 [AutoJs6#289](https://github.com/SuperMonster003/AutoJs6/issues/289), [#382](https://github.com/SuperMonster003/AutoJs6/issues/382), [#432](https://github.com/SuperMonster003/AutoJs6/issues/432), [#463](https://github.com/SuperMonster003/AutoJs6/issues/463), [#520](https://github.com/SuperMonster003/AutoJs6/issues/520), [#521](https://github.com/SuperMonster003/AutoJs6/issues/521), GKD `47267c7` 及微信 8.0.72 去識別化靜態證據
- 提供不記錄私人節點文字或螢幕截圖的 A-B-A 裝置驗收協定, 以重複統計及可逆結果區分相容效果, 頁面載入及快取偶然性
- 建立 10 種語言的 README 及 changelog 文案源, 可重現 Markdown 產生器, 唯讀一致性檢查及 GitHub Actions 門禁
- 記錄 QV710AF65F 上由 AutoJs6 本身完成的 7 次取樣 A-B-A 實測, 節點數從 1 穩定增加到 244 並在關閉相容服務後回到 1, 同時驗證系統無障礙設定精確還原
