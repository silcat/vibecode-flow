---
branch: feature-sdd-test
status: planned
type: standalone
parent: none
requirement: none
created: 2026-07-30
updated: 2026-07-30
blocker: none
---

# SDD 流程验证 计划

## 当前基线

测试项目，无实际代码基线。

## 阶段 1：[P] UserRepository 数据层

- **状态**：planned
- **依赖**：无
- **目标**：创建 UserRepository，支持 save 和 findByEmail
- **非目标**：不涉及数据库迁移、不涉及缓存
- **触及面**：src/repository/UserRepository.java
- **公共契约**：UserRepository.save(User), UserRepository.findByEmail(String)
- **恢复指引**：从 UserRepository.java 开始
- **闭环关卡**：
  - [ ] save 后 findByEmail 能查到
  - [ ] 重复邮箱抛 DuplicateEmailException
- **验证证据**：单元测试输出

## 阶段 2：[P] EmailValidator 校验层

- **状态**：planned
- **依赖**：无
- **目标**：创建 EmailValidator，校验邮箱格式
- **非目标**：不涉及 DNS 验证、不涉及 SMTP 检查
- **触及面**：src/validator/EmailValidator.java
- **公共契约**：EmailValidator.isValid(String): boolean
- **恢复指引**：从 EmailValidator.java 开始
- **闭环关卡**：
  - [ ] 合法邮箱返回 true
  - [ ] 非法邮箱返回 false
  - [ ] 空字符串返回 false
- **验证证据**：单元测试输出

## 阶段 3：UserService 业务层

- **状态**：planned
- **依赖**：阶段 1（UserRepository）、阶段 2（EmailValidator）
- **目标**：创建 UserService，整合 Repository 和 Validator，实现注册流程
- **非目标**：不涉及密码加密、不涉及 JWT 签发
- **触及面**：src/service/UserService.java
- **公共契约**：UserService.register(String email): User
- **恢复指引**：从 UserService.java 开始
- **闭环关卡**：
  - [ ] 合法邮箱 + 未注册 → 注册成功返回 User
  - [ ] 非法邮箱 → 抛 InvalidEmailException
  - [ ] 已注册邮箱 → 抛 DuplicateEmailException
- **验证证据**：单元测试输出

## 测试矩阵

| 验收标准 | 测试断言 | 测试文件 | 类型 |
|---------|---------|---------|------|
| save 后能查到 | assertEquals | UserRepositoryTest | 单元 |
| 重复邮箱抛异常 | assertThrows | UserRepositoryTest | 单元 |
| 合法邮箱返回 true | assertTrue | EmailValidatorTest | 单元 |
| 非法邮箱返回 false | assertFalse | EmailValidatorTest | 单元 |
| 空字符串返回 false | assertFalse | EmailValidatorTest | 单元 |
| 合法注册返回 User | assertNotNull | UserServiceTest | 单元 |
| 非法邮箱抛异常 | assertThrows | UserServiceTest | 单元 |
| 已注册抛异常 | assertThrows | UserServiceTest | 单元 |

## Skill

- Skill: sdd
- 调研引用：无
