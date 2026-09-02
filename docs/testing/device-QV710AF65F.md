# QV710AF65F 真机 A-B-A 验收报告

验收日期: 2026-09-02

## 结论

在指定设备, 版本和微信 `LauncherUI` 页面上, Accessibility Compat 产生了稳定且可逆的变化. AutoJs6 自身的选择器在 A 阶段只能获得 1 个无语义节点, B 阶段在兼容服务开启后稳定获得 244 个节点, A2 关闭兼容服务后精确回到 1 个节点. 每阶段的 7 次连续采样均稳定.

这个结果满足 [A-B-A 设备验收协议](aba-device-validation.md) 的核心因果判据. 它证明本次受控组合有效, 不代表所有微信版本, 页面, 账号或设备均有效.

## 环境

| 字段 | 值 |
|---|---|
| 设备 serial | `QV710AF65F` |
| 设备型号 | Sony `XQ-AT72` |
| Android | 12, API 31 |
| ABI | `arm64-v8a` |
| 微信 | 8.0.72, versionCode 3085, `com.tencent.mm` |
| AutoJs6 | 6.8.0, versionCode 5277 |
| 目标页面 | `com.tencent.mm.ui.LauncherUI` |
| 每阶段样本数 | 7 |

设备在实验前已有 6 个已启用无障碍服务. 为保持设备现实状态并避免扩大变更范围, 这 6 个服务在 A, B 和 A2 全程保留, 没有被删除或替换. 实验只临时合并 AutoJs6 服务, 并在 B 阶段额外合并 Accessibility Compat 服务.

## 测量方法

- 主测量来自 AutoJs6 自己的 `packageName("com.tencent.mm").find()`, 不是 shell `uiautomator`.
- 使用仓库中的[脱敏测量脚本](../../tools/autojs6-wechat-metrics.js). 每次只保留聚合计数和不含文本值的结构 SHA-256, 不保存节点文本, 描述, resource ID 值, 边界明细或截图.
- 每个阶段都先 force-stop 微信, 再重新启动 `LauncherUI`, 等待超过 5 秒后开始采样.
- 每阶段连续采样 7 次. 脚本内部样本间隔为 450 ms, 每次先调用 `auto.clearCache()`.
- B 阶段通过 `dumpsys accessibility` 确认 AutoJs6 与 Accessibility Compat 两个目标服务均处于 bound 状态.
- 页面, 账号, 微信版本, AutoJs6 版本, 网络和 6 个既有无障碍服务在三个阶段保持不变.

通用协议为新测试保守建议在切换后等待至少 10 秒. 这次已完成的实验依据目标样本中观察到的 5000 ms 检查间隔等待超过 5 秒, 没有达到该保守建议值. 结果仍具有 A 和 A2 精确回退及每阶段 7/7 稳定的强证据, 但后续复测应统一采用至少 10 秒等待.

## A-B-A 结果

| 阶段 | Compat | nodes | 非空 text | 非空 desc | 非空 id | clickable | 结构稳定性 |
|---|---|---:|---:|---:|---:|---:|---|
| A | 关闭 | 1 | 0 | 0 | 0 | 0 | 7/7 相同 |
| B | 开启 | 244 | 31 | 13 | 157 | 38 | 7/7 相同 |
| A2 | 再次关闭 | 1 | 0 | 0 | 0 | 0 | 7/7 相同 |

表中只包含计数. 任何节点的实际 `text`, `desc` 或 `id` 均未写入本报告.

## 因果解释

本次结果不是一次偶然成功:

- A 提供了可复现失败基线, 7 次都是同一个单节点结构.
- B 只增加兼容服务后, 7 次均得到同一组显著更丰富的指标.
- A2 再次移除兼容服务后, 计数精确回到 A 的单节点分布.
- 每个阶段都重新启动微信并采用相同等待步骤, 降低页面加载和旧缓存解释结果的可能性.
- 指标由 AutoJs6 自身服务读取, 因而直接对应项目目标.

既有 6 个服务不是零服务基线, 但它们在三个阶段保持完全不变. 因此本报告验证的是在该既定设备状态下增加 Accessibility Compat 的边际效果, 不能推导出移除这些服务后仍有相同结果.

## 授权切换与恢复

本次实验由设备所有者明确授权使用 ADB. 自动化流程在写入前保存 `enabled_accessibility_services` 和 `accessibility_enabled` 的精确原值, 然后只对目标组件做集合合并或移除:

1. A 在原有 6 个组件基础上临时加入 AutoJs6, 不加入 Accessibility Compat.
2. B 保持原列表与 AutoJs6, 只再加入 Accessibility Compat.
3. A2 只移除 Accessibility Compat, 保持 AutoJs6 和原有 6 个组件.
4. `finally` 恢复实验前两个 secure setting 的原值, 再回读并逐字校验.

恢复校验通过. 测试结束后, `enabled_accessibility_services` 和 `accessibility_enabled` 均与实验前精确一致. 设备上的临时脚本副本和结果文件已删除. Accessibility Compat APK 保持安装, 但其无障碍服务未启用.

常规用户不应复制这一 ADB 流程. 普通安装和使用必须在 Android 系统设置中手动确认服务启用. 授权实验的完整约束见[A-B-A 设备验收协议](aba-device-validation.md#授权自动化实验的切换约束).

## 适用范围与限制

- 已验证范围仅为 Sony XQ-AT72, Android 12/API 31, 微信 8.0.72 code 3085, AutoJs6 6.8.0 code 5277 与 `LauncherUI`.
- 未验证聊天页, 微信小程序, XWeb, Canvas, 自绘控件, 其他账号, 其他 ROM 或后续微信版本.
- 结果不能证明兼容服务能生成页面本来不存在的语义节点. 小程序或 XWeb 仍可能只有视觉内容而没有完整 `text` 或 `desc`.
- 微信远程配置或版本更新可能随时改变行为. 每个新版本和目标页面都应重新执行 A-B-A.
- 本报告不包含节点内容, 原始 dump, 微信 APK, dex, 截图或账号信息.

## 清理状态

- 系统两个无障碍 secure setting: 已恢复并验证.
- 6 个既有无障碍服务: 全程保留, 测试后状态不变.
- AutoJs6 临时启用状态: 已恢复到实验前状态.
- Accessibility Compat: APK 仍安装, 服务关闭.
- 设备侧临时脚本和聚合结果: 已删除.
