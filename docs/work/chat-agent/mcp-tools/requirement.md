# mcp-tools 需求子集

> 从总需求提取，副本冻结。

## 范围内

- 用户配置 MCP Server 连接信息 → agent 启动时加载工具列表
- 对话中 agent 判断需要调用工具 → 执行 MCP 调用 → 结果注入上下文
- 支持 stdio 传输（首期），HTTP+SSE 预留

## 范围外

- 管理界面中动态增删 MCP Server（首期配置文件静态声明）
- 多 MCP Server 并发工具编排（首期单一 Server）

## 验收标准

7. 配置一个有效的 MCP Server → agent 启动后工具列表中可见该 Server 提供的工具
8. 对话中触发工具调用 → agent 正确调用工具并将结果用于回复生成
