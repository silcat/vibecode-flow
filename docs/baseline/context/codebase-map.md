# 代码地图

## 用途

此文件给 AI 代理一个紧凑的仓库导航图，避免每次都靠全局搜索重新发现结构。

保持足够新，能路由常见工作即可。不要把它写成完整架构文档。

## 入口点

| 区域 | 路径 | 备注 | 最后验证 | 置信度 |
|------|------|------|---------|--------|
| chat-agent 应用入口 | chat-agent/src/main/java/com/vibecode/agent/AgentApplication.java | Spring Boot 入口，端口 8090 | 2026-07-05 | high |
| chat-agent-ui 前端入口 | chat-agent-ui/src/main.js | Vue 3 + Vite，端口 5173 | 2026-07-05 | high |

## 常见变更路由

| 任务类型 | 从这开始 | 验证 | 最后验证 | 置信度 |
|---------|---------|------|---------|--------|
| 新增 LLM 适配器 | chat-agent/.../llm/ | `mvn test -f chat-agent/pom.xml` | 2026-07-05 | high |
| 新增 REST API | chat-agent/.../chat/ | SSE 端点 + 会话管理 | 2026-07-05 | high |
| 新增 RAG 功能 | chat-agent/.../knowledge/ | 文档上传 → 向量化 → 检索 | 2026-07-05 | high |

## 大型或脆弱文件

| 路径 | 风险 | 推荐做法 | 最后验证 | 置信度 |
|------|------|---------|---------|--------|
| chat-agent/src/main/resources/application.yml | API key 配置集中 | 改前确认 `DEEPSEEK_API_KEY` 环境变量 | 2026-07-05 | medium |

## 更新规则

- 新增入口点、迁移公共代码、新增测试目录、或 AI 反复重新发现同一路径时更新此文件
- 列出的路径缺失或占位符仍存在时，不要以此文件为准，先验证活仓库再更新
- 涉及保护区、跨模块或迁移类工作时，先更新相关行再实施
| 新增 MCP 工具 | chat-agent/.../mcp/ | `mvn test -f chat-agent/pom.xml` | 2026-07-05 | high |
