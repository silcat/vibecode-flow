# Spring Boot 数据库设计规范

## 用途

创建表、编写 SQL、设计 Schema、MyBatis Mapper、索引设计时的强制性规范。

## 表设计规范

- 每张表必须有 `id`（BIGINT 自增）、`created_at`（DATETIME）、`updated_at`（DATETIME）
- 每个字段必须有 COMMENT
- 表名使用小写蛇形命名（`snake_case`），复数形式
- 字段类型选择最小够用的：状态字段用 TINYINT，布尔字段用 TINYINT(1)

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    status TINYINT DEFAULT 1 COMMENT '状态：1-正常 0-禁用',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='用户表';
```

## Entity 规范

- 实体类使用 `@TableName` 指定表名
- 主键使用 `@TableId(type = IdType.AUTO)`
- 逻辑删除字段使用 `@TableLogic`
- `created_at` 和 `updated_at` 由数据库自动管理，Entity 中不手动赋值

## Mapper 规范

- Mapper 接口继承 MyBatis Plus `BaseMapper<T>`
- 自定义 SQL 写在 XML 中，不在 Java 代码里拼接 SQL
- 参数用 `#{}` 预编译，禁止 `${}` 字符串拼接（除动态表名/排序字段已验证白名单外）

```xml
<!-- 安全：#{} 使用 PreparedStatement -->
<select id="getUserByName" resultType="User">
    SELECT * FROM users WHERE username = #{username}
</select>

<!-- 危险：${} 是字符串拼接 -->
<select id="getUserByName" resultType="User">
    SELECT * FROM users WHERE username = '${username}'
</select>
```

## 批量操作

- 批量插入使用 `saveBatch()`，批量更新使用 `updateBatchById()`
- 禁止在循环中逐条调用 Mapper

## 索引

- 所有 WHERE、JOIN、ORDER BY 字段必须有索引
- 组合索引遵循最左前缀原则
- 唯一约束用 UNIQUE 索引实现
