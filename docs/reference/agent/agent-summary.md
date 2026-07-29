# Agent 骨架与作用汇总（12 个）

> 位置：\.claude/agents/*.md\（Claude 原生）+ \.codex/agents/*.toml\（Codex 镜像）

---

## RIPER-5 模式 Agent（6 个）

### vc-research-agent（RESEARCH 模式）
作用：只读收集信息，不建不建议

  ├─ 公共骨架
  │   ├─ Purpose               任务目标说明
  │   ├─ Permitted Activities  允许的操作（Read/Grep/Glob/Bash/WebSearch）
  │   ├─ Strictly Forbidden    禁止的操作（Write/Edit/Delete）
  │   ├─ Phase Lock            阶段锁定警告
  │   ├─ Completion            完成标志 → "go" 到 INNOVATE
  │   ├─ Ready for Next Phase  过渡条件（仅限"go"/"ENTER INNOVATE MODE"）
  │   ├─ Violation Prevention  越界自检（PHASE JUMPING PREVENTED）
  │   ├─ Example Session       示例
  │   ├─ Tool Usage            工具使用说明
  │   └─ Status Reporting      状态块（DONE/DONE_WITH_CONCERNS/BLOCKED/NEEDS_CONTEXT）
  └─ 非公共（特有）
      ├─ Research Quality Checklist     研究质量检核表（6项：多源/官方/日期/矛盾/证据/遗留问题）
      ├─ Evidence Capture               证据捕获规范（精确错误文本/命令/堆栈/时间戳）
      ├─ Context Validation             all-context.md → 最小路由 → 环境变量/导入路径验证
      └─ External Research              外部研究规范（官方文档优先/多源交叉/标注日期）

### vc-innovate-agent（INNOVATE 模式）
作用：头脑风暴方案讨论，不做决定

  ├─ 公共骨架（同 research-agent，无 Bash 工具）
  │   ├─ Purpose
  │   ├─ Permitted Activities（Read/Grep/Glob，无 Bash）
  │   ├─ Strictly Forbidden
  │   ├─ Phase Lock
  │   ├─ Completion → 需产出 Decision Summary
  │   ├─ Ready for Next Phase
  │   ├─ Violation Prevention
  │   ├─ Example Session
  │   ├─ Tool Usage
  │   └─ Status Reporting
  └─ 非公共（特有）
      ├─ Brainstorm Quality Checklist      头脑风暴质量检核表（6项：质疑假设/探索2-3方案/对比维度/二阶效应/最简方案/决策摘要）
      ├─ Architecture Validation Gate      架构验证门禁（新服务/DB变更/第三方/规模化/>2h）
      │   └─ Comparison Guidance           对比维度：复杂度/工期/运营风险/可维护性/性能/成本/向后兼容
      ├─ Decision Summary                  决策摘要（必出：选定方案+被拒方案+理由）
      └─ ADR                               架构决策记录（可选，复杂决策扩展）

### vc-plan-agent（PLAN 模式）
作用：写详尽技术规格和实现计划，零歧义

  ├─ 公共骨架（同 research-agent + Context Routing）
  │   ├─ Purpose
  │   ├─ Context Routing
  │   ├─ Permitted Activities（+Write，仅限 process/*/active/）
  │   ├─ Strictly Forbidden
  │   ├─ Phase Lock
  │   ├─ Completion → "ENTER EXECUTE MODE"
  │   ├─ Ready for Next Phase
  │   ├─ Violation Prevention
  │   ├─ Example Session
  │   ├─ Tool Usage
  │   └─ Status Reporting
  └─ 非公共（特有）
      ├─ Plan Artifact Exception           写文件例外（仅 process/general-plans/ 和 process/features/*/）
      ├─ Workflow Integration              vc-generate-plan 技能契约 + 阶段程序引用
      │   ├─ Step 1: Check for Existing Plan   扫描 active/ 避免重复
      │   └─ Step 2: Update Existing Plan      集成 RESEARCH/INNOVATE 产出
      ├─ Checklist Output                  清单输出格式
      ├─ Plan Quality Requirements         9项质量检核表（数据流/依赖/风险/兼容/测试矩阵/回滚/成功标准/验证器/）
      └─ Anti-Rationalization              反合理化警告（"我已知怎么做"不是计划）

### vc-execute-agent（EXECUTE 模式）
作用：严格按批准计划实现，精确执行

  ├─ 公共骨架（同 research-agent + Context Loading + Entry Requirement）
  │   ├─ Purpose
  │   ├─ Entry Requirement             仅限"ENTER EXECUTE MODE"
  │   ├─ Context Loading               all-context.md → 最小相关文档
  │   ├─ Permitted Activities          全工具（Read/Write/Edit/Grep/Glob/Bash/Delete）
  │   ├─ Strictly Forbidden            偏离计划
  │   ├─ Phase Lock
  │   ├─ Completion → 关闭包分类（Ready/Keep/Needs reconciliation）
  │   ├─ Ready for Next Phase
  │   ├─ Violation Prevention
  │   ├─ Example Session
  │   ├─ Tool Usage
  │   └─ Status Reporting
  └─ 非公共（特有）
      ├─ Plan File Verification             必须显式计划文件路径，否则 STOP
      ├─ Deviation Handling                 发现偏离→立即STOP→说明→回PLAN→等审批
      ├─ Mid-Implementation Check-In        50%时进度检查
      ├─ Specialist Agent Delegation        可调用子Agent：tester/debugger/code-reviewer/code-simplifier/ui-ux-designer/git-manager
      ├─ Self-Review After Execution        执行后自审
      ├─ Implementation Discipline          实现纪律
      ├─ Approach Abandonment Protocol      方案废弃协议
      ├─ Verification Discipline            验证纪律（高风险需证据包：risk-gate.json/context-snippets.json/verification.json等）
      └─ Completion                         关闭包：selected plan path + 完成内容 + 已验证/未验证 + 下一步

### vc-fast-mode-agent（FAST 模式）
作用：压缩版 RIPER-5（RESEARCH+INNOVATE+PLAN 单轮），强制 PAUSE 后执行

  ├─ 公共骨架（部分）
  │   ├─ Purpose
  │   ├─ Entry Requirement             仅限"ENTER FAST MODE"
  │   ├─ Phase Lock Enforcement
  │   ├─ Completion
  │   └─ Example Session
  └─ 非公共（特有）
      ├─ Required Workflow                  单轮压缩流程，明确分段
      │   ├─ [RESEARCH]                     收集上下文
      │   ├─ [INNOVATE]                     2-3方案+推荐
      │   ├─ [PLAN]                         写计划+清单
      │   └─ **MANDATORY PAUSE**            强制暂停，等"ENTER EXECUTE MODE"
      ├─ Important Notes                    压缩注意事项
      ├─ Difference from Default Mode       对比标准模式差异
      │   └─ Default = 逐阶段确认；FAST = 压缩到 PLAN 后等待一次
      └─ Output Format                      [MODE: FAST] 格式模板

### vc-update-process-agent（UPDATE PROCESS 模式）
作用：复盘执行、生成改进、归档计划、沉淀知识

  ├─ 公共骨架（部分）
  │   ├─ Purpose
  │   ├─ Entry Requirement             仅限"ENTER UPDATE PROCESS MODE"
  │   ├─ Violation Prevention
  │   ├─ Completion
  │   └─ Example Session
  └─ 非公共（特有）
      ├─ Required 6-Phase Process           固定六阶段
      │   ├─ Phase 1: Conversation Analysis         分析对话+失败分析
      │   ├─ Phase 2: Improvement Generation        分类改进（memory/plan/phase-program/code-standards/context/agent/skill/hooks）
      │   ├─ Phase 3: User Approval                 用户审批
      │   ├─ Phase 4: Implementation                执行变更
      │   ├─ Phase 5: Final Review                  最终审查
      │   └─ Phase 6: Completion                    完成
      ├─ Plan File Archiving Pattern         归档流程（mv→验证→清理，防编辑器残留）
      └─ Output Format                       [MODE: UPDATE PROCESS] + 六阶段标志

---

## Specialist Agent（6 个，无公共骨架，角色定制）

### vc-debugger（Senior SRE）
作用：根因分析，证据驱动排查（日志/数据库/性能/CI）

  └─ 定制标题
      ├─ Behavioral Checklist             9项检核表（并发/假设/时间线/环境/证据链/预防）
      ├─ Core Competencies                核心能力（问题调查/DB诊断/日志分析/性能优化/技能调用）
      ├─ Investigation Methodology        5步方法论
      │   ├─ 1. Scope Definition             确定范围
      │   ├─ 2. Evidence Collection          收集证据
      │   ├─ 3. Hypothesis Formation         形成2-3竞争假设
      │   ├─ 4. Systematic Diagnosis         系统诊断（含高风险证据包）
      │   └─ 5. Solution Development         方案边界→交 execute-agent
      ├─ Tools and Techniques             工具链（Prisma/PGlite/sqlite3/日志/CI）
      ├─ Reporting Standards              报告标准（执行摘要+技术分析+建议+证据）
      ├─ Best Practices                   最佳实践
      ├─ Communication Approach           沟通方式
      └─ Report Output                    报告输出

### vc-tester（QA Lead）
作用：差异感知测试验证

  └─ 定制标题
      ├─ Project Test Configuration           项目测试配置（pnpm/npm/pip/策略）
      └─ Diff-Aware Mode（Default）            差异感知模式
          ├─ Strategy A                         仅改动的文件
          ├─ Strategy B                         改动+依赖文件
          └─ Strategy C                         全量（>70%映射）
      └─ Report Output                      报告输出（结果/覆盖率/失败/性能/构建/风险）

### vc-code-reviewer（Staff Engineer）
作用：生产就绪度审查（并发/N+1/安全/数据泄露）

  └─ 定制标题
      ├─ Behavioral Checklist               9项检核表（并发/错误边界/API契约/兼容/输入验证/权限/N+1/数据泄露/高风险决策）
      ├─ Core Responsibilities              7项职责（质量/类型安全/构建/性能/安全/完整性/审查边界）
      ├─ Review Process                     审查流程
      │   ├─ 1. Edge Case Scouting             边界侦查（先读 vc-scout）
      │   ├─ 2. Initial Analysis               初始分析
      │   ├─ 3. Systematic Review              系统审查（结构/逻辑/类型/性能/安全）
      │   └─ 4. Prioritization                 优先级划分（Critical/High/Medium/Low）
      ├─ Output Format                      输出格式
      ├─ Code Review Summary                审查摘要
      ├─ Guidelines                         指南
      └─ Report Output                      报告输出

### vc-code-simplifier（简化专家）
作用：代码精简，保持行为不变

  └─ 无 ## 标题，纯段落
      ├─ 角色：Expert code simplification specialist
      ├─ 5条简化原则（保持功能 → 项目标准 → 清晰 → 平衡 → 范围聚焦）
      └─ 7步流程（识别 → 分析 → 应用标准 → 保持功能 → 验证 → 运行检查 → 停止扩展）

### vc-ui-ux-designer（UI/UX 设计师）
作用：设计感知前端实现（界面/响应式/动画/无障碍）

  └─ 定制标题
      ├─ Required Skills（Priority Order）     依赖技能（vc-frontend-design / browser / docs）
      ├─ Expert Capabilities                   专家能力（设计适应/摄影/UX优化）
      ├─ Core Responsibilities                 核心职责（4阶段）
      │   ├─ 1. Design Phase                     设计阶段
      │   ├─ 2. Implementation Phase             实现阶段
      │   ├─ 3. Validation Phase                 验证阶段
      │   └─ 4. Documentation Phase              文档阶段
      ├─ Report Output                         报告输出
      ├─ Available Tools                       可用工具
      ├─ Design Workflow                       设计工作流
      ├─ Design Principles                     设计原则（Mobile-First/无障碍/一致性/性能/清晰/愉悦/包容/趋势/转化/品牌）
      ├─ Quality Standards                     质量标准（响应式/对比度/交互动画/reduced-motion/触摸目标/行高/越南文支持）
      ├─ Error Handling                        错误处理
      └─ Collaboration                         协作规范

### vc-git-manager（Git Ops Specialist）
作用：规范提交管理（conventional commits）

  └─ 定制标题
      ├─ Git Operations Workflow               8步流程（status → scope → add → parity校验 → plan校验 → diff-check → commit → push）
      ├─ Worktree Analysis Workflow            touched_files 差异分析 + 逻辑拆分
      │   └─ 4种分组：feature code/tests/config/types
      ├─ Conventional Commit Standards         类型标准（feat/fix/refactor/docs/style/test/chore）
      └─ Safety Rules                          安全规则（禁止 force push/禁止 .env/禁止 --no-verify/避免模糊 scope）
