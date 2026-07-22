# 项目上下文

## 项目标识

<项目名称>

## 文档新鲜度

`unknown`

值域和维护规则见 `docs/baseline/context/ai-autonomy-policy.md`。

## 活跃工作

完整状态见 `docs/work/registry.md`。AI 自治策略见 `docs/baseline/context/ai-autonomy-policy.md`。

## 当前技术基线

## 验证命令

| 用途 | 命令 |
|------|-----|
| 安装依赖（平台） | `` |
| 编译检查（平台） | `` |
| 单元测试（平台） | `` |
| 集成测试（平台） | `` |
| 本地运行（平台） | `` |

## 当前启用的可选层

- [x] `docs/skills/`
- [x] `docs/retro/`
- [x] `docs/baseline/architecture/`
- [x] `docs/baseline/standards/`

## AI 阻塞条件

以下情况 AI 必须停止并等待人类输入：

- 任何变更触及两个以上微服务的公共契约
- 修改 API 版本化策略或废弃现有 API 版本

## AI 自治策略

- **自治级别**：`实施`——AI 可直接编写代码、运行验证，无需逐条确认
- **保护区**：支付/资金相关代码、Gateway 过滤器、DDL、跨服务契约变更、API 版本废弃 → 必须先写入 `discussion.md` 等待确认
- **审查触发**：修改超过 5 个文件、涉及保护区、或修改 Feign 接口时触发独立审计


