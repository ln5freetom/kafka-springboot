---
name: java-master
description: Java 全能助手，适配 Claude Code，涵盖代码审查、SpringBoot接口生成、全局异常、MyBatis规范、并发安全、工具类生成、单元测试、微服务开发，一键响应所有Java开发需求
invocable_by: user
invocable_by: assistant
user_command: /java
auto_trigger:
  - 写Java代码
  - 代码审查
  - 生成SpringBoot接口
  - Java代码优化
  - MyBatis优化
  - 并发问题解决
  - 生成单元测试
  - 微服务开发
---

# Java 开发全能规则（Claude Code 专属适配）

## 1. 代码规范与审查（核心）

- 命名规范：类名大驼峰（名词），方法/变量小驼峰（动词+名词），常量全大写+下划线，禁止拼音、单字符变量（如a、b、c）
- 结构规范：Controller 仅处理参数校验、请求接收和结果返回，不写任何业务逻辑；Service 遵循单一职责，拆分细化；Mapper/DAO 只负责数据交互，禁止写业务逻辑
- 代码细节：方法行数控制在50行以内，if-else/for循环嵌套不超过3层；禁止重复代码，抽取公共方法/工具类复用；禁止使用魔法值，所有固定值定义为常量
- 异常与日志：禁止空catch块、禁止捕获Throwable/Exception顶级异常；异常必须打印完整堆栈（log.error("异常描述", e)）；禁止在循环中打印大量日志，避免日志泛滥；敏感数据（手机号、身份证）必须脱敏，禁止日志打印明文

## 2. SpringBoot 接口开发（一键生成）

调用 /java 后，输入“生成XX接口”，自动输出标准三层架构：

- 数据层：Entity（实体类）、DTO（入参）、VO（出参），区分清晰，不混用
- 控制层：RESTful 风格 Controller，使用 @Valid 做参数校验，统一返回 Result<T> 格式
- 业务层：Service 接口+实现类，事务注解（@Transactional）加在 Service 层，精准控制事务范围
- 数据访问层：Mapper 接口+XML（MyBatis）/ 接口方法（MyBatis-Plus），遵循MyBatis规范
- 附加：自动生成全局异常处理器，统一捕获参数校验、业务、系统异常

## 3. 全局异常与统一返回（直接生成可用代码）

生成结构（复制即可集成到项目）：

1. 统一返回类 Result<T>：包含 code（响应码）、msg（响应信息）、data（响应数据）
2. 响应码枚举 ResultCode：定义 SUCCESS(200,"成功")、FAIL(500,"失败")、PARAM_ERROR(400,"参数错误") 等常用枚举
3. 自定义业务异常 BusinessException：继承 Exception，接收 ResultCode 和自定义消息
4. 全局异常处理器 GlobalExceptionHandler：使用 @RestControllerAdvice，捕获参数校验异常（MethodArgumentNotValidException）、业务异常、系统异常、404/500 异常，统一返回 Result 格式

## 4. MyBatis / MyBatis-Plus 规范（防坑+优化）

- 安全规范：一律使用 #{} 占位符，严禁 ${} 直接拼接 SQL，防止 SQL 注入
- 性能规范：禁止 select \*，必须明确查询字段；禁止循环查库（N+1 查询），使用关联查询或批量查询；大表必须分页（MyBatis-Plus IPage / PageHelper）、添加索引
- 开发规范：批量操作优先使用 batch 方法（如 insertBatch），禁止循环插入/更新；逻辑删除、乐观锁优先配置使用；XML 映射文件中禁止写复杂业务逻辑，仅做简单数据查询/操作

## 5. 并发与线程池（安全+高效）

- 线程池规范：禁用 Executors 工具类创建线程池，手动创建 ThreadPoolExecutor，合理配置核心参数（corePoolSize、maximumPoolSize、阻塞队列、拒绝策略）
- 并发安全：多线程环境下使用线程安全集合（ConcurrentHashMap、CopyOnWriteArrayList）；共享变量使用 volatile 修饰或 Lock/synchronized 加锁，锁粒度最小化
- 异步开发：使用 @Async 注解实现异步，必须指定自定义线程池，避免使用默认线程池；ThreadLocal 存储数据后，务必在 finally 中调用 remove()，防止内存泄漏

## 6. 常用工具类（一键生成代码）

调用 /java 输入“生成XX工具类”，可直接生成以下常用工具（适配 JDK8+）：

- 日期工具：基于 LocalDateTime，实现日期格式化、日期加减、日期对比、字符串与日期转换
- 脱敏工具：实现手机号（138\***\*1234）、身份证（110\*\***1234）、邮箱（123\*\*\*\*@qq.com）脱敏
- 加密工具：MD5、SHA、BCrypt（密码加密）、AES 对称加密
- HTTP 工具：基于 RestTemplate / WebClient，实现 GET/POST/PUT/DELETE 请求，统一处理响应
- Excel 工具：基于 EasyExcel，实现 Excel 导入、导出，支持复杂表头、数据校验
- 通用工具：集合判空、Bean 拷贝（使用 Spring BeanUtils，禁止 Apache BeanUtils）、字符串处理

## 7. 单元测试（JUnit 5 + Mockito）

生成规范（直接复制到测试类）：

- 测试范围：优先测试 Service 层，不测试 Controller 层（接口测试用 Postman）
- 依赖 Mock：使用 @MockBean Mock Mapper 接口、外部服务依赖，不依赖真实数据库、Redis
- 测试场景：覆盖正常场景、异常场景（如参数错误、业务异常）、边界场景（如空参数、最大值/最小值）
- 断言规范：使用 AssertJ 断言（如 assertThat(result).isNotNull()），断言清晰易懂

## 8. Spring Cloud 微服务规范（适配微服务开发）

- Feign 调用：必须配置超时时间、重试策略、异常处理，避免调用超时导致服务雪崩；接口参数校验，统一异常返回
- 网关配置：统一网关（Gateway）做鉴权、限流、日志记录、请求转发，所有请求走网关入口
- 链路追踪：实现 traceId 全链路传递，集成 Sleuth + Zipkin，方便问题定位
- 事务处理：避免分布式事务，优先使用最终一致性方案（如消息队列）；禁止大事务，拆分事务范围
- 服务管理：使用 Nacos 做服务发现和配置中心，统一管理服务配置；服务拆分遵循单一职责，避免巨型服务，防止循环依赖

## 9. 性能优化与问题排查（快速定位问题）

- 常见性能问题：循环查库、N+1 查询、未加索引导致慢 SQL；大对象、频繁创建对象导致内存溢出（OOM）；锁粒度太大导致并发阻塞；日志过多导致 IO 瓶颈
- 排查方向：慢接口定位（通过链路追踪）、慢 SQL 优化（执行计划分析）、GC 问题（jstat 命令）、线程池阻塞（jstack 命令）、连接池耗尽（检查连接池配置）
