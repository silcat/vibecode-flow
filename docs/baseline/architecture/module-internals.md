# 分层架构规范

## 用途

新增模块时，先选原型，再回答对应问题清单即可产出模块内部结构。

## 原型选择

| 原型 | 适用场景 | 问题清单 |
|------|---------|---------|
| crud-service | 围绕数据实体的标准业务服务 | [archetypes/crud-service.md](archetypes/crud-service.md) |
| agent-service | LLM 驱动的 AI 智能体 | [archetypes/agent-service.md](archetypes/agent-service.md) |

无匹配原型时，标记为 `custom`，在本文件底部追加模块专属结构说明。

## 已实施模块


## 更新触发条件

- 新增模块 → 选原型、回答对应问题清单、在本文件底部追加模块结构
- 新增原型 → 在 archetypes/ 下创建新原型文件、更新原型选择表
- 模块结构重大变化 → 更新本文件对应模块的结构描述
