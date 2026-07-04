# 日志回顾

## 文件角色

追加每日开发日志，检测错误模式并触发技能提取，记录回归与回顾。

## 首先阅读

| 如果你需要… | 先读 |
|-------------|------|
| 每日日志编写指南 | `docs/logs/00-log-writing-guide.md` |
| Bug 记录编写指南 | `docs/bugs/00-bug-fix-note-writing-guide.md` |
| 回顾编写指南 | `docs/retro/00-retrospective-writing-guide.md` |

## 执行

- 追加每日开发日志到 `docs/logs/YYYY/MM-DD.md`（倒序）
- 检查 `docs/logs/error-patterns.md` → ≥2 次重复模式 → 触发 skill 阶段
- 非显而易见回归 → 记录到 `docs/bugs/`
- 原型与实施实质性偏离 → 编写回顾到 `docs/retro/`

## 项目完结条目

总计划完结时（所有子计划 ✅ + 集成审计通过），在每日日志中追加：

```markdown
## 项目完结：<项目名>

- 总计划：`docs/work/<项目目录>/plan.md`
- 子计划清单：
  | 子计划目录 | 完成时间 |
  |-----------|---------|
  | <子计划1> | YYYY-MM-DD |
  | <子计划2> | YYYY-MM-DD |
- 集成审计：通过 / 发现项及处理（如有）
```

## 退出

→ 流程结束（→ skill，若 error-patterns ≥2 次）
