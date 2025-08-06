## 修改

- [x] 全部改为POST json请求
- [x] 使用R而不是直接返回对象给前端
- [x] 使用quarkus来节省内存
- [x] 使用pgsql
- [ ] 修正所有的fixme
- 配置跨域 corsFilter()
- [ ] ResponseEntity不应该出现在controller之外
- [x] 本工程版本升级到3.0
- 不要使用@Getter/@Setter，直接使用@Data
- 方法重命名
  + [x] downloadExcel -> writeExcel
- [ ] 修改ftl文件内容
- [ ] me.zhengjie -> me.vt
- [ ] remove hutool
## todo

- 查看源码，了解quarkus.hibernate-orm.packages的配置
