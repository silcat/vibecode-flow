# 更新基线

## 文件角色

对照需求文档，将本次变更固化到架构边界、技术标准与项目上下文中。

## 进入条件

research.md 已生成（若触发 research）；requirement.md 存在。

## 执行步骤

### 1. 影响分析

对照 requirement 逐条提取，在对话中输出影响矩阵：

| 变更项 | 新增服务 | 职责变更 | 新技术 | 端口变更 |
|--------|---------|---------|--------|---------|
| [变更描述] | Y/N | Y/N | Y/N | Y/N |

### 2. 写入基线

对照影响矩阵，更新：
- `docs/baseline/architecture/` — 模块边界、系统基线
- `docs/baseline/standards/` — api、database、auth 等
- `docs/baseline/context/project-context.md`

更新后自检：对照 requirement 逐条确认基线描述与需求一致。

## 产出物

| 产出 | 路径 |
|------|------|
| 更新的基线文件 | `docs/baseline/` 下受影响文件 |

## 完成证明

`git diff --stat docs/baseline/` 确认基线文件已更新，且变更与影响矩阵一致。

> 若 autonomy ≠ implement：影响矩阵须人类确认后才执行文件写入。

## 退出路由

| 条件 | 去向 |
|------|------|
| 完成 | → requirement-audit |

