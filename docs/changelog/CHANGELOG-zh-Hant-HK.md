<!-- This file is generated. Edit .changelog/lang_*.json and rerun .python/generate_markdown.py. -->

# Accessibility Compat 發行歷史

> 本頁語言: 香港繁體中文

## v1.3.0 - 2026/09/15

### 新增

- 新增設定頁面, 提供預設跟隨 AutoJs6 的語言, 深色模式及主題色選項, 並收納兼容機制, 私隱, 限制, 發行歷史及關於入口
- 新增兼容服務管理器, 提供跟隨 AutoJs6, 啟用, 停用三選一控制策略, 可透過 root, WRITE_SECURE_SETTINGS 或 Shizuku 變更服務狀態, 並顯示即時狀態及可複製的報告
- 透過宿主狀態廣播, 監聽已啟用服務清單的內容觸發工作以及服務連線和應用程式開啟時的檢查, 自動跟隨 AutoJs6 無障礙服務
- 透過插件資訊能力及共享的無障礙伴生契約 (契約版本 3) 發佈所選服務策略
- 新增關於應用程式與開發者頁面, 提供版本, 套件名稱, 開源許可證, 專案頁面與意見回饋連結

### 修復

- 列標題與摘要按佈局方向而非文字方向對齊, 語言對話框中的阿拉伯語選項不再遠離其單選按鈕

### 優化

- 重新設計主頁面: 狀態卡片直接開啟管理器, 受支援應用程式附帶啟動按鈕, 原有開啟應用程式, 無障礙設定及重新整理按鈕移入設定頁面
- 編譯目標提升至 Android API 37 以配合更新後的通用插件 API
- 服務管理器的策略選項與其他列對齊, 移除多餘的立即套用列 (選擇策略後即立即套用), 並重新建構外觀選項對話框以統一文字大小, 邊距與間距

### 依賴

- 新增 Shizuku API 13.1.5 及 AndroidX Annotation 1.10.0
- 更新內置通用插件 API 以包含無障礙伴生契約

## v1.2.0 - 2026/09/13

### 新增

- 介面提供本地發行歷史, 支援多語言及英語回退

### 優化

- 校驗發行簽署設定, 預期 APK 集合與可重現文件

## v1.1.0 - 2026/09/12

### 優化

- 將應用程式, 無障礙服務, 插件中繼資料, 內嵌說明及 README 文案統一改為應用程式無關的支援配置表達, 同時保留微信 (`com.tencent.mm`) 作為目前唯一通過驗證的配置
- 將特定目標插件變體改為 `service-identity`, 以集合形式發佈受支援套件名稱, 並透過通用介面操作列出或開啟已安裝的受支援應用程式
- 完善自適應啟動圖示, 提供明暗主題變體及主題圖示所需的單色圖層
- 通用化 A-B-A 協議, 研究入口及私隱安全度量工具, 包括為未來支援配置提供明確的 `targetPackage` 輸入
- 移除已過時的 Android Studio 及 IntelliJ IDEA 最低版本屬性, IDE 與工具鏈兼容性現由中央機制選擇
- 建置階段阻止意外引入原生相依套件, 並輸出 JSON 校驗報告

### 依賴

- 將 `io.github.supermonster003.autojs6-platform-versions` 由 1.7.0 升級至 1.7.3, 讓 JDK 25 及 26 構建自動在根 buildscript classpath 對齊所選 KGP

## v1.0.0 - 2026/09/02

### 提示

- 這是實驗性兼容方案. 效果取決於微信版本, 頁面及遠端配置, 發行版本不保證在所有裝置或帳號生效
- 兼容服務無法產生微信小程序, XWeb 或 Canvas 原本沒有的語義節點, 亦不會繞過登入, 風控或平台反濫用機制

### 新增

- 提供獨立的 no-op 無障礙伴生 APK, 只面向 `com.tencent.mm`, 並保持應用程式 ID, 圖示, 標籤, 說明及簽名真實可辨
- 註冊公開實驗支援的兼容服務實現類別名稱, 同時繼續由 AutoJs6 自己的無障礙服務讀取及操作節點, 伴生回調不讀取使用者內容
- 透過 AutoJs6 插件資訊介面報告兼容模式, 目標套件, 服務組件及最低主程式版本, 並提供使用者主動啟用及關閉服務的介面

### 優化

- 整理 [AutoJs6#289](https://github.com/SuperMonster003/AutoJs6/issues/289), [#382](https://github.com/SuperMonster003/AutoJs6/issues/382), [#432](https://github.com/SuperMonster003/AutoJs6/issues/432), [#463](https://github.com/SuperMonster003/AutoJs6/issues/463), [#520](https://github.com/SuperMonster003/AutoJs6/issues/520), [#521](https://github.com/SuperMonster003/AutoJs6/issues/521), GKD `47267c7` 及微信 8.0.72 脫敏靜態證據
- 提供不記錄私人節點文字或螢幕截圖的 A-B-A 裝置驗收協議, 以重複統計及可逆結果區分兼容效果, 頁面載入及快取偶然性
- 建立 10 種語言的 README 及 changelog 文案源, 可重現 Markdown 生成器, 唯讀一致性檢查及 GitHub Actions 門禁
- 記錄 QV710AF65F 上由 AutoJs6 本身完成的 7 次取樣 A-B-A 實測, 節點數由 1 穩定增加至 244 並在關閉兼容服務後返回 1, 同時驗證系統無障礙設定精確恢復
