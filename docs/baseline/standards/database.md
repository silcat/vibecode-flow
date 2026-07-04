# 数据库规范

> 触发场景：新增表/字段/索引、编写迁移脚本

## 命名

- 表名：蛇形、复数（`users`、`adoption_records`）
- 字段：蛇形（`created_at`、`updated_by`）
- 索引：`idx_{表}_{字段}`（`idx_users_email`）
- 唯一约束：`uk_{表}_{字段}`（`uk_users_phone`）

## 必备字段

```sql
id          BIGINT PRIMARY KEY AUTO_INCREMENT,
created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
```

## 字段类型

| 数据 | 类型 |
|------|------|
| 主键 | BIGINT |
| 字符串 | VARCHAR(N)，不定长 TEXT |
| 布尔 | TINYINT(1) |
| 金额 | DECIMAL(12,2) |
| 枚举 | VARCHAR(32)，不用 ENUM |
| JSON | JSON 类型（MySQL 5.7+） |

## 迁移

- 工具：Flyway
- 文件命名：`V{序号}__{描述}.sql`
- 每次变更新建文件，不修改已有迁移
- 迁移必须可回滚（提供对应 undo 脚本）

## 约束

- 禁止物理外键（应用层维护引用完整性）
- 软删除：`deleted_at DATETIME DEFAULT NULL`
- 查询加 `WHERE deleted_at IS NULL`
- 密码：bcrypt 存储，禁止明文或 MD5

## 索引

- 高频查询字段必建索引
- 联合索引左前缀原则
- 禁止在 TEXT/BLOB 上建索引
