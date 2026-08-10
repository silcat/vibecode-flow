# QA 验证

> 用途：qa 阶段的测试用例生成与验证执行
> 触发：qa 阶段启动，implement 完成后

## 1. 生成 test-cases.md（仅首次）

首次 qa 从 plan.md 闭环关卡逐条提取，写入 `docs/work/<branch>/test-cases.md`。修复模式重入时**不重建**，按 §5 增量重验。

用例表（行为/回归验证）：

```
| # | 来源 | 关联 task | 用例 | 类型 | 结果 |
|---|------|-----------|------|------|------|
| 1 | 阶段 1 | task-1-report.md | /api/order/cancel POST → 201 | HTTP | |
```

**来源与关联规则：**

| 来源 | 关联 task | 说明 |
|------|-----------|------|
| 阶段 N | `task-N-report.md`（必须） | 来自 plan.md 闭环关卡，参与映射校验 |
| git diff (A/B/C/D) | 变更所属 task，无法定位写 `—` | 回归用例 |
| code-audit | `—`（不绑定 task） | 追加到 `## 审计缺陷`，独立编号 A1/A2… |

**映射校验（qa 输出）：** 闭环关卡 ↔ task-N-report 自审项 ↔ 用例三向对齐；code-audit 缺陷项不参与 task 映射。

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
- **审计缺陷** → 复核缺陷是否已修复，修复填 PASS，未修复填 TODO

**结果取值：** `PASS` / `FAIL` / `TODO`；修复重验通过后标记 `PASS（R<N>）`（仍失败则 `FAIL（R<N>）`），N 为修复轮次。

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
映射校验：闭环关卡 X/X 已覆盖 | task 自审项 Y/Y 有对应用例 | 审计缺陷 Z 条（不参与 task 映射）
运行了 {N} 个测试：{pass} PASS，{fail} FAIL
```

未映射文件标记：`[!] 未找到 <文件> 的测试 — 建议补充`

## 5. 修复重验（增量）

修复模式重入 qa 时：

1. 读 `## 裁定` Failures 清单
2. 逐条重跑：按 # 定位用例或审计缺陷 → 通过填 `PASS（R<N>）`，仍失败填 `FAIL（R<N>）`
3. 按 git diff 新增影响重跑相关回归用例，结果就地更新
4. 全部 Failures 解决且无新 FAIL/TODO → 重新裁定；仍有 → 保持 BLOCKED
5. `## 修复历史` 追加记录（用例 #、轮次、提交、结果）

## 关联

- qa 工作流：`docs/process/flows/stages/qa/README.md` 工作流 1
- 下一步：代码审查 → `code-review/SKILL.md`

## test-cases.md 格式

写入 `docs/work/<branch>/test-cases.md`：

```markdown
## 用例

| # | 来源 | 关联 task | 用例 | 类型 | 结果 |
|---|------|-----------|------|------|------|
| 1 | 阶段 1 | task-1-report.md | /api/order/cancel POST → 201 | HTTP | PASS |
| 2 | 阶段 1 | task-1-report.md | /api/order/cancel 重复取消 → 409 | HTTP | PASS |
| 3 | git diff (A) | task-2-report.md | OrderServiceTest.testFindAll | 单元 | PASS |
| 4 | git diff (C) | — | 配置变更全量回归 | 单元 | PASS（R1） |

## 审计缺陷

| # | 严重级别 | 问题 | 范围 | 结果 | 修复轮 |
|---|---------|------|------|------|--------|
| A1 | P0 | cancelOrder 缺 @Transactional | 全局 | PASS | R1 |

## 修复历史

| # | 轮次 | 提交 | 结果 |
|---|------|------|------|
| 4 | R1 | f3c4d5e | PASS（R1） |
| A1 | R1 | f3c4d5e | PASS（R1） |

## 裁定

**Status:** DONE | BLOCKED
**Route:** done | implement | human
**Summary:** 用例 HTTP: 6/6 | 回归: 14/14 | 审计缺陷: 0 TODO
**Failures:**
  - [implement] #2 /api/order/cancel 重复取消 → 409
  - [human] 保护区触碰：PaymentGateway 接口变更

## 审计记录

**审计结论:** 通过闭环审计 | 需要修改 | —（未完成）

| 日期 | 阶段 | 操作/发现 |
|------|------|---------|
```

裁定规则：Status 是结论、Route 是唯一路由依据，只允许 DONE/done、BLOCKED/implement、BLOCKED/human 三组组合；`## 审计记录` 的审计结论必须与 Status 一致（通过闭环审计 → DONE，需要修改 → BLOCKED，未完成 → BLOCKED/human）。
