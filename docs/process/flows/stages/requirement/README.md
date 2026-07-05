# 合成需求

## 文件角色

将讨论记录（discussion.md）与外部材料（docs/work/input/）合成为结构化需求文档，验收标准逐条可独立测试。

## 首先阅读

- [template.md](template.md) — 需求文件模板与格式

## 执行

见 [template.md](template.md)

产出：`docs/work/<branch>/requirement.md`

验收标准必须编号，每条可独立测试。未决问题全部标记为非阻塞或已解决。

## 退出条件（阻断）

进入 scope 前全部满足：

- [ ] 验收标准已编号，每条以"用户/系统可观察行为"开头（非实现细节）
- [ ] 每条验收标准已标注可验证方式（手动 / 单元测试 / 集成测试）
- [ ] 未决问题全部标记 `[未决]` 或 `[已解决]`
- [ ] 无 `[待定]` 或 `TBD` 占位符

缺失任一条 → 回到自身修改，不得进入 scope。

## 产出

`docs/work/<branch>/requirement.md`

## 退出

→ scope

## 回退

需求不合格 → clarify
