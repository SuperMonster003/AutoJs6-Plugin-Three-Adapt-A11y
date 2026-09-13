<!-- This file is generated. Edit .readme/lang_*.json and rerun .python/generate_markdown.py. -->
<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-accessibility-compat-icon" border="0" width="128" />
  </p>
  <h1>Accessibility Compat</h1>
  <p>面向受支持应用的隐私最小化无障碍兼容触发器</p>
  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/issues"><img alt="GitHub issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat?color=534BAE&label=License"/></a>
  </p>
</div>

> 本页语言: 简体中文

### 语言 (Languages)

[简体中文](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-zh-Hans.md) | [香港繁體](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-zh-Hant-HK.md) | [台灣繁體](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-zh-Hant-TW.md) | [English](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-en.md) | [Français](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-fr.md) | [Español](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-es.md) | [日本語](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-ja.md) | [한국어](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-ko.md) | [Русский](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-ru.md) | [العربية](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/.readme/README-ar.md)

### 项目状态

Accessibility Compat 是一个独立的实验性伴生 APK. 它尝试帮助已发布支持配置覆盖的应用向 AutoJs6 无障碍服务公开控件树. 它不是通用自动化引擎, 也不会代替 AutoJs6 读取或操作控件.

> 兼容效果由各目标应用版本, 页面, 设备和远程配置决定. 本项目不承诺在所有环境中有效. 请先完成 A-B-A 验收再用于任何脚本.

### No-op 伴生架构

项目将应用兼容配置隔离在独立 APK 中, 保持 AutoJs6 核心服务名称和通用行为不变:

- AutoJs6 自己的无障碍服务仍是唯一读取, 查询和操作节点的组件.
- 伴生 APK 注册一个已被公开实证识别的服务实现类名, 但应用 ID, 图标, 标签, 描述和签名都如实标识为 Accessibility Compat.
- 兼容服务的事件回调是 no-op. 它不读取 `event.source`, `event.text`, `rootInActiveWindow`, 截图或页面内容.
- 本版本不是节点代理或 Binder 桥. 它只尝试触发支持配置中记录的条件性节点公开行为.

### 安装与使用

1. 确认设备为 Android 7.0 (API 24) 及以上, AutoJs6 内部版本号不低于 3923.
2. 仅从本项目 [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/releases) 或可信的 AutoJs6 插件入口安装 APK.
3. 打开 Accessibility Compat, 核对真实应用名称和用途, 再按引导进入 Android 无障碍设置.
4. 由用户手动启用 Accessibility Compat 对应的无障碍服务. Android 显示无障碍风险提示是正常现象, 不要使用 ADB 绕过确认.
5. 同时保持 AutoJs6 的无障碍服务启用, 重新进入支持配置所列目标页面, 再从 AutoJs6 布局分析器或脚本读取节点.

安装 APK 本身不会生效. 不使用时请在系统无障碍设置中关闭兼容服务, 如不再需要可直接卸载.

### A-B-A 设备验收

不要以一次成功 dump 作为结论. 在同一静止页面执行 A-B-A 对照, 才能把变化与兼容服务关联起来:

- A: 关闭兼容服务, 保持 AutoJs6 服务开启, 连续采集至少 5 次脱敏指标.
- B: 只开启兼容服务, 回到同一页面, 再连续采集至少 5 次.
- A2: 再次关闭兼容服务并重复采集, 确认变化能够逆转, 排除页面加载和缓存偶然性.
- 记录节点数, 非空 text/desc/resource-id 数量, clickable 数和结构 hash, 不保存聊天文本, 联系人名或截图.

[查看完整 A-B-A 验收协议](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/aba-device-validation.md)

### 实机验证结果

2026-09-02 在授权设备 Sony XQ-AT72, Android 12/API 31 上完成了可逆 A-B-A 验收. 微信为 8.0.72 code 3085, AutoJs6 为 6.8.0 code 5277, 既有 6 个无障碍服务保持不变. 每阶段均强制停止并重启微信 LauncherUI, 等待超过 5 秒, 再由 AutoJs6 自身连续采样 7 次:

- A 兼容服务关闭: nodes 1, text 0, desc 0, id 0, clickable 0, 7 次结构 hash 一致.
- B 兼容服务开启且两个目标服务均已 bound: nodes 244, text 31, desc 13, id 157, clickable 38, 7 次结构 hash 一致.
- A2 再次关闭: 精确回到 nodes 1 且其余指标为 0, 7 次结构 hash 一致. 后续又完成 10 轮首页与第一条会话聊天页切换, 共 140 个样本: 首页始终为 244 个节点, 聊天页始终为 111 或 113 个节点, 没有任何一次退回 1. 测试后系统无障碍设置已精确恢复.

这证明兼容触发器在该设备, 微信版本, LauncherUI 首页和本次第一条会话聊天页面组合上有效. 结果不能外推到其他版本, 账号, 设备, 其他聊天, 小程序, XWeb 或 Canvas 页面.

[查看 QV710AF65F 完整脱敏报告](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/device-QV710AF65F.md)

### 已知限制

- 当前支持配置仅包含微信 (`com.tencent.mm`). 这是明确的支持集合, 不代表宣称通用于所有应用.
- WebView, 小程序, Canvas 和自绘控件可能根本没有原生语义节点. 本插件无法凭空补出缺失的 `text` 或 `content-desc`.
- 即使根节点恢复, 某些页面仍可能返回空属性, 陈旧节点, 随机树或只包含边界信息.
- 目标应用升级或远程配置变化可随时让对应支持配置失效. 降级, OCR 或坐标方案也各有安全和稳定性代价.
- 本插件只处理可观察性, 不会绕过登录, 风控, 验证码, 权限, 账号限制或平台反滥用机制.

### 隐私边界

- 兼容回调不读取事件来源, 事件文本, 活动窗口根节点或屏幕图像.
- 应用不上传, 持久化或记录目标应用页面内容, 也不包含网络或分析功能.
- 应用不申请存储, 悬浮窗, 相机, 麦克风或媒体权限.
- 插件信息 Binder 只报告版本, 身份和能力元数据, 不传输节点树.
- 无障碍服务由用户在 Android 设置中明确启用和关闭, 项目不会静默更改系统设置.

### 伦理与合规

无障碍兼容能力只应帮助用户自动化其有权操作的界面. 使用者对脚本行为和账号后果负责.

- 仅在自己的设备, 账号和获得明确授权的流程中使用.
- 不得用于骚扰, 群发垃圾信息, 未经同意的数据采集, 监控他人或规避安全控制.
- 遵守适用法律, 目标平台规则和组织政策, 并为界面变化和误操作设置人工确认及停止条件.
- 分享诊断时只发布聚合统计和脱敏结构, 不公开聊天内容, 联系人, token, APK 或原始 dex.

### 兼容信息

服务组件名包含公开实验使用的兼容类名. 这不是 Google Select to Speak, 不提供朗读功能, 也不冒充 Google 应用或签名. 真实身份始终由本项目的应用 ID, 标签, 图标, 说明页和签名公开展示.

```text
application id: io.github.supermonster003.autojs6.plugin.accessibilitycompat
accessibility service: io.github.supermonster003.autojs6.plugin.accessibilitycompat/com.google.android.accessibility.selecttospeak.SelectToSpeakService
supported packages: com.tencent.mm
minimum Android: Android 7.0 (API 24)
minimum AutoJs6 build: 3923
```

### 常见问题

#### 这个项目是否冒充 Google 应用?

不是. 只有无障碍服务的实现类名用于兼容实验. 应用包名, 应用和服务标签, 图标, 文档与签名都保持真实, 并明确声明它不是 Google Select to Speak.

#### 为什么安装后没有任何变化?

安装 APK 不会自动启用服务. 必须在 Android 系统设置中同时启用 Accessibility Compat 与 AutoJs6, 并确认应用首页显示兼容服务已启用. 如果目标包节点结果仍异常, 先关闭并重新启用兼容服务, 等待至少 10 秒后完全退出并重新进入目标页面. 只有两个服务都已启用且实际 bound 后, 才按 A-B-A 协议判断当前应用版本和页面是否受支持.

#### 为什么受支持页面仍然没有文字节点?

WebView, 小程序, Canvas 或自绘页面可能没有对应的 Android 语义节点. 兼容服务只能尝试恢复被条件性隐藏的树, 不能生成页面本来没有的信息.

#### 使用插件是否保证账号安全?

不保证. 插件不会绕过目标平台风控, 也无法替任何自动化行为作安全承诺. 应使用低风险, 可审计, 有人工确认的脚本, 并自行遵守平台规则.

### 研究依据

研究文档整理 AutoJs6 #289, #382, #432, #463, #520, #521, GKD `47267c7`, 以及测试设备上微信 8.0.72 的脱敏静态证据. 它区分公开事实, 可复现实验和推断, 不把微信内部实现描述为官方承诺.

[查看无障碍服务身份兼容研究记录](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/research/accessibility-service-identity-compat.md)

### 发行历史

#### v1.2.0 - 2026/09/13

##### 新增

- 界面提供本地发行历史, 支持多语言及英语回退

##### 优化

- 校验发行签名配置, 预期 APK 集合与可复现文档

[查看完整发行历史](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/changelog/CHANGELOG-zh-Hans.md)

### 构建与文档校验

普通用户应安装 Releases 中的成品 APK. 开发者可使用仓库 Gradle Wrapper 构建并验证项目:

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug :app:assembleDebugAndroidTest :app:lintDebug
py .python\generate_markdown.py --check
```

README 和 changelog 由 JSON 文案源生成. 修改 `.readme/lang_*.json`, `.changelog/lang_*.json` 或模板后运行:

```powershell
py .python\generate_markdown.py
py .python\generate_markdown.py --check
```

### 许可

项目代码使用 [Mozilla Public License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/LICENSE). 微信, WeChat, Google 和 Select to Speak 名称归各自权利人所有, 本项目与这些公司不存在隶属或认可关系.

### 相关链接

- [AutoJs6](https://github.com/SuperMonster003/AutoJs6)
- [查看无障碍服务身份兼容研究记录](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/research/accessibility-service-identity-compat.md)
- [查看完整 A-B-A 验收协议](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/aba-device-validation.md)
- [查看 QV710AF65F 完整脱敏报告](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/testing/device-QV710AF65F.md)


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat/blob/master/docs/16kb.md)
