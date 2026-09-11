<!-- This file is generated. Edit .changelog/lang_*.json and rerun .python/generate_markdown.py. -->

# Accessibility Compat 發行歷史

> 本頁語言: 香港繁體中文

## v1.1.0 - 2026/09/11

### 優化

- 將應用程式, 無障礙服務, 插件中繼資料, 內嵌說明及 README 文案統一改為應用程式無關的支援配置表達, 同時保留微信 (`com.tencent.mm`) 作為目前唯一通過驗證的配置
- 將特定目標插件變體改為 `service-identity`, 以集合形式發佈受支援套件名稱, 並透過通用介面操作列出或開啟已安裝的受支援應用程式
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
