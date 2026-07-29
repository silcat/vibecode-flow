# QA 验证

> 用途：qa 阶段的测试用例生成与验证执行
> 触发：qa 阶段启动，implement 完成后

## 1. 生成 test-cases.md

从 plan.md 闭环关卡逐条提取，写入 `docs/work/<branch>/test-cases.md`：

```
| # | 用例 | 来源 | 类型 | 结果 |
|---|------|------|------|------|
| 1 | /api/order/cancel POST → 201 | plan §2.1 | HTTP | |
```

## 2. diff 映射（回归用例）

`git diff --name-only` 获取变更文件，按策略映射测试文件：

| # | 策略 | 模式 | 示例 |
|---|------|------|------|
| A | 同目录 | `Foo.java` → `FooTest.java` | Service/Controller 同目录 |
| B | 导入图 | `rg "ClassName\|methodName" --type java -l` 找引用方测试 | 跨模块影响 |
| C | 配置变更 | `application.yml`、`pom.xml` 等 → 全量 `mvn test` | 基础设施 |
| D | 高扇出 | 模块被 >5 个测试引用 → 全量 `mvn test` | 共享工具类 |

映射结果追加到 test-cases.md：

```
| 3 | OrderServiceTest.testFindAll | git diff (A) | 单元 | |
```

**自动升级全量：** 配置文件变更 / 映射 >70% / plan 要求全量。

**常见陷阱：** 桶形文件 = 高扇出；测试辅助类 = 视为配置；重命名文件 → 检查 `git diff --name-status` 中的 R 条目。

## 3. 执行验证

- **HTTP 用例** → 启动 Spring 容器，逐条构造 HTTP 请求，断言响应状态码和关键字段 → 填 PASS/FAIL
- **回归用例** → `mvn test` 运行映射测试 → 填 PASS/FAIL

**验证标准：**

- 每个测试独立，不依赖执行顺序
- 测试数据清理（每次执行后恢复干净状态）
- 不访问真实外部服务（用 WireMock 或 Testcontainers）
- 不硬编码环境相关值（端口、文件路径）

## 4. 差异感知报告

```
差异感知模式：分析了 N 个变更文件
  已映射：M 个测试（策略 A/B/C/D）
  未映射：K 个文件（无对应测试）
运行了 {N} 个测试：{pass} PASS，{fail} FAIL
```

未映射文件标记：`[!] 未找到 <文件> 的测试 — 建议补充`

## 关联

- qa 工作流：`.codex/agents/qa-agent.toml` 步骤 1-3
- 下一步：代码审查 → `code-review/SKILL.md`

## test-cases.md 格式

写入 `docs/work/<branch>/test-cases.md`：

```

## 测试用例

| # | 用例 | 来源 | 类型 | 结果 |
|---|------|------|------|------|
| 1 | /api/order/cancel POST → 201 | plan §2.1 | HTTP | PASS |
| 2 | /api/order/cancel 重复取消 → 409 | plan §2.1 | HTTP | PASS |
| 3 | OrderServiceTest.testFindAll | git diff (A) | 单元 | PASS |
| 4 | cancelOrder 缺 @Transactional | code-audit P0 | 单元 | TODO |

## 裁定

**Status:** DONE | BLOCKED
**Route:** implement | human
**Failures:**
  - [implement] #1 /api/order/cancel POST → 500
  - [human] 保护区触碰：PaymentGateway 接口变更

## 审计记录

| 日期 | 阶段 | 操作 |
|------|------|------|
```