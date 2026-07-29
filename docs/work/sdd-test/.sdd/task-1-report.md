# Task 1 Report — UserRepository 数据层

## 提交
476aa08 — feat(repo): add UserRepository with save and findByEmail

## 实现
UserRepository: save(User), findByEmail(String)。HashMap 内存存储，save 时重复邮箱抛 DuplicateEmailException。附带 User 实体类、DuplicateEmailException 异常类。

## 测试
2/2 PASS

## TDD 证据
### RED
javac → 10 errors（UserRepository、User、DuplicateEmailException 未定义）
### GREEN
java -ea -cp out UserRepositoryTest → 2/2 PASS

## 自审
- [x] save 后 findByEmail 能查到 → shouldFindSavedUserByEmail PASS
- [x] 重复邮箱抛 DuplicateEmailException → shouldThrowOnDuplicateEmail PASS

## Touchpoints 检查
- [x] UserRepository.java 在触及面内 ✓
- [!] User.java、DuplicateEmailException.java 是 UserRepository 的必要依赖，同包内，未触及其他模块。标记为关注点。

## 关注点
触及面仅声明 UserRepository.java，实际额外创建了 User.java 和 DuplicateEmailException.java 两个同包依赖类。不触公共契约，但建议更新 plan.md 触及面为 src/repository/。