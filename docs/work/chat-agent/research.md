# 技术调研 — chat-agent

## 背景

构建企业级 AI 聊天 agent，需求中已确定的决策：
- LLM：DeepSeek API，抽象层预留可插拔
- 前端：Vue 3，Web 聊天界面
- 向量存储（后续子计划）：HNSW / Lucene
- MCP（后续子计划）：标准 MCP 客户端

本次调研聚焦 chat-core 子计划的关键技术选型：DeepSeek API 调用方式。

## 候选方案

### 方案 A：Spring AI（spring-ai-openai）

| 维度 | 评估 |
|------|------|
| **功能匹配** | 原生支持 OpenAI 兼容 API（DeepSeek 完全兼容），开箱即用的流式对话、ChatClient API、Prompt Template。覆盖率 100% |
| **技术质量** | Spring 官方子项目，1.0.0 已 GA。内置重试、速率限制、可观测性集成（Micrometer）。与 Spring Boot 3.2 深度集成 |
| **集成成本** | 一个 starter 依赖。自动配置 DeepSeek base-url 即可。胶水代码约 5 行配置 |
| **可维护性** | Spring 生态统一升级路径。API 稳定，破坏性变更走 deprecation 周期。内置测试支持 |
| **社区生态** | Spring 官方维护，GitHub 19k+ stars，活跃度高。文档完善，quickstart 可运行 |
| **团队组织** | 与已有 Spring Boot 技术栈一致，零额外学习成本。Apache 2.0 许可 |

### 方案 B：原生 HttpURLConnection + 手动 SSE 解析

| 维度 | 评估 |
|------|------|
| **功能匹配** | 可完整实现流式和非流式调用。需手动构造请求体、解析 SSE 数据流、处理 [DONE] 信号。覆盖率 100%，但全部自研 |
| **技术质量** | JDK 内置，无第三方依赖。但需自行处理连接池、重试、超时、错误恢复——这些都是生产级 HTTP 客户端的标配 |
| **集成成本** | 零依赖引入。但需编写 ~150 行 HTTP 客户端代码 + ~80 行 SSE 解析代码，外加连接管理和错误处理 |
| **可维护性** | 自研代码需持续维护。DeepSeek API 变更时需手动适配。无社区升级路径 |
| **社区生态** | 不适用。自研方案 |
| **团队组织** | JDK 内置，无许可证风险。但把时间花在 HTTP 客户端上而非业务逻辑上 |

### 方案 C：OkHttp + 手动 SSE

| 维度 | 评估 |
|------|------|
| **功能匹配** | 同方案 B，连接池和 HTTP/2 由 OkHttp 处理，SSE 仍需手动解析 |
| **技术质量** | Square 维护，生产验证充分。HTTP/2、连接池、重试/拦截器内置 |
| **集成成本** | 一个 OkHttp 依赖 + 约 80 行 SSE 解析代码 |
| **可维护性** | OkHttp 升级路径清晰，但 SSE 解析仍需自维护 |
| **社区生态** | 46k+ stars，但并非 AI 领域专用库 |
| **团队组织** | 同方案 B——核心精力仍在非业务代码上 |

## 推荐结论

**推荐: 方案 A — Spring AI（spring-ai-openai）**

理由:
- 功能: 100% 覆盖 DeepSeek 流式/非流式调用，无需自研
- 质量: Spring 官方维护，生产级重试/限流/可观测性内置
- 集成: 一个 starter，5 行配置。与 Spring Boot 3.2 技术栈零摩擦
- 维护: 统一 Spring 生态升级，社区驱动

弃用方案及原因:
- 方案 B: 生产级 HTTP 客户端（连接池、重试、超时）需大量自研，非核心业务
- 方案 C: 减少但未消除自研负担，SSE 解析仍是额外维护成本

风险与缓解:
- DeepSeek 与 OpenAI API 存在细微差异 → 首期验证流式和非流式两种模式，差异点抽象到 Adapter 层
- Spring AI 版本迭代快速 → 锁定 1.0.x 稳定版，主版本升级前评估 changelog

## 结论

已批准。2026-07-05 — Spring AI (spring-ai-openai) 对接 DeepSeek，同意推荐方案及风险缓解策略。

批准方案 A。chat-core 子计划使用 Spring AI 的 OpenAI starter 对接 DeepSeek。