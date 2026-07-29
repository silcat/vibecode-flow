# Skill 骨架与作用汇总（31 个）

> 位置：\.agents/skills/*/SKILL.md\（symlink to \.claude/skills/\）

---

## Contract Skills（8 个）

### vc-generate-plan
作用：创建/更新 SIMPLE 或 COMPLEX 格式的实现计划

  ├─ 骨架
  │   ├─ Workflow             工作流程
  │   └─ Important Rules      重要规则
  └─ 非骨架

### vc-generate-context
作用：生成/更新 \process/context/all-context.md\

  ├─ 骨架（同 vc-generate-plan）
  └─ 非骨架

### vc-audit-context
作用：审计上下文路由、skill 发现、Claude/Codex 接线

  ├─ 骨架（同 vc-generate-plan）
  └─ 非骨架

### vc-audit-plans
作用：审计计划文件陈旧度、完整性、路由准确性

  ├─ 骨架
  │   ├─ Workflow             工作流程
  │   └─ Output               输出格式
  └─ 非骨架

### vc-audit-vc
作用：审计 Agent Harness 健康度（agent 一致性、skill 注册、README 同步、协议接线）

  ├─ 骨架（同 vc-generate-plan）
  └─ 非骨架

### vc-publish
作用：将本地 Harness 改进推送到远程 Kit 仓库

  ├─ 骨架（同 vc-generate-plan）
  └─ 非骨架（特有）
      ├─ Prerequisites         前置条件
      ├─ Configuration         配置
      └─ Key Changes from v1.0 版本变更记录

### vc-update
作用：从远程 Kit 仓库拉取最新 Harness 改进

  ├─ 骨架（同 vc-generate-plan）
  └─ 非骨架（特有）
      ├─ When to Use           何时使用
      └─ Reference             参考

### vc-setup
作用：交互式初始化 Agent Harness（检测栈、scaffold 目录、填充 context）

  ├─ 骨架
  │   └─ Workflow             工作流程
  └─ 非骨架（特有）
      └─ Prerequisites         前置条件

---

## Helper Skills — 思考型（3 个）

### vc-sequential-thinking
作用：多步推理、假设验证、问题分解、路径修正

  ├─ 骨架
  │   ├─ When to Apply / When to Use  适用条件
  │   ├─ Core Process / Core Method   核心方法
  │   └─ References                   参考
  └─ 非骨架（特有）
      ├─ Application Modes       应用模式
      └─ Scripts                 脚本

### vc-problem-solving
作用：卡住时系统化解决（复杂度螺旋、创新阻塞、假设约束）

  ├─ 骨架（同 vc-sequential-thinking）
  └─ 非骨架（特有）
      ├─ Quick Dispatch          快速分派
      ├─ Core Techniques         核心技术
      ├─ Application Process     应用流程
      └─ Combining Techniques    组合技巧

### vc-debug
作用：根因分析后修 bug（测试失败/性能/CI）

  ├─ 骨架（同 vc-sequential-thinking）
  └─ 非骨架（特有）
      ├─ Core Principle          核心原则
      ├─ Techniques              技术
      ├─ Quick Reference         快速参考
      ├─ Tools Integration       工具集成
      └─ Red Flags               危险信号

---

## Helper Skills — 浏览器/测试型（4 个）

### vc-chrome-devtools
作用：Puppeteer 脚本自动化浏览器（截图/性能/爬虫/表单/JS 调试）

  ├─ 骨架
  │   ├─ Quick Start / Skill Location  快速开始/技能定位
  │   ├─ Workflow / Approach           工作流程/方法
  │   └─ Project-Specific Setup        项目特定配置
  └─ 非骨架（特有）
      └─ Choosing Your Approach        方法选择

### vc-agent-browser
作用：AI 优化浏览器自动化 CLI，长会话/自验证/录屏/云浏览器

  ├─ 骨架（同 vc-chrome-devtools）
  └─ 非骨架（特有）
      ├─ Quick Start                   快速开始
      └─ Core Workflow                 核心流程

### vc-web-testing
作用：Playwright/Vitest/k6 全类型测试

  ├─ 骨架（同 vc-chrome-devtools）
  └─ 非骨架（特有）
      ├─ Quick Start                   快速开始
      ├─ Testing Strategy              测试策略
      ├─ Reference Documentation       参考文档
      ├─ Scripts                       脚本
      └─ CI/CD Integration             CI/CD 集成

### vc-frontend-design
作用：从设计稿/截图/视频创建前端界面

  ├─ 骨架（同 vc-chrome-devtools）
  └─ 非骨架（特有）
      ├─ Workflow Selection            工作流选择
      ├─ Design Dials                  设计旋钮
      ├─ Design Thinking               设计思维
      ├─ Frontend Aesthetics           前端美学指南
      ├─ Asset & Analysis References   资产/分析参考
      └─ Anti-Patterns (AI Slop)       反模式

---

## Helper Skills — 搜索/研究型（3 个）

### vc-scout
作用：快速代码库侦查（shell 搜索 + 并行 research agent）

  ├─ 骨架
  │   ├─ Arguments / Overview           参数/概述
  │   ├─ Workflow                       工作流程
  │   ├─ Output / Report Format         输出/报告格式
  │   └─ Relevant Files / References     相关文件/参考
  └─ 非骨架（特有）
      ├─ Arguments                      参数
      ├─ Quick Start                    快速开始
      ├─ Configuration                  配置
      └─ Unresolved Questions           未解决问题

### vc-docs-seeker
作用：通过 llms.txt 搜索库/框架文档（API docs/GitHub 分析）

  ├─ 骨架（同 vc-scout）
  └─ 非骨架（特有）
      ├─ Overview                       概述
      ├─ Primary Workflow               主工作流
      ├─ Scripts                        脚本
      ├─ Workflow References            工作流参考
      ├─ Execution Principles           执行原则
      ├─ Quick Start                    快速开始
      └─ Environment                    环境

### vc-xia
作用：比较仓库、提取特性思路、适配研究（不规划不实现）

  ├─ 骨架（同 vc-scout）
  └─ 非骨架（特有）
      ├─ Approved Modes                 批准模式
      ├─ Core Principles                核心原则
      ├─ Output Policy                  输出策略
      ├─ Preferred Workflow             优选工作流
      ├─ Handoff Rule                   转交规则
      ├─ Safety Rules                   安全规则
      ├─ Output Expectations            输出预期
      └─ Good Trigger Phrases           触发词

---

## Helper Skills — 边界检查型（4 个）

### vc-scenario
作用：按 12 维度生成边缘案例和测试场景

  ├─ 骨架
  │   ├─ When to Use                何时使用
  │   ├─ When NOT to Use            何时不用
  │   └─ Core Method / Workflow     核心方法
  └─ 非骨架（特有）
      └─ Integration with Other Skills  与其他 skill 集成

### vc-security
作用：STRIDE + OWASP 安全审计，可选自动修复

  ├─ 骨架（同 vc-scenario）
  └─ 非骨架（特有）
      ├─ Audit Methodology          审计方法论
      └─ Fix Mode (--fix)           修复模式

### vc-predict
作用：5 专家角色辩论变更方案（架构/安全/性能/UX）

  ├─ 骨架（同 vc-scenario）
  └─ 非骨架（特有）
      ├─ Debate Protocol            辩论协议
      ├─ Verdict Levels             裁决等级
      └─ Example Invocations        调用示例

### vc-autoresearch
作用：自主迭代优化循环（覆盖率/性能/体积等可量化指标）

  ├─ 骨架（同 vc-scenario）
  └─ 非骨架（特有）
      ├─ Configuration Format       配置格式
      ├─ Interactive Setup          交互式配置
      ├─ Core Protocol              核心协议
      ├─ Results Logging            结果日志
      ├─ Stuck Detection            卡住检测
      ├─ Example Invocations        调用示例
      └─ Limitations                局限性

---

## 其他独立 Skill（9 个）—— 无公共骨架

### vc-context-engineering
作用：检查上下文限制、优化 token 使用、调试上下文故障

  └─ 结构：When to Activate → Core Principles → Quick Reference
          → Key Metrics → 4-Bucket Strategy → Anti-Patterns
          → Guidelines → Runtime Awareness → Scripts

### vc-docs
作用：分析代码库、初始化/更新/总结项目文档

  └─ 结构：Default (No Arguments) → Subcommands → Routing → Shared Context

### vc-mcp-management
作用：管理 MCP 服务器（发现/分析/执行 tools/prompts/resources）

  └─ 结构：Overview → When to Use → Core Capabilities → Implementation Patterns
          → Scripts Reference → Quick Start → Technical Details → Integration Strategy

### vc-merge-worktree
作用：合并 git worktree 分支回主仓库并清理

  └─ 结构：Workflow → Safety

### vc-repomix
作用：将仓库打包成 AI 友好参考工件（研究/审计/移植准备）

  └─ 结构：When To Use → Output Policy → Install And Invocation → Common Flows
          → Token And Size Review → Security Rules → Remote Sources → Troubleshooting

### vc-tech-graph
作用：生成发布级 SVG/PNG 技术图表（架构/流程/时序/UML）

  └─ 结构：Capability Surface → Routing Matrix → Output Policy → Diagram Types
          → Workflow → Helper Scripts → Dependency Contract → Good Trigger Phrases

### vc-preview
作用：检查文件、生成可视化解释/图表/HTML 摘要

  └─ 结构：Diagram Boundary → Default (No Arguments) → Usage → Argument Resolution
          → Error Handling → HTML Output Mode

### vc-team
作用：编排多 Agent 并行协作团队（研究/实现/审查/debug）

  └─ 结构：Usage → Execution Protocol → CK Context Block
          → ON /vc:team execute → ON /vc:team debug

### vc-watzup
作用：只读手交摘要（分支/ref/worktree/计划/下一步建议）

  └─ 结构：Core Contract → Invocation → Input Sources → Output Shape → Safety Rules
