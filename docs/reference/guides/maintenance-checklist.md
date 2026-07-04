# 文档维护检查清单

## 用途

在变更落地后使用此文件检查仓库记忆是否保持同步。

## 非平凡代码变更后始终审查

1. `docs/baseline/architecture/` 中的相关所有者文档
2. `docs/logs/` 中的每日日志
3. 任何受影响的需求、计划、Bug 笔记或测试笔记
4. 当建立了有意义完整验证基线时，`docs/reference/guides/known-good-baselines.md`

## 变更触发项

### 架构或边界变更

审查：

- `docs/baseline/architecture/system-baseline.md`
- `docs/baseline/architecture/module-boundaries.md`
- 如果路由变更，`docs/index.md`

### 产品意图或范围变更

审查：

- 如果源材料本身变更，`docs/work/` 中的相关文件
- 如果需求解读变更，`docs/work/` 中的相关文件
- `docs/work/` 中的相关文件

### 应用层功能或流程变更

审查：

- `docs/baseline/architecture/` 中最相关的文件
- 如果用户可见范围变更，`docs/work/`
- 如果需要手动/探索性证明，`docs/work/`

### 非平凡实施切片

审查：

- `docs/work/` 下的活跃计划
- 如果审计是切片的一部分，`docs/skills/` 下的相关文件
- `docs/logs/YYYY/MM-DD.md`
- 如果需要探索性/手动证明，`docs/work/`

创建的计划在实施前需要计划审计，在完成前需要闭环审计。

### 微妙回归或根因发现

审查：

- `docs/bugs/`
- 如果问题暴露了流程或需求缺口，`docs/retro/`
- 任何受影响的所有者文档

## 验证基线

使用 `docs/baseline/context/project-context.md` 中的真实项目命令。

如果该文件仍包含占位符，在声称验证成功之前填充它。
