# 无障碍服务身份兼容研究记录

更新日期: 2026-09-02

## 结论摘要

本项目研究一类可检验的兼容机制: 某些目标应用可能根据已启用无障碍服务的实现类名或组件 ID 后半段, 条件性地改变向 Android 无障碍框架暴露的节点树. 当前唯一具有充分公开证据和实机验证的支持配置是微信. 该配置的判断来自 AutoJs6 社区复现, GKD 的单变量兼容提交, 以及测试设备上微信 8.0.72 APK 的静态观察.

这不是腾讯公开的接口或兼容承诺. 白名单的完整内容, 远程配置规则, 版本阈值, 灰度范围及账号差异都可能变化. 因此本项目采用独立, 可卸载, no-op 的伴生 APK, 不修改 AutoJs6 核心服务名称, 并要求通过 A-B-A 对照验证效果.

该假设只解释一类问题. WebView, 小程序, Canvas 或自绘界面可能本来就没有完整的 Android 语义节点. 即使兼容触发器恢复了根节点, 也无法凭空生成页面未提供的 `text`, `content-desc` 或可点击语义.

## 支持配置

| 应用 | 包名 | 验证范围 | 状态 |
|---|---|---|---|
| 微信 | `com.tencent.mm` | 8.0.72, LauncherUI 首页及第一条会话聊天页, Sony XQ-AT72 / Android 12 | 已验证 |

新应用只有在独立 A-B-A 复现及证据文档完成后才能加入此表. 插件名称, 应用 ID, 可见服务名称, 插件元数据和测试工具均保持应用无关; 应用专属事实只进入支持配置和证据章节.

## 证据等级

| 等级 | 含义 | 本记录中的例子 |
|---|---|---|
| 高 | 可从公开提交或指定 APK 中直接复核的事实 | GKD `47267c7` 的服务类名变更, 微信 8.0.72 的静态字符串及引用位置 |
| 中 | 多个独立复现相互支持, 但缺少目标应用官方说明 | 启用另一无障碍服务后 AutoJs6 重新看到节点树 |
| 低或未知 | 合理推断, 尚未形成稳定单变量复现 | 远程灰度比例, 完整白名单, 具体生效版本阈值 |

## AutoJs6 Issues 快照

状态为 2026-09-02 调查时的 GitHub 页面快照. Issue 报告与评论是复现线索, 不等同于腾讯或 AutoJs6 维护者对根因的官方确认.

| Issue | 状态 | 可用证据 | 对设计的影响 |
|---|---|---|---|
| [#289 关于微信控件显示不正确的问题](https://github.com/SuperMonster003/AutoJs6/issues/289) | Open | 微信 8.0.54 和 8.0.55 在同一静止控件上可能随机正确或返回旧 `desc`; AutoX.js v7 也异常; 降到 8.0.47 后恢复 | 问题不能简单归因于 AutoJs6 `UiSelector`; 验收必须重复采样并检测陈旧树 |
| [#382 新版微信无法获取布局](https://github.com/SuperMonster003/AutoJs6/issues/382) | Open, Duplicate, Helpless | 报告微信 8.0.56 到 8.0.58 布局分析异常; 维护者不赞成为单一应用修改核心通用行为 | 将兼容实验隔离到独立伴生 APK, 保持 AutoJs6 核心真实且通用 |
| [#432 除降低版本外是否有其他方法](https://github.com/SuperMonster003/AutoJs6/issues/432) | Open, Duplicate, Helpless | 服务类名方法可解除部分混淆, 但聊天中肉眼可见的文字仍可能没有 `text` 或 `desc` | 明确两层故障模型, 不把类名兼容描述为完整节点恢复 |
| [#463 微信 8.0.58 后无法布局分析](https://github.com/SuperMonster003/AutoJs6/issues/463) | Open | 维护者说明观察到匹配服务 ID 后半段, 反对在核心中采用误导名称, 并提出独立 APK 或可选 Binder 的折中; 讨论中还有 Hamibot 与 AutoJs6 同时启用后后者恢复的复现 | 直接形成当前 no-op 伴生架构与真实身份边界 |
| [#520 开启其他无障碍后可获取微信布局](https://github.com/SuperMonster003/AutoJs6/issues/520) | Closed | 报告者称 AutoJs6 单独启用时看不到微信布局, 启用另一应用的无障碍后又能看到; Issue 约 9 分钟后由报告者自行关闭且没有评论 | 是全局触发假设的旁证, 不是维护者确认, 也不能单独作为验收结论 |
| [#521 微信 8.0.70 小程序按钮无节点](https://github.com/SuperMonster003/AutoJs6/issues/521) | Open | 报告微信 8.0.70 小程序按钮节点缺失; 只有社区回复称 8.0.59 后存在类似情况, 没有维护者确认 | 小程序和 XWeb 必须作为独立子问题测试, 不能以主界面成功代替 |

## GKD 受控变更

[gkd-kit/gkd@47267c7](https://github.com/gkd-kit/gkd/commit/47267c7ceb802a2f083eec98ee50ad03f24f2f38) 于 2025-04-04 提交, 标题为 `feat: compat higher version wechat`. 该提交提供了接近单变量的公开实证:

- Manifest 中无障碍服务实现由 `.service.A11yService` 改为 `com.google.android.accessibility.selecttospeak.SelectToSpeakService`.
- 新类位于上述包名, 并继承原来的 `A11yService`, 因而原服务逻辑仍被复用.
- Accessibility metadata 没有随这一兼容点改变.
- GKD 的 application ID 仍为 `li.songe.gkd`; 自有应用标签, 图标和签名没有变成 Google 身份.
- 其余变更主要用于让 GKD 以新组件 ID 检测自身服务状态.

这支持受影响微信版本识别 service implementation FQCN 或组件 ID 后半段的判断. 它不支持 "微信校验 Google 应用包名, Google 签名或 Google 品牌" 的说法, 也不能证明未来版本继续采用相同逻辑.

## 支持配置案例: 微信 8.0.72 APK 静态观察

### 样本身份

样本来自用户授权的测试设备, 仅在本地临时分析. APK, 解包目录和原始 dex 均未加入仓库.

| 字段 | 值 |
|---|---|
| 测试设备 | `QV710AF65F`, Sony `XQ-AT72`, Android 12, API 31, `arm64-v8a` |
| 包名 | `com.tencent.mm` |
| 版本 | 8.0.72 |
| versionCode | 3085 |
| targetSdk | 35 |
| `base.apk` 大小 | 194,770,301 bytes |
| `base.apk` SHA-256 | `59CFC54474ED23FF7276D1EEA36EA779DB7E6893D7B2CB925F28A0F3301784BE` |

### 静态结果

Dex 静态分析定位到 `com.tencent.mm.accessibility.feature.AccExptServiceKt`. 在该版本样本中观察到:

- 默认服务白名单字符串包含 `com.google.android.accessibility.selecttospeak.SelectToSpeakService`.
- 同一默认字符串还包含 `com.dianming.phoneapp.MyAccessibilityService`.
- 与白名单相关的配置键为 `clicfg_acc_white_service_list`.
- 检查间隔常量为 5000 ms.

这些是指定哈希 APK 的静态事实, 不是腾讯公开承诺. `clicfg` 命名和默认值表明配置可能被远程或实验配置覆盖, 但仅凭静态代码无法确定服务器下发值, 生效范围, 缓存生命周期或所有运行路径.

为保护用户隐私和第三方版权, 本仓库不包含 APK, dex, 反编译源码, 聊天文本, 联系人名称, 页面截图或完整 UI dump. 复核时应只对用户有权分析的 APK 进行本地检查, 并先核对文件大小和 SHA-256.

## 测试设备运行时前置观察

在未安装本伴生 APK 的前置检查中, 微信最终停留于 `com.tencent.mm.ui.LauncherUI`. 通过 shell `UiAutomation` 连续执行 3 次脱敏采样, 三次结果一致:

| 指标 | 值 |
|---|---:|
| 微信节点总数 | 122 |
| 非空 `text` | 15 |
| 非空 `content-desc` | 2 |
| 非空 `resource-id` | 82 |
| clickable | 17 |
| focusable | 18 |
| scrollable | 1 |
| 脱敏结构 hash 前缀 | `D77F6C02F2914C22` |

这个结果不能证明 AutoJs6 已经正常, 也不能证明伴生 APK 有效:

- 当时 AutoJs6 无障碍服务未启用.
- 设备已经启用 6 个其他无障碍服务, 实验基线受到污染.
- Shell `UiAutomation` 的身份和生命周期不同于普通 AccessibilityService, 微信可能区别处理.
- #289 已报告随机或陈旧节点, 单页短时稳定不能外推到聊天, 小程序或 XWeb.

因此这组前置数据只用于说明设备和页面状态, 不能作为插件结论. 后续已由 AutoJs6 自身选择器完成正式 A-B-A 验收: A 为 1 个节点, B 为 244 个节点, A2 精确回到 1 个节点, 每阶段连续 7 次稳定. 完整方法, 脱敏指标, 授权 ADB 切换和恢复记录见 [QV710AF65F 真机验收报告](../testing/device-QV710AF65F.md).

## 两层故障模型

### 第一层: 条件性隐藏或混淆

页面本来拥有节点, 但目标应用根据已启用服务身份改变节点暴露. 伴生 APK 只尝试影响这一层. 成功时 AutoJs6 自己的服务应重新获得更完整且稳定的树. 当前支持配置只对微信完成了验证.

### 第二层: 页面没有完整语义

WebView, 小程序, Canvas 或自绘控件没有把视觉内容映射为 Android 无障碍节点. 类名兼容无法解决这一层. 截图, OCR 和坐标可能作为宿主侧通用后备路径, 但应经过用户授权, 最小化敏感数据并加入结果校验.

## 当前实现为何采用 no-op 伴生 APK

- 保持 AutoJs6 核心 `AccessibilityServiceUsher` 的真实名称和通用语义.
- 应用 ID 固定为 `io.github.supermonster003.autojs6.plugin.three.adapt.a11y`, 不模仿 Google 包名.
- 服务标签, 描述, 图标, 设置界面, 文档和签名均明确显示真实用途.
- 兼容服务只监听已发布支持配置中的包名, 当前集合为 `com.tencent.mm`. 回调不读取 `event.source`, `event.text`, `rootInActiveWindow` 或截图.
- 不提供网络, 分析, 存储, 悬浮窗, 相机或麦克风能力.
- 不通过 Binder 代理节点. AutoJs6 仍使用自己的服务读取和操作树.
- 用户必须在 Android 设置中手动启用, 可随时关闭或卸载.

如果 no-op 触发不能稳定恢复节点, 下一阶段可以单独评估显式节点桥. 任何桥接设计都必须重新进行隐私威胁建模, 定义调用方鉴权, 数据生命周期, 大小上限和审计策略, 不能在本实验中默认扩大权限.

## 可证伪条件

出现以下任一结果时, 不应宣称当前方案有效:

- A 阶段已经通过, 没有可复现的失败基线.
- 只有 shell `uiautomator` 改善, AutoJs6 自己的布局分析或脚本结果没有改善.
- B 阶段只偶然成功 1 次, 或 A2 关闭服务后改善仍然保留.
- 改善来自页面完成加载, 目标应用重启, 账号变化, 其他无障碍服务或网络变化.
- 主界面恢复, 但目标实际是 WebView, 小程序或自绘页面且所需语义仍然缺失.

## 参考链接

- [AutoJs6](https://github.com/SuperMonster003/AutoJs6)
- [AutoJs6 Issues #289, #382, #432, #463, #520, #521](https://github.com/SuperMonster003/AutoJs6/issues)
- [GKD compatibility commit 47267c7](https://github.com/gkd-kit/gkd/commit/47267c7ceb802a2f083eec98ee50ad03f24f2f38)
- [A-B-A 设备验收协议](../testing/aba-device-validation.md)
