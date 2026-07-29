# 测试用例 — sdd-test

## 用例

| # | 来源 | 用例 | 验证方式 | 结果 |
|---|------|------|---------|------|
| 1 | 阶段 1 | save 后 findByEmail 能查到 | HTTP | PASS |
| 2 | 阶段 1 | 重复邮箱抛 DuplicateEmailException | HTTP | PASS |
| 3 | 阶段 2 | 合法邮箱返回 true | HTTP | PASS |
| 4 | 阶段 2 | 非法邮箱返回 false | HTTP | PASS |
| 5 | 阶段 2 | 空字符串返回 false | HTTP | PASS |
| 6 | 阶段 3 | 合法注册返回 User | HTTP | PASS |
| 7 | 阶段 3 | 非法邮箱抛 InvalidEmailException | HTTP | PASS |
| 8 | 阶段 3 | 已注册抛 DuplicateEmailException | HTTP | PASS |
| 9 | code-audit | @Repository 注解已添加 | 审查 | PASS |

## 裁定

**Status:** DONE
**Route:** —
**Summary:** HTTP: 8/8 | 回归: 8/8 | 审查: 0 TODO
