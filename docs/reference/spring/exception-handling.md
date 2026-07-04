# Spring Boot 异常处理规范

## 用途

异常处理、错误码定义、自定义异常类、全局异常处理器设计时的强制性规范。

## 异常分类

- `BusinessException`：业务规则违反（如余额不足、状态不允许）
- `ResourceNotFoundException`：资源不存在
- `ValidationException`：参数校验失败
- 其他未预期异常由全局异常处理器兜底

## 错误码设计

- 错误码格式：模块前缀（3位）+ 具体错误（3位）
- 示例：`USR_001`（用户模块）、`ORD_002`（订单模块）
- 每个错误码对应唯一的错误消息
- 错误码定义在枚举中集中管理

## 全局异常处理器

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        log.error("未预期异常", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("SYS_001", "系统内部错误"));
    }
}
```

## Service 层策略

- Service 层主动抛业务异常，不吞异常返回 null 或空对象
- 事务回滚：`@Transactional(rollbackFor = Exception.class)`
- 异常信息必须包含足够上下文（如资源 ID、操作类型）

## 禁止事项

- 禁止 catch 后只打日志不处理（空 catch 块）
- 禁止返回 `null` 表示"未找到"，必须抛异常
- 禁止向客户端暴露堆栈信息
