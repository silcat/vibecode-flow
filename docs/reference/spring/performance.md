# Spring Boot 性能规范

## 用途

数据库查询、列表接口、批量数据、缓存策略、高并发场景时的强制性规范。

## 红线 1：禁止循环查库（N+1）

```java
// 禁止
for (Order order : orders) {
    User user = userMapper.selectById(order.getUserId());
}

// 正确：批量查询 + 内存关联
Set<Long> userIds = orders.stream().map(Order::getUserId).collect(Collectors.toSet());
Map<Long, User> userMap = userMapper.selectBatchIds(userIds).stream()
    .collect(Collectors.toMap(User::getId, Function.identity()));
```

## 红线 2：禁止 OFFSET 深分页

```java
// 禁止：深分页时性能极差
SELECT * FROM users LIMIT 10 OFFSET 100000;

// 正确：游标分页
SELECT * FROM users WHERE id > #{lastId} ORDER BY id ASC LIMIT 10;
```

## 缓存策略

- 所有缓存必须设置过期时间
- 必须考虑缓存穿透/击穿/雪崩防护：
  - 穿透：布隆过滤器或缓存空值
  - 击穿：互斥锁或永不过期 + 异步刷新
  - 雪崩：过期时间加随机偏移

## 查询优化

- 禁止 `SELECT *`，列出需要的字段
- 列表接口必须有分页
- 统计类查询优先走异步或定时任务预计算
- 数据库慢查询阈值设为 200ms，超时必须优化

## 连接池配置

- 最小空闲连接：10
- 最大连接数：按 QPS 和 RT 计算，默认 50
- 连接超时：1s
- 空闲连接回收：5 分钟

## 异步处理

- 非实时业务逻辑使用 `@Async` 异步处理
- 必须配置线程池（核心线程、最大线程、队列容量、拒绝策略）
- 异步方法不能和调用方在同一类中
