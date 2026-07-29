# Task 3 Report — UserService 业务层

## 提交
c3d4e5f — feat(service): add UserService with register flow

## 实现
UserService: register(String email)。注入 UserRepository 和 EmailValidator，先校验邮箱格式，再查重，最后保存。依赖 Task 1 和 Task 2 的公共契约。

## 测试
3/3 PASS

## TDD 证据
### RED
mvn test → 3 FAIL（UserService 类未定义）
### GREEN
mvn test → 3 PASS

## 自审
- [x] 合法邮箱 + 未注册 → 注册成功返回 User → UserServiceTest.shouldRegisterNewUser PASS
- [x] 非法邮箱 → 抛 InvalidEmailException → UserServiceTest.shouldRejectInvalidEmail PASS
- [x] 已注册邮箱 → 抛 DuplicateEmailException → UserServiceTest.shouldRejectDuplicateEmail PASS

## Touchpoints 检查
- [x] 全部变更在触及面内（仅 src/service/UserService.java + 测试文件） ✓

## 关注点
无
