# 更新基线

## 文件角色

对照需求文档，将本次变更固化到架构边界、技术标准与项目上下文中。

## 首先阅读

| 如果你需要… | 先读 |
|-------------|------|
| 了解当前架构边界与系统基线 | `docs/baseline/architecture/` |
| 了解当前技术标准（api、database、auth 等） | `docs/baseline/standards/` |
| 了解当前项目上下文 | `docs/baseline/context/project-context.md` |

## 前置（阻断）

基线更新前，必须输出"基线影响分析"。全部打勾后才允许修改基线文件：

- [ ] 已列出本次需求变更涉及的所有模块/服务名称（对照 requirement 逐条提取）
- [ ] 已逐条判定：新增服务(Y/N)、职责变更(Y/N)、新技术引入(Y/N)、端口变更(Y/N)
- [ ] 已输出"影响矩阵"：每条变更 → 对应基线文件 → 更新内容摘要
- [ ] 影响矩阵经人类确认后，才执行文件写入

缺失任一条 → 保持阻塞，不得修改基线文件。

## 执行

对照需求文档和影响矩阵，更新以下基线文件：
- `docs/baseline/architecture/`（模块边界、系统基线）
- `docs/baseline/standards/`（api、database、auth 等）
- `docs/baseline/context/project-context.md`

更新后自检：对照 requirement 逐条确认基线描述与需求一致。发现矛盾先修正再退出。

## 产出

`docs/baseline/` 下受影响的文件（architecture/、standards/、context/project-context.md）。

## 退出

→ audit
