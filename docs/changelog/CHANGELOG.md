<!-- This file is generated. Edit .changelog/lang_*.json and rerun .python/generate_markdown.py. -->

# Accessibility Compat 发行历史

> 本页语言: 简体中文

## v1.3.1 - 2026/09/19

### 修复

- AGP 9.1 构建时的 SDK XML v4 解析警告及 JVM 单元测试组装任务误触发 APK 原生库对齐检查的问题 (共享构建插件 1.8.3)

### 优化

- 宿主与兼容插件显示高级保护说明, 区分全局模式, 实际服务可用性和安全设置控制能力
- 继 compileSdk 之后将 targetSdk 提升到 37 (Android 17), 插件行为不受新目标版本影响

## v1.3.0 - 2026/09/15

### 新增

- 新增设置页面, 提供默认跟随 AutoJs6 的语言, 暗色模式和主题色选项, 并收纳兼容机制, 隐私, 限制, 发行历史与关于入口
- 新增兼容服务管理器, 提供跟随 AutoJs6, 启用, 禁用三选一控制策略, 可通过 root, WRITE_SECURE_SETTINGS 或 Shizuku 改变服务状态, 并显示实时状态与可复制的报告
- 通过宿主状态广播, 监听已启用服务列表的内容触发任务以及服务连接和应用打开时的检查, 自动跟随 AutoJs6 无障碍服务
- 通过插件信息能力和共享的无障碍伴生契约 (契约版本 3) 发布所选服务策略
- 新增关于应用与开发者页面, 提供版本, 包名, 开源许可证, 项目页面与反馈链接

### 修复

- 行标题与摘要按布局方向而非文字方向对齐, 语言对话框中的阿拉伯语选项不再远离其单选按钮

### 优化

- 重新设计主页面: 状态卡片直接打开管理器, 受支持应用附带启动按钮, 原有打开应用, 无障碍设置和刷新按钮移入设置页面
- 编译目标提升至 Android API 37 以匹配更新后的通用插件 API
- 服务管理器的策略选项与其他行对齐, 移除冗余的立即应用行 (选择策略后即立即应用), 并重新构建外观选项对话框以统一文字大小, 边距与间距

### 依赖

- 新增 Shizuku API 13.1.5 和 AndroidX Annotation 1.10.0
- 更新内置通用插件 API 以包含无障碍伴生契约

## v1.2.0 - 2026/09/13

### 新增

- 界面提供本地发行历史, 支持多语言及英语回退

### 优化

- 校验发行签名配置, 预期 APK 集合与可复现文档

## v1.1.0 - 2026/09/12

### 优化

- 将应用, 无障碍服务, 插件元数据, 内嵌说明及 README 文案统一改为应用无关的支持配置表达, 同时保留微信 (`com.tencent.mm`) 作为目前唯一通过验证的配置
- 将特定目标插件变体改为 `service-identity`, 以集合形式发布受支持包名, 并通过通用界面操作列出或打开已安装的受支持应用
- 完善自适应启动图标, 提供明暗主题变体及主题图标所需的单色图层
- 通用化 A-B-A 协议, 研究入口和隐私安全度量工具, 包括为未来支持配置提供明确的 `targetPackage` 输入
- 移除已过时的 Android Studio 与 IntelliJ IDEA 最低版本属性, IDE 与工具链兼容性现由中央机制选择
- 构建阶段阻止意外引入原生依赖, 并输出 JSON 校验报告

### 依赖

- 将 `io.github.supermonster003.autojs6-platform-versions` 从 1.7.0 升级至 1.7.3, 使 JDK 25 和 26 构建自动在根 buildscript classpath 对齐所选 KGP

## v1.0.0 - 2026/09/02

### 提示

- 这是实验性兼容方案. 效果取决于微信版本, 页面和远程配置, 发布版本不承诺在所有设备或账号上生效
- 兼容服务无法生成微信小程序, XWeb 或 Canvas 原本没有的语义节点, 也不会绕过登录, 风控或平台反滥用机制

### 新增

- 提供独立的 no-op 无障碍伴生 APK, 仅面向 `com.tencent.mm`, 并保持应用 ID, 图标, 标签, 描述和签名真实可辨
- 注册公开实验支持的兼容服务实现类名, 同时继续由 AutoJs6 自己的无障碍服务读取和操作节点, 伴生回调不读取用户内容
- 通过 AutoJs6 插件信息接口报告兼容模式, 目标包, 服务组件和最低宿主版本, 并提供用户主动启用及关闭服务的界面

### 优化

- 整理 [AutoJs6#289](https://github.com/SuperMonster003/AutoJs6/issues/289), [#382](https://github.com/SuperMonster003/AutoJs6/issues/382), [#432](https://github.com/SuperMonster003/AutoJs6/issues/432), [#463](https://github.com/SuperMonster003/AutoJs6/issues/463), [#520](https://github.com/SuperMonster003/AutoJs6/issues/520), [#521](https://github.com/SuperMonster003/AutoJs6/issues/521), GKD `47267c7` 和微信 8.0.72 脱敏静态证据
- 提供不记录私有节点文本或截图的 A-B-A 设备验收协议, 用重复统计和可逆结果区分兼容效果, 页面加载及缓存偶然性
- 建立 10 种语言的 README 与 changelog 文案源, 可复现 Markdown 生成器, 只读一致性检查及 GitHub Actions 门禁
- 记录 QV710AF65F 上由 AutoJs6 自身完成的 7 样本 A-B-A 实测, 节点数从 1 稳定升至 244 并在关闭兼容服务后回到 1, 同时验证系统无障碍设置精确恢复
