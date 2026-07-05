# 中断恢复

人类触发恢复（"继续"/关键词）后，AI 按本文档独立完成，不反复询问。

## 恢复入口

1. 读 `docs/work/registry.md`
2. 若"当前激活"有效且状态为 `in-progress` 或 `paused` → 优先定位该计划（人类的快捷书签）
3. 否则 → 扫描 registry 全部行，找所有 `status: in-progress` 的计划，按 `updated` 倒序取第一条
4. 仍无匹配 → 扫描 registry 全部行，找 `status: paused` 的计划，同上
5. 仍无匹配 → 报告"无可恢复计划"，停止

## 定位阶段

1. 读 plan.md
2. 找第一个 `status: in-progress` 的阶段
3. 无 in-progress 阶段，plan frontmatter `status` 为 `in-progress` → 取第一个 `planned` 阶段，将其改为 `in-progress`，从该阶段开始执行
4. plan frontmatter `status` 为 `paused` → 将 plan status 改为 `in-progress`，同步 registry，回到 step 2
5. 所有阶段均为 `completed`，plan 为 `in-progress` → 进入 closure 审计

## 阶段内恢复

1. 定位到阶段后，**先刷新验证状态**——重新执行该阶段闭环关卡对应的验证操作，根据实际结果更新 checkbox 状态。"验证证据"字段作为参考但不驱动自动化
2. 找该阶段第一个未勾 checkbox，从该子任务恢复执行
3. 阶段全部 checkbox 已勾 → 阶段状态改为 `completed`，同步 registry
4. 存在下一个阶段且状态为 `planned` → 将其改为 `in-progress`，继续执行，不等待人类再次触发

## 多计划项目恢复

若恢复的是子计划，子计划完成后按 [template-sub.md](template-sub.md) §多计划状态联动规则 执行联动。

主计划级别的编排循环中断 → 从 [program-management.md](../project/program-management.md) §编排循环 步骤 1 重新扫描。
