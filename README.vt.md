## 修改

- [x] 全部改为POST json请求
- [x] 使用R而不是直接返回对象给前端
- [x] 使用quarkus来节省内存
  - [x] 小内存使用-XX:+UseSerialGC
- [x] 使用pgsql
- [ ] 修正所有的fixme
- [x] 配置跨域 见 quarkus.http.cors.origins in application.yml
- [ ] ResponseEntity不应该出现在controller之外
- [x] 本工程版本升级到3.0
- 不要使用@Getter/@Setter，直接使用@Data
- 方法重命名
  + [x] downloadExcel -> writeExcel
  + [ ] writeExcel -> generateExcelFile
- [ ] 修改ftl文件内容
- [x] me.vt -> me.vt
- [ ] remove hutool
- [ ] 实现@PreAuthorize


## 新功能

- [ ] 把stock的功能移到eladmin中
-
## todo

- 查看源码，了解quarkus.hibernate-orm.packages的配置

## archunit

这些规则可以组合起来，形成一个 多模块项目的 ArchUnit 检查体系：

分层依赖：Controller -> Service -> Repository

命名规范：Service、Controller、Repository、DTO、Entity

注解约束：保证 Spring / JPA 注解一致

类型使用限制：Multipart、Servlet、DTO、Entity

继承约束：Controller 必须继承 BaseController

包访问控制：internal 包只允许内部访问

见模块 eladmin-system中的DependencyRulesTest等类。

可尝试问gpt有哪些常见的规则。
