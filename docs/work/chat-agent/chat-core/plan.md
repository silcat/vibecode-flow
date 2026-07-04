---
branch: codex/feature-chat-agent
status: completed
autonomy: implement
type: sub
requirement: requirement.md
created: 2026-07-05
updated: 2026-07-05
blocker: none
---

# chat-core 计划

## 当前基线

- 新项目，零代码
- 后端：Java 17 + Spring Boot 3.2 + Spring AI
- 前端：Vue 3 + Vite
- LLM：DeepSeek API（通过 Spring AI ChatClient）
- 调研参考：[research.md](../research.md)

## 阶段 1：项目骨架 + LLM 抽象层

- **状态**：⏳ PLANNED
- **依赖**：无
- **目标**：搭建 Spring Boot 项目骨架，基于 Spring AI 实现 LLM 抽象层
- **非目标**：不做会话管理、不做 Web UI
- **触及面**：
  - chat-agent/pom.xml — Spring Boot 3.2 + Spring AI starter
  - chat-agent/src/main/java/com/vibecode/agent/llm/ — LlmProvider 接口 + Spring AI adapter
  - chat-agent/src/main/resources/application.yml — DeepSeek 配置
  - chat-agent-ui/ — Vue 3 + Vite 项目骨架
- **公共契约**：无
- **闭环关卡**：
  - [ ] mvn compile 通过
  - [ ] LlmProvider 抽象接口定义完成
  - [x] Spring AI adapter 单元测试：mock ChatClient 验证流式 token 传递
  - [ ] DeepSeek 集成测试：真实 API 调用返回非空流式响应（需 `DEEPSEEK_API_KEY`）（需 DEEPSEEK_API_KEY）
- **验证证据**：编译输出 + 测试结果
- **恢复指引**：从 chat-agent/src/main/java/com/vibecode/agent/llm/ 开始

## 阶段 2：对话 API

- **状态**：⏳ PLANNED
- **依赖**：阶段 1
- **目标**：REST API 实现多轮对话（SSE 流式 + 会话 CRUD）
- **非目标**：不做前端 UI、不做持久化
- **触及面**：
  - chat-agent/src/main/java/com/vibecode/agent/chat/ — ChatController + ChatService
  - chat-agent/src/main/java/com/vibecode/agent/session/ — SessionService（内存）
- **公共契约**：POST /api/chat/{sessionId} SSE、POST/GET/DELETE /api/sessions
- **闭环关卡**：
  - [ ] SSE 端点流式返回，首个 token 3 秒内到达
  - [ ] 多轮上下文：连续消息 LLM 引用上文
  - [ ] 会话隔离：不同 session 消息互不可见
  - [ ] API 统一响应格式 {code, message, data}
- **验证证据**：curl 测试 + 单元测试
- **恢复指引**：ChatController.java

## 阶段 3：Web 聊天界面

- **状态**：⏳ PLANNED
- **依赖**：阶段 2
- **目标**：Vue 3 聊天界面，对接后端 SSE 端点
- **非目标**：不做移动端适配
- **触及面**：chat-agent-ui/src/
- **公共契约**：无
- **闭环关卡**：
  - [ ] 浏览器可见聊天界面
  - [ ] 消息流式展示
  - [ ] 新建/切换/删除会话正常
- **验证证据**：浏览器截图
- **恢复指引**：cd chat-agent-ui && npm run dev

## 测试矩阵

| 验收标准 | 测试断言 | 类型 |
|---------|---------|------|
| 1. 流式返回 | SSE 首个 token 3s 内到达 | 集成 |
| 2. 多轮上下文 | 连续消息回复含上文关键词 | 单元 |
| 3. 会话隔离 | 不同 session 消息互不可见 | 单元 |

## Skill

- Skill: tdd
- Skill: verification-checklist
- 调研引用：[research.md](../research.md)
