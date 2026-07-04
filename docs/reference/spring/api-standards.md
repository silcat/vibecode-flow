# Spring Boot API 设计规范

## 用途

编写 Controller、设计 RESTful 接口、参数校验、分页接口和接口文档时的强制性规范。

## URL 设计

- 资源名使用复数名词，层级不超过 2 层
- 动作用 HTTP 方法表达，不在 URL 中出现动词
- 版本前缀统一：`/api/v1/`

```
GET    /api/v1/users/list          # 分页查询
GET    /api/v1/users/detail?id=1   # 查询单个
POST   /api/v1/users/save          # 创建
POST   /api/v1/users/update        # 更新
DELETE /api/v1/users/1,2,3         # 删除（支持批量）
```

## 统一响应格式

所有接口必须返回 `ApiResponse<T>` 结构：

```java
{
    "code": 200,
    "message": "操作成功",
    "data": { ... },
    "timestamp": 1711180800000
}
```

## Controller 规范

- Controller 只接收参数、调用 BizManageService、返回响应，**不写业务逻辑**
- 参数校验注解必须带 `message` 属性
- 不使用 `@Autowired` 或 `@RequiredArgsConstructor`，统一使用 `@Resource`

## DTO/VO 规范

- 入参使用 DTO，出参使用 VO
- DTO 必须带校验注解（`@NotNull`、`@NotBlank` 等）
- VO 只包含接口需要返回的字段，不返回 Entity 全部字段

## 分页策略

- 分页入参统一使用 `PageDTO`（含 `page`、`size`、`sort`）
- 分页出参统一使用 `PageVO<T>`（含 `records`、`total`、`page`、`size`）
- 禁止 OFFSET 深分页，超过 1000 页时必须改为游标分页

