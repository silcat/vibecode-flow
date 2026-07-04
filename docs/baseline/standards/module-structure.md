# Maven 多模块结构规范

## 用途

新增业务模块时，AI 代理按此骨架批量生成目录结构和 POM 文件。

## 触发场景

- 新增微服务业务模块
- 新增对应 API 定义模块

## 目录骨架（固定模板）

```
根项目/
├── pom.xml                                    # 根 POM（dependencyManagement + pluginManagement）
│
├── <项目名>-service/                          # packaging=pom（微服务集合）
│   ├── pom.xml                                # 公共依赖 + <modules> 声明
│   │
│   └── <项目名>-<业务>/                        # packaging=jar ─ 一个业务一个模块
│       ├── pom.xml
│       ├── Dockerfile
│       └── src/main/
│           ├── java/org/ytf/<pkg>/
│           │   ├── config/                    # [必]
│           │   ├── controller/                # [必]
│           │   ├── bo/                        # [必]
│           │   ├── repository/                # [必]
│           │   │   └── impl/                  # [必]
│           │   ├── mapper/                    # [必]
│           │   ├── entity/                    # [必]
│           │   ├── enums/                     # [必]
│           │   ├── utils/                     # [必]
│           │   ├── converter/                 # [必]
│           │   ├── domain/                    # [选]
│           │   ├── gateway/                   # [选]
│           │   │   ├── feign/                 # [选]
│           │   │   └── sdk/                   # [选]
│           │   ├── exception/                 # [选]
│           │   ├── properties/                # [选]
│           │   └── consumer/                  # [选] consumer 类模块
│           └── resources/
│               └── mapper/                    # [选]
│
└── <项目名>-service-api/                      # packaging=pom（API 集合）
    ├── pom.xml                                # 公共 API 依赖 + <modules> 声明
    │
    └── <项目名>-<业务>-api/                    # packaging=jar ─ 对应微服务的 API 定义
        ├── pom.xml
        └── src/main/java/org/ytf/<pkg>/
            ├── enums/                         # [必]
            ├── feign/                         # [必]
            ├── respond/                       # [必]
            ├── request/                       # [必]
            ├── constant/                      # [必]
            ├── utils/                         # [必]
            └── exception/                     # [选]
```

## 包路径映射规则

| 业务类型   | 示例业务名        | 包路径 (org.ytf.*) |
|-----------|------------------|------------------------|
| 独立业务   | device / i18n    | `<name>`               |
| consumer 类 | device-consumer  | `<name>.consumer`      |

## 必需 vs 可选目录

| 位置          | [必] 目录                                              | [选] 目录                     |
|---------------|-------------------------------------------------------|------------------------------|
| service 模块   | config, controller, bo, repository, mapper, entity, enums, utils, converter | domain, gateway, exception, properties, consumer |
| service 资源   | —                                                     | mapper                       |
| api 模块       | enums, feign, respond, request, constant, utils       | exception                    |

> `repository/impl/` 为必建子目录。`gateway/` 下 `feign/`、`sdk/` 按需创建。

## 实施规则

1. 先生成所有目录（`[必]` 目录全部创建，`[选]` 目录按需创建）
2. 再生成 POM 文件（根 → service 聚合 → api 聚合 → 各子模块）
3. POM 版本号从根 POM 的 `<properties>` 统一管理
4. 每完成一个模块做一次 `mvn compile` 验证
