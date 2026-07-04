# 工作产物目录

一个目录 = 一次工作。所有相关产物同目录共存。

## 结构

### 完整/轻量级别（单计划）

```
work/
├── registry.md              ← 唯一状态仪表盘
├── input/                   ← 共享原始材料（PM 笔记等）
├── <工作目录>/               ← 一次工作的所有产物
│   ├── discussion.md        ← clarify 阶段产出
│   ├── requirement.md       ← requirement 阶段产出
│   ├── research.md          ← research 阶段产出
│   └── plan.md              ← plan 阶段产出
└── README.md
```

### 项目级别（总计划 + 子计划）

```
work/
├── registry.md              ← 唯一状态仪表盘
├── input/                   ← 共享原始材料
├── <项目目录>/               ← 总计划
│   ├── plan.md              ← 总计划（含子计划清单、依赖图、集成关卡）
│   └── requirement.md       ← 总需求
├── <子计划1>/                ← 子计划（普通工作目录）
│   ├── plan.md              ← 子计划 plan.md
│   └── requirement.md       ← 从总需求提取的子集
├── <子计划2>/
│   ├── plan.md
│   └── requirement.md
└── README.md
```

## 产物与阶段对应

| 文件 | 产出阶段 | 适用级别 |
|------|---------|---------|
| `discussion.md` | `clarify` | 完整、轻量 |
| `requirement.md` | `requirement` | 所有级别。子计划由 AI 从总需求提取生成 |
| `research.md` | `research`（条件触发） | 完整、轻量 |
| `plan.md` | `plan` | 所有级别。总计划 frontmatter `type: master`，子计划 `type: sub` |

## 规则

- 目录名是人工可读的工作标识
- 产物模板和阶段协议见 `docs/process/flows/stages/<stage>/`
- 工作完成 → 目录保留，从 registry.md 删除对应行。总计划在所有子计划完成 + 集成审计通过后删除
- log 阶段产出写入 `docs/logs/`，不在 work 目录下
