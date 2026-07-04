# 测试规范

> 触发场景：编写单元测试、集成测试、服务间 Mock

## 分层策略

| 层 | 工具 | 覆盖目标 |
|----|------|---------|
| Service 单元测试 | JUnit 5 + Mockito | 核心业务逻辑 ≥ 80% |
| Controller 集成测试 | MockMvc + Testcontainers | 请求→响应全链路 |
| 服务间集成测试 | WireMock | 服务调用契约 |
| 数据库迁移测试 | Testcontainers | 正向 + 回滚 |

## 命名

`{方法名}_{场景}_{期望结果}`

示例：`register_duplicateUsername_409`、`login_expiredToken_401`

## 结构

```java
@Test
void register_validRequest_201() {
    // Given: 准备输入
    // When:  执行被测方法
    // Then:  断言输出
}
```

## 禁止

- 测试依赖执行顺序（每个测试独立）
- 测试访问真实外部服务（用 WireMock 或 Testcontainers）
- 测试中硬编码环境相关值（端口、文件路径）

## TDD

- 先写测试 → 看失败 → 最小实现 → 通过 → 重构
- 详见 `docs/skills/engineering/tdd/SKILL.md`
