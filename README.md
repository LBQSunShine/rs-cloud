### 资源分享平台
#### 开发模式、技术选型
* 前后端分离模式开发
* 后端开发框架采用Spring Boot、Spring Cloud、Spring Cloud Alibaba、Mybatis-Plus
* 数据库选择Mysql
* 注册中心、配置中心选型Nacos
* 网关权限认证使用Spring Cloud Gateway、Redis
* 数据缓存使用Redis、分布式锁使用Redisson
* 分布式事务选型Seata
* 流量控制框架选型Sentinel
* 微服务统一日志ELK
#### 系统模块
```java
rs-cloud
|——rs-authorization     认证中心
|——rs-common            公共服务
|——rs-gateway           网关模块
|——rs-modules           业务模块
        |——rs-file-root     文件模块
                |——rs-file          文件服务
                |——rs-file-client   文件服务api
        |——rs-job           任务调度
        |——rs-system-root   系统模块
                |——rs-system        系统服务
                |——rs-system-client 系统服务api
```
