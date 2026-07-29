# Git 提交

> 用途：实施完成后按规范提交代码
> 触发：execute-agent 阶段 5

## 执行步骤

### 1. 确认范围

```
git status
git diff --stat
```

有不相关脏文件 → STOP，确认后继续。

### 2. 暂存

```
git add <具体文件>
```

禁止 `git add -A`。禁止提交 `.env`、密钥、IDE 配置。

### 3. 最终确认

```
git diff --cached --check
```

### 4. 提交

当变更包含代码 + 文档时，拆为两个提交：

**第一个：代码 + 测试**
```
feat(scope): 描述
```
或
```
fix(scope): 描述
```

**第二个：文档 + 日志 + 计划更新**
```
docs(scope): 更新实施日志及计划状态
```

单次提交格式：`type(scope): 描述`。主题行 ≤72 字符。

## 提交类型

| type | 用途 |
|------|------|
| feat | 新功能 |
| fix | Bug 修复 |
| refactor | 不改变行为的代码重构 |
| docs | 仅文档变更 |
| test | 添加或更新测试 |
| chore | 构建、依赖、工具链 |

## 安全规则

- 禁止 `git push --force` 除非明确指示
- 禁止 `--no-verify` 跳过 hooks 除非明确指示