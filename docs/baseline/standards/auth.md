# 认证规范

> 触发场景：JWT 签发/校验、Gateway 过滤器、服务间调用鉴权

## JWT 签发

- 算法：HS256，密钥来自环境变量 `JWT_SECRET`
- 内容：`{ sub: userId, role: "USER", exp: now+2h }`
- Access Token：2 小时过期
- Refresh Token：7 天过期，存 Redis，一次使用后失效

## JWT 校验

- Gateway 全局过滤器拦截所有 `/api/**` 路径
- 排除：`/api/users/register`、`/api/users/login`
- 无 token → 401
- 过期 token → 401 + `{ code: 401, message: "token expired" }`
- 非法 token → 401

## 服务间调用

- Feign 请求头携带 `X-Internal-Token`
- 被调服务校验 internal token
- 不经过 Gateway 认证过滤器

## 密码规则

- 长度 ≥ 8 位，含字母和数字
- bcrypt 加密，cost factor = 10
- 禁止响应中返回 password 字段
- 禁止日志中打印密码原文或哈希
