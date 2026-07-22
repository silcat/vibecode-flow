# 收集输入

## 文件角色

收集用户输入和外部材料，为后续阶段准备原始上下文。

## 进入条件

无。

## 执行步骤

1. 提示用户为本次工作命名（如 `pet-homepage`），据此创建 `docs/work/<branch>/`
2. `docs/work/input/` 不存在则自动创建
3. 若 `docs/work/input/` 为空，提示用户是否需要放入外部材料（PM 笔记、原型截图等），用户确认后再继续
4. 用户在聊天中描述需求 → 写入 `docs/work/<branch>/discussion.md`

规则：保持原始材料接近原意，不在此阶段改写为完整需求。

## 产出物

| 产出 | 路径 |
|------|------|
| 讨论记录 | `docs/work/<branch>/discussion.md` |
| 外部材料 | `docs/work/input/`（如有） |

## 完成证明

`discussion.md` 存在且非空。直接在对话中确认路径即可，无需结构化标记。

## 退出路由

| 条件 | 去向 |
|------|------|
| 材料完整 | → requirement |
| 材料模糊或矛盾 | → clarify |
