# 分布式系统任务调度解决方案 clever-quartz

clever-quartz分布式系统任务调度解决方案，主要解决分布式系统定时任务触发管理，基于SpringBoot开发整合quartz，仅依赖mysql和clever-notification(本人开发的分布式消息通知服务)。支持集群部署，任务告警通知。使用Http Api或Dubbo Api调用第三方业务系统，业务系统无须任何依赖(只需暴露对应的接口)，即可使用分布式任务调度。

### 计划支持功能

- [x] 任务调度管理
    - [x] 支持集群部署达到高可用
    - [x] 定时任务支持线上动态修改
    - [x] 记录调度日志，支持查询调度日志
    - [x] 支持Http任务调度
    - [ ] 支持Dubbo任务调度
    - [ ] 支持任务告警通知
    - [ ] 支持线上任务调度器变化(新增调度任务、暂停调度器...等等)发送告警通知
    - [ ] 完善的管理后台

![API 截图](https://raw.githubusercontent.com/Lzw2016/clever-quartz/master/api.png)