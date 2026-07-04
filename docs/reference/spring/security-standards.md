# Spring Boot 安全开发规范

## 用途

认证鉴权、用户输入处理、SQL 查询、敏感数据处理、文件上传、CORS 配置时的强制性规范。

## SQL 注入防护

- MyBatis 参数全部使用 `#{}`，禁止 `${}` 字符串拼接
- 动态排序/表名场景：必须用白名单校验后再使用 `${}`
- 优先使用 MyBatis Plus 的 `LambdaQueryWrapper` 避免手写 SQL

## XSS 防护

- 所有用户输入在存储前做 HTML 转义
- 富文本场景：使用白名单过滤（允许的安全标签）
- 后端接口返回的 JSON 中不直接嵌入用户输入的 HTML

## 密码安全

```java
// 正确：BCrypt（自带盐值）
user.setPassword(passwordEncoder.encode(rawPassword));

// 禁止：明文、MD5、SHA-1
user.setPassword(rawPassword);
user.setPassword(DigestUtils.md5Hex(rawPassword));
```

## JWT / API Key 安全

- JWT 过期时间不超过 2 小时
- API Key 存储在环境变量或配置中心，不硬编码
- token 通过 Authorization Header 传递，不放在 URL 参数中

## 敏感数据脱敏

- 手机号：`138****1234`
- 身份证：`3201**********1234`
- 邮箱：`u***@example.com`
- 脱敏在序列化层统一处理（Jackson 自定义序列化器）

## 输入校验

```java
@NotBlank(message = "用户名不能为空")
@Size(min = 3, max = 20, message = "用户名长度为3-20个字符")
private String username;

@Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
private String phone;
```

## 文件上传

- 限制文件类型（白名单）
- 限制文件大小（默认 10MB）
- 重命名文件（UUID），不保留原始文件名
- 上传目录不放在 Web 根目录下

## CORS

- 明确指定允许的域名，不使用 `*`
- 明确指定允许的 HTTP 方法和 Header
