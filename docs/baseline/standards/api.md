# API 设计规范

> 触发场景：新增/修改 Controller、设计接口、参数校验、分页查询

## URL 规则

- 前缀：`/api/{resource}`
- 不带动词：`POST /api/users` ✓  `POST /api/createUser` ✗
- 资源复数：`/api/users/{id}` ✓  `/api/user/{id}` ✗
- 嵌套不超过两层：`/api/users/{id}/pets` ✓  `/api/users/{id}/pets/{pid}/photos` ✗

## 统一响应

```json
{ "code": 200, "message": "success", "data": {} }
```

- 成功：`code=200`，`data` 含业务数据
- 客户端错误：`code=4xx`，`data=null`
- 服务端错误：`code=5xx`，`data=null`

## HTTP 状态码

| 场景 | 码 |
|------|-----|
| 创建成功 | 201 |
| 查询/更新成功 | 200 |
| 删除成功 | 204 |
| 参数校验失败 | 400 |
| 未认证 | 401 |
| 无权限 | 403 |
| 资源不存在 | 404 |
| 业务冲突 | 409 |
| 服务端异常 | 500 |

## 分页

- 请求：`GET /api/users?page=1&size=20&sort=createdAt,desc`
- 响应：`{ "content": [...], "page": 1, "size": 20, "totalElements": 156, "totalPages": 8 }`

## 参数校验

- 必填用 `@NotNull` / `@NotBlank`
- 格式用 `@Email`、`@Pattern`
- 业务校验在 Service 层抛 `BusinessException`
- 校验失败统一返回 400 + 字段级错误
