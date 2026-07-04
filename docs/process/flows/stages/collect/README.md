# 收集输入

## 文件角色

收集用户输入和外部材料，为后续阶段准备原始上下文。

## 首先阅读

| 如果你需要… | 先读 |
|-------------|------|
| 了解讨论记录命名约定 | [clarify/template.md](../clarify/template.md) |

## 执行

1. 提示用户为本次工作命名（如 `pet-homepage`），AI 据此创建 `docs/work/<branch>/`
2. `docs/work/input/` 不存在则 AI 自动创建
3. 若 `docs/work/input/` 为空，提示用户是否需要放入外部材料（PM 笔记、原型截图等），用户确认后再继续
4. 用户在聊天中描述需求 → 写入 `docs/work/<branch>/discussion.md`

新鲜源来自两处：`docs/work/input/`（外部文件）和聊天。

规则：保持原始材料接近原意，不在此阶段改写为完整需求。

## 产出

`docs/work/<branch>/discussion.md`（若 `docs/work/input/` 有材料则一并就位）。

## 退出

→ ② 或 ③
