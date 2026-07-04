# 文档命名与时效性

## 用途

本指南区分稳定的所有者文档和有时效性的过程记录。

对于中小项目，这保持仓库易于导航，无需强迫每个文件遵循相同命名风格。

## 两个类别

### 1. 稳定的所有者文档

这些描述当前受支持的基线，通常应保持无日期的固定名称。

对以下使用固定名称：

- `docs/process/`
- `docs/baseline/architecture/`
- `docs/baseline/architecture/`
- `docs/reference/`
- `docs/skills/`
- 长期需求基线文件如 `docs/work/<分支>/product-scope.md` 和 `docs/work/<分支>/mvp.md`

示例：

- `docs/baseline/architecture/system-baseline.md`
- `docs/baseline/architecture/system-baseline.md`
- `docs/process/guides/application-development-workflow.md`

规则：

- 这些文件应原地更新
- 不要仅因内容变更就创建新的带日期版本

### 2. 有时效性的记录

这些捕获执行历史、调查上下文或日期决策。

这些文件通常应在路径或文件名中包含日期。

对以下使用带日期命名：

- `docs/logs/`
- `docs/testing/`
- `docs/work/<分支>/`
- `docs/analysis/`
- `docs/work/<分支>/`
- `docs/retro/`
- 大多数一次性需求合成文件和实施计划

## 推荐的路径约定

### 日志

- `docs/logs/YYYY/MM-DD.md`

### 测试笔记

- `docs/testing/YYYY/MM-DD.md`

### 讨论

- `docs/work/<分支>/YYYY-MM-DD-主题.md`

### 分析

- `docs/analysis/YYYY-MM-DD-主题.md`

### 审计

- `docs/work/<分支>/YYYY-MM-DD-<类型>-<主题>.md`

### 回顾

- `docs/retro/YYYY-MM-DD-主题.md`

### 计划

对于中小项目，推荐简单的带日期计划名称：

- `docs/work/<分支>/YYYY-MM-DD-主题-plan.md`

如果项目后来积累了较多计划且需要更强索引，可添加数字前缀：

- `docs/work/<分支>/NNN-YYYY-MM-DD-主题-plan.md`

### 一次性需求合成文件

如果文件是一次性切片而非稳定基线文件，建议使用带日期名称：

- `docs/work/<分支>/YYYY-MM-DD-功能名称.md`

## Bug 笔记

Bug 笔记是历史性的，但通常通过问题标识而非日期来引用。

对于中小项目，以下两种均可接受：

- `docs/bugs/01-简短-bug-名称.md`
- `docs/bugs/YYYY-MM-DD-简短-bug-名称.md`

建议：

- 如果 Bug 笔记将成为长期参考库，优先编号文件名
- 如果 Bug 笔记保持少量且主要服务于本地团队记忆，基于日期的文件名可接受

## 简易经验法则

- 如果文件回答"当前受支持基线是什么？" → 固定名称
- 如果文件回答"本轮/今天/本次调查发生了什么？" → 带日期名称

## 快速复制集

使用这些现成模式：

```text
docs/logs/2026/05-21.md
docs/testing/2026/05-21.md
docs/work/<分支>/2026-05-21-user-management-scope.md
docs/analysis/2026-05-21-auth-strategy-comparison.md
docs/work/<分支>/2026-05-21-document-audit-user-management.md
docs/work/<分支>/2026-05-21-user-list-plan.md
docs/work/<分支>/2026-05-21-order-refund-flow.md
docs/retro/2026-05-21-checkout-prototype-gap.md
docs/bugs/01-order-status-double-submit.md
```