# Accessibility Compat A-B-A 设备验收协议

## 目的

本协议用于判断开启 Accessibility Compat 是否让 AutoJs6 自己的无障碍服务在同一目标应用页面上稳定获得更完整的节点树. A-B-A 表示兼容服务关闭, 开启, 再关闭. 最后一次关闭用于验证结果可逆, 排除页面加载, 缓存和随机返回造成的假阳性.

Shell `uiautomator` 只可作为次要诊断. 正式判据必须来自 AutoJs6 布局分析器或经审阅的 AutoJs6 脚本, 因为 shell `UiAutomation` 与普通 AccessibilityService 的身份不同.

## 安全与隐私前提

- 只测试本人设备, 本人账号或已获得明确授权的账号和页面.
- 优先使用不含真实私人内容的测试账号, 测试页面或测试数据.
- 不保存原始 XML, `text`, `content-desc`, 账号标识, 私人正文, token 或截图.
- 不把目标应用 APK, dex 或反编译目录放入仓库或测试报告.
- 常规用户必须在 Android 系统设置中手动启用或关闭服务, 不应使用 `settings put secure` 或其他 ADB 命令绕过确认.
- 自动化实验只有在设备所有者明确授权时才可通过 ADB 切换. 实验必须先保存两个 secure setting 的原始值, 只对目标组件做精确合并或移除且保留全部既有服务, 并在 `finally` 中恢复原值及回读校验. 不得把原始服务列表写入公开报告.
- 测试完成后恢复原有无障碍服务状态, 并在不需要时关闭 Accessibility Compat.

### 授权自动化实验的切换约束

ADB 例外仅用于可审计的受控实验, 不属于普通安装或使用步骤. 自动化脚本必须同时满足:

1. 在第一次写入前原样读取并暂存 `enabled_accessibility_services` 与 `accessibility_enabled`.
2. 把 AutoJs6 或 Accessibility Compat 组件精确并入原列表, 不重排, 替换或删除其他组件.
3. 每次切换后回读设置, 并用 `dumpsys accessibility` 确认预期服务状态.
4. 把所有恢复逻辑放入 `finally`, 即使采样, 启动目标应用或解析结果失败也必须执行.
5. 恢复后再次回读两个设置, 与实验前保存的原始值逐字比较. 不一致时立即停止并报告恢复失败.

这种方式只证明已授权实验环境内的可控变化. 它不能成为应用静默启用无障碍服务的产品功能, 也不能替代常规用户的系统确认.

## 固定变量

开始前记录以下非敏感信息, 三个阶段必须保持一致:

| 变量 | 要求 |
|---|---|
| 设备 | 同一设备, Android 版本, 分辨率和方向 |
| AutoJs6 | 同一版本, 同一脚本, 自身无障碍服务始终开启 |
| 目标应用 | 同一包名, 版本, 账号, 页面入口和页面内容 |
| 页面状态 | 停止滚动和动画, 返回页面后采用相同等待时间 |
| 网络 | 三阶段保持相同网络类型和连接状态 |
| 其他服务 | 除 AutoJs6 外的非必要无障碍服务全部关闭, 或保持完全相同并逐项记录 |
| 采样 | 每阶段至少 5 次, 推荐 10 次, 采样间不点击页面 |

首个已验证支持配置的 8.0.72 样本中观察到 5000 ms 检查间隔常量. 这不是公开协议, 也不能外推到其他应用. 为了避免过早采样, 建议每次切换服务并回到目标页面后等待至少 10 秒. 三个阶段必须采用同一等待时间和相同的离开及返回页面步骤.

## 指标

每次采样只记录聚合值:

```text
timestamp
phase: A1 | B | A2
foreground package and activity
rootIsNull
nodeCount
nonEmptyTextCount
nonEmptyContentDescriptionCount
nonEmptyResourceIdCount
clickableCount
focusableCount
scrollableCount
maximumDepth
sanitizedStructureHash
```

`sanitizedStructureHash` 只允许使用以下字段构造:

```text
package | class | resource-id | bounds | clickable | focusable | scrollable | child-count
```

不得把 `text`, `content-desc`, hint, 输入值或其他用户内容写入 hash 输入. 即使 hash 不可直接阅读, 私有文本仍可能被字典攻击或用于跨样本关联.

## 阶段 A1: 关闭兼容服务

1. 保持 AutoJs6 无障碍服务开启.
2. 常规用户在 Android 无障碍设置中关闭 Accessibility Compat 对应的服务. 已授权自动化实验只从保存的原列表精确移除该兼容组件.
3. 关闭或固定记录所有其他无障碍服务.
4. 按预先定义的路径进入目标应用页面, 停止交互并等待至少 10 秒.
5. 使用 AutoJs6 连续采集至少 5 次聚合指标.
6. 记录是否出现根节点为 null, 随机结构, 陈旧属性或目标控件缺失.

如果 A1 已经稳定满足脚本需求, 当前环境没有失败基线. 应将结果记为 `NOT_REPRODUCED`, 不继续把后续成功归功于兼容服务.

## 阶段 B: 开启兼容服务

1. 不修改 AutoJs6, 目标应用, 页面内容, 网络或其他服务.
2. 常规用户在 Android 无障碍设置中开启 Accessibility Compat 对应的服务. 已授权自动化实验只把该兼容组件精确合并到当前列表.
3. 用与 A1 完全相同的路径返回目标页面并等待至少 10 秒.
4. 使用同一 AutoJs6 工具连续采集相同次数的指标.
5. 不因第一次看见目标节点就停止采样.

## 阶段 A2: 再次关闭兼容服务

1. 常规用户再次手动关闭 Accessibility Compat 对应的服务. 已授权自动化实验只移除该兼容组件, 不改变其他组件.
2. 使用与 A1 和 B 相同的返回页面和等待步骤.
3. 再次采集相同次数的指标.
4. 确认指标是否回到 A1 的失败分布.

如果 A2 继续保持 B 的改善, 可能原因包括缓存, 页面自身加载完成, 目标应用进程状态或其他服务影响. 此时结果为 `INCONCLUSIVE`, 不能证明兼容服务有效.

## 判定规则

在测试前先写下目标控件和通过条件, 避免看到结果后移动标准. 推荐同时满足以下要求才判定 `PASS`:

- A1 和 A2 都稳定复现相同失败, B 稳定成功.
- B 至少 80% 样本包含脚本所需的节点和属性.
- B 的结构在静止页面上稳定, 例如至少 4/5 样本具有同一脱敏结构 hash.
- A1 与 A2 的节点和语义字段分布彼此接近, 并与 B 明显分离.
- AutoJs6 自身布局分析或脚本结果改善, 而不是只有 shell `uiautomator` 改善.
- 目标页面分别验证. 主界面通过不能替代聊天页, 小程序, XWeb 或 Canvas 页面通过.

其他结果:

| 结果 | 含义 |
|---|---|
| `PASS` | B 稳定改善, A1 和 A2 稳定失败, 变化可逆 |
| `FAIL` | A1 和 A2 失败, B 也没有满足预设目标 |
| `NOT_REPRODUCED` | A1 已经成功, 没有可归因的失败基线 |
| `INCONCLUSIVE` | 页面或环境变化, 样本不稳定, A2 不回退, 或只有次要工具改善 |

## 结果记录模板

```markdown
### Environment

- Device model:
- Android/API:
- AutoJs6 versionCode/versionName:
- Target app package/versionCode/versionName:
- Accessibility Compat versionCode/versionName:
- Target page category: main | chat | mini-program | XWeb | other
- Other enabled accessibility services:
- Service switching: manual | owner-authorized ADB
- Original accessibility settings restored and read back: yes | no | not-applicable
- Predeclared required controls and fields:

### Aggregate results

| Phase | Samples | Root null | Median nodes | Stable hash ratio | Required control success | Result |
|---|---:|---:|---:|---:|---:|---|
| A1 off |  |  |  |  |  |  |
| B on |  |  |  |  |  |  |
| A2 off |  |  |  |  |  |  |

### Notes

- Page loading or navigation differences:
- Unexpected service changes:
- Sanitized conclusion:
```

报告中不要附原始 dump. 若维护者确实需要检查单个异常结构, 先建立不含用户内容的最小复现页面, 再单独征得数据共享同意.

## 次要 shell 采样

以下 PowerShell 示例直接把 `uiautomator dump` 输出到进程内存, 只打印聚合计数, 不把 XML 写到设备共享存储或本地文件. 它用于辅助确认页面和系统状态, 不能替代 AutoJs6 主判据.

```powershell
$serial = 'QV710AF65F'
$targetPackage = 'replace.with.target.package'
$raw = (& adb -s $serial exec-out uiautomator dump /dev/tty 2>&1 | Out-String)
$start = $raw.IndexOf('<?xml')
$end = $raw.LastIndexOf('</hierarchy>')
if ($start -lt 0 -or $end -lt $start) {
    throw 'uiautomator did not return complete XML'
}

$doc = [xml]$raw.Substring($start, $end + 12 - $start)
$nodes = @($doc.SelectNodes('//node'))

[ordered]@{
    nodeCount = $nodes.Count
    nonEmptyText = @($nodes | Where-Object { $_.text -ne '' }).Count
    nonEmptyContentDesc = @($nodes | Where-Object { $_.'content-desc' -ne '' }).Count
    nonEmptyResourceId = @($nodes | Where-Object { $_.'resource-id' -ne '' }).Count
    clickable = @($nodes | Where-Object { $_.clickable -eq 'true' }).Count
    focusable = @($nodes | Where-Object { $_.focusable -eq 'true' }).Count
    scrollable = @($nodes | Where-Object { $_.scrollable -eq 'true' }).Count
    targetNodes = @($nodes | Where-Object { $_.package -eq $targetPackage }).Count
}
```

虽然脚本不输出文本, 原始 XML 在 PowerShell 进程内存中短暂存在. 在高敏感页面上应完全跳过 shell dump, 改用预先设计的无真实内容测试页面.

## 测试设备前置记录

在 `QV710AF65F` 上, 微信 8.0.72 的 `LauncherUI` 曾经通过 shell `UiAutomation` 连续 3 次得到相同的 122 节点结构, 其中 15 个节点有非空 `text`, 2 个有非空 `content-desc`, 82 个有资源 ID, 17 个可点击. 当时 AutoJs6 服务未启用且另有 6 个无障碍服务正在运行, 所以该记录被标记为 `INCONCLUSIVE` 前置观察, 不能充当 A1 或插件成功证据.

## 恢复与收尾

1. 保存聚合表, 不保存原始页面内容.
2. 恢复测试前的无障碍服务组合.
3. 不再使用时关闭 Accessibility Compat.
4. 检查仓库和临时目录, 确认没有 APK, dump, 截图, dex 或解包目录.
5. 报告 `PASS`, `FAIL`, `NOT_REPRODUCED` 或 `INCONCLUSIVE`, 不以含糊的 "看起来可以" 代替判定.

研究背景见[无障碍服务身份兼容研究记录](../research/accessibility-service-identity-compat.md). 当前支持应用及每个应用的实机证据应在各自的支持配置和设备报告中记录.
