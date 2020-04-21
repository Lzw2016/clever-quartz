create database if not exists `clever-quartz` default character set = utf8;
use `clever-quartz`;

/*

In your Quartz properties file, you'll need to set
org.quartz.jobStore.driverDelegateClass = org.quartz.impl.jdbcjobstore.StdJDBCDelegate

By: Ron Cordell - roncordell
  I didn't see this anywhere, so I thought I'd post it here. This is the script from Quartz to create the tables in a MySQL database, modified to use INNODB instead of MYISAM.

*/

drop table if exists qrtz_fired_triggers;
drop table if exists qrtz_paused_trigger_grps;
drop table if exists qrtz_scheduler_state;
drop table if exists qrtz_locks;
drop table if exists qrtz_simple_triggers;
drop table if exists qrtz_simprop_triggers;
drop table if exists qrtz_cron_triggers;
drop table if exists qrtz_blob_triggers;
drop table if exists qrtz_triggers;
drop table if exists qrtz_job_details;
drop table if exists qrtz_calendars;

/* ====================================================================================================================
    qrtz_job_details -- 存储每一个已配置的Job的详细信息

    JobDetail   他是实现轮询的一个的回调类，可将参数封装成JobDataMap对象,Quartz将任务的作业状态保存在JobDetail中.
    JobDataMap  JobDataMap用来报错由JobDetail传递过来的任务实例对象
==================================================================================================================== */
create table qrtz_job_details
(
  sched_name          varchar(120)    not null    comment 'Scheduler名称',
  job_name            varchar(200)    not null    comment 'Job key',
  job_group           varchar(200)    not null    comment 'Job group 名称',
  description         varchar(250)    null        comment 'Job描述， .withDescription()方法传入的string',
  job_class_name      varchar(250)    not null    comment '实现Job的类名，trigger触发时调度此类的execute方法',
  is_durable          varchar(1)      not null    comment '为true时，Job相关的trigger完成以后，Job数据继续保留',
  is_nonconcurrent    varchar(1)      not null    comment '是否不允许并发，为true时，如果下一次的触发事件到了，而上一次的job执行还未结束，则后续的触发会放入队列等待',
  is_update_data      varchar(1)      not null    comment '是否在多次调度之间更新JobDataMap',
  requests_recovery   varchar(1)      not null    comment 'Scheduler实例发生故障时，故障恢复节点会检测故障的Scheduler正在调度的任务是否需要recovery(复苏)，如果需要会添加一个只执行一次的simple trigger重新触发',
  job_data            blob            null        comment '存储JobDataMap等',
  primary key (sched_name,job_name,job_group)
) engine=innodb comment '存储每一个已配置的Job的详细信息';


/* ====================================================================================================================
    qrtz_triggers -- 存储已配置的Trigger的信息

    SimpleTrigger               <普通的Trigger>                 SimpleScheduleBuilder
    CronTrigger                 <带Cron Like 表达式的Trigger>    CronScheduleBuilder
    CalendarIntervalTrigger     <带日期触发的Trigger>            CalendarIntervalScheduleBuilder
    DailyTimeIntervalTrigger    <按天触发的Trigger>              DailyTimeIntervalScheduleBuilder
==================================================================================================================== */
create table qrtz_triggers
(
  sched_name      varchar(120)        not null    comment 'Scheduler名称',
  trigger_name    varchar(200)        not null    comment 'Trigger key',
  trigger_group   varchar(200)        not null    comment 'Trigger group名称',
  job_name        varchar(200)        not null    comment 'Job key',
  job_group       varchar(200)        not null    comment 'Job group 名称',
  description     varchar(250)        null        comment 'Trigger 描述， .withDescription()方法传入的string',
  next_fire_time  bigint(13)          null        comment '下一次触发时间',
  prev_fire_time  bigint(13)          null        comment '上一次触发时间，默认-1',
  priority        integer             null        comment 'Trigger 优先级，默认5',
  trigger_state   varchar(16)         not null    comment 'Trigger状态，PAUSED_BLOCKED:停止_阻塞; PAUSED:停止; WAITING:等待执行; ACQUIRED:已获得; EXECUTING:执行中; COMPLETE:完成; BLOCKED:阻塞; ERROR:错误; DELETED:已删除',
  trigger_type    varchar(8)          not null    comment 'Cron 或 Simple',
  start_time      bigint(13)          not null    comment 'Trigger开始时间',
  end_time        bigint(13)          null        comment 'Trigger结束时间',
  calendar_name   varchar(200)        null        comment 'Trigger关联的Calendar name',
  misfire_instr   smallint(2)         null        comment 'misfire规则id',
  job_data        blob                null        comment '存储Trigger的JobDataMap等',
  primary key (sched_name,trigger_name,trigger_group),
  foreign key (sched_name,job_name,job_group)
  references qrtz_job_details(sched_name,job_name,job_group)
)engine=innodb comment '存储已配置的Trigger的信息';


/* ====================================================================================================================
    qrtz_simple_triggers -- 存储简单的Trigger，包括重复次数，间隔，以及已触的次数
==================================================================================================================== */
create table qrtz_simple_triggers
(
  sched_name          varchar(120)    not null    comment 'Scheduler名称',
  trigger_name        varchar(200)    not null    comment 'Trigger key',
  trigger_group       varchar(200)    not null    comment 'Trigger group 名称',
  repeat_count        bigint(7)       not null    comment '需要重复次数',
  repeat_interval     bigint(12)      not null    comment '重复间隔',
  times_triggered     bigint(10)      not null    comment '已经触发次数',
  primary key (sched_name,trigger_name,trigger_group),
  foreign key (sched_name,trigger_name,trigger_group)
  references qrtz_triggers(sched_name,trigger_name,trigger_group)
)engine=innodb comment '存储简单的Trigger，包括重复次数、间隔、以及已触的次数';


/* ====================================================================================================================
    qrtz_cron_triggers -- 存储Cron Trigger，包括Cron表达式和时区信息
==================================================================================================================== */
create table qrtz_cron_triggers
(
  sched_name          varchar(120)    not null    comment 'Scheduler名称',
  trigger_name        varchar(200)    not null    comment 'Trigger key',
  trigger_group       varchar(200)    not null    comment 'Trigger group 名称',
  cron_expression     varchar(120)    not null    comment '调度规则',
  time_zone_id        varchar(80)                 comment '时区',
  primary key (sched_name,trigger_name,trigger_group),
  foreign key (sched_name,trigger_name,trigger_group)
  references qrtz_triggers(sched_name,trigger_name,trigger_group)
)engine=innodb comment '存储CronTrigger，包括Cron表达式和时区信息';


/* ====================================================================================================================
    qrtz_simprop_triggers -- 存储trigger属性信息
==================================================================================================================== */
create table qrtz_simprop_triggers
(
  sched_name      varchar(120)        not null    comment 'Scheduler名称',
  trigger_name    varchar(200)        not null    comment 'Trigger key',
  trigger_group   varchar(200)        not null    comment 'Trigger group 名称',
  str_prop_1      varchar(512)        null        comment '字符串属性，占位用',
  str_prop_2      varchar(512)        null        comment '字符串属性，占位用',
  str_prop_3      varchar(512)        null        comment '字符串属性，占位用',
  int_prop_1      int                 null        comment '字符串属性，占位用',
  int_prop_2      int                 null        comment '字符串属性，占位用',
  long_prop_1     bigint              null        comment '字符串属性，占位用',
  long_prop_2     bigint              null        comment '字符串属性，占位用',
  dec_prop_1      numeric(13,4)       null        comment '字符串属性，占位用',
  dec_prop_2      numeric(13,4)       null        comment '字符串属性，占位用',
  bool_prop_1     varchar(1)          null        comment '字符串属性，占位用',
  bool_prop_2     varchar(1)          null        comment '字符串属性，占位用',
  primary key (sched_name,trigger_name,trigger_group),
  foreign key (sched_name,trigger_name,trigger_group)
  references qrtz_triggers(sched_name,trigger_name,trigger_group)
)engine=innodb comment '存储trigger属性信息';


/* ====================================================================================================================
    qrtz_blob_triggers -- 存储用户自定义的Trigger
==================================================================================================================== */
create table qrtz_blob_triggers
(
  sched_name          varchar(120)    not null    comment 'Scheduler名称',
  trigger_name        varchar(200)    not null    comment 'Trigger key',
  trigger_group       varchar(200)    not null    comment 'Trigger group 名称',
  blob_data           blob            null        comment '对于用户自定义的Trigger信息，无法提前设计字段，所以序列化后使用BLOB存储',
  primary key (sched_name,trigger_name,trigger_group),
  index (sched_name,trigger_name, trigger_group),
  foreign key (sched_name,trigger_name,trigger_group)
  references qrtz_triggers(sched_name,trigger_name,trigger_group)
)engine=innodb comment '存储用户自定义的Trigger';


/* ====================================================================================================================
    qrtz_calendars -- 以Blob类型存储Quartz的Calendar信息
==================================================================================================================== */
create table qrtz_calendars
(
  sched_name      varchar(120)        not null    comment 'scheduler名称',
  calendar_name   varchar(200)        not null    comment 'calendar 名称',
  calendar        blob                not null    comment 'Calendar 数据',
  primary key (sched_name,calendar_name)
)engine=innodb comment '存储Quartz的Calendar信息';


/* ====================================================================================================================
    qrtz_paused_trigger_grps -- 存储已暂停的 Trigger 组的信息
==================================================================================================================== */
create table qrtz_paused_trigger_grps
(
  sched_name      varchar(120)        not null    comment 'scheduler名称',
  trigger_group   varchar(200)        not null    comment 'Trigger group 名称',
  primary key (sched_name,trigger_group)
)engine=innodb comment '存储已暂停的Trigger组的信息';


/* ====================================================================================================================
    qrtz_fired_triggers -- 存储与已触发的Trigger相关的状态信息，以及相联Job的执行信息
==================================================================================================================== */
create table qrtz_fired_triggers
(
  sched_name          varchar(120)        not null    comment 'Scheduler名称',
  entry_id            varchar(95)         not null    comment '条目号',
  trigger_name        varchar(200)        not null    comment 'Trigger key',
  trigger_group       varchar(200)        not null    comment 'Trigger group 名称',
  instance_name       varchar(200)        not null    comment 'Scheduler实例的唯一标识（应该是完成这次调度的Scheduler标识，待多实例环境测试验证）',
  fired_time          bigint(13)          not null    comment '触发时间',
  sched_time          bigint(13)          not null    comment '（疑似下一次触发时间，待验证）',
  priority            integer             not null    comment 'Trigger 优先级',
  state               varchar(16)         not null    comment 'Trigger状态',
  job_name            varchar(200)        null        comment 'Job key',
  job_group           varchar(200)        null        comment 'Job group 名称',
  is_nonconcurrent    varchar(1)          null        comment '是否不允许并发',
  requests_recovery   varchar(1)          null        comment 'Scheduler实例发生故障时，故障恢复节点会检测故障的Scheduler正在调度的任务是否需要recovery，如果需要会添加一个只执行一次的simple trigger重新触发',
  primary key (sched_name,entry_id)
)engine=innodb comment '存储与已触发的Trigger相关的状态信息，以及相联Job的执行信息';


/* ====================================================================================================================
    QRTZ_SCHEDULER_STATE -- 存储少量的有关Scheduler的状态信息，和别的Scheduler实例(假如是用于一个集群中)
==================================================================================================================== */
create table qrtz_scheduler_state
(
  sched_name          varchar(120)    not null    comment 'Scheduler名称',
  instance_name       varchar(200)    not null    comment 'Scheduler实例的唯一标识，配置文件中的Instance Id',
  last_checkin_time   bigint(13)      not null    comment '最后检入时间',
  checkin_interval    bigint(13)      not null    comment 'Scheduler 实例检入到数据库中的频率，单位毫秒',
  primary key (sched_name,instance_name)
)engine=innodb comment '存储少量的有关Scheduler的状态信息，和别的Scheduler实例';


/* ====================================================================================================================
    qrtz_locks -- 存储程序的悲观锁的信息(假如使用了悲观锁)
==================================================================================================================== */
create table qrtz_locks
(
  sched_name          varchar(120)    not null    comment 'scheduler名称，同一集群下的scheduler实例名称相同，instance_id不同',
  lock_name           varchar(40)     not null    comment '锁名称，TRIGGER_ACCESS，STATE_ACCESS，JOB_ACCESS，CALENDAR_ACCESS，MISFIRE_ACCESS',
  primary key (sched_name,lock_name)
)engine=innodb comment '存储锁以及获得锁的调度器名称。获取的锁不存在时创建，获得锁的调度器可以操作相应数据';




create index idx_qrtz_j_req_recovery on qrtz_job_details(sched_name,requests_recovery);
create index idx_qrtz_j_grp on qrtz_job_details(sched_name,job_group);

create index idx_qrtz_t_j on qrtz_triggers(sched_name,job_name,job_group);
create index idx_qrtz_t_jg on qrtz_triggers(sched_name,job_group);
create index idx_qrtz_t_c on qrtz_triggers(sched_name,calendar_name);
create index idx_qrtz_t_g on qrtz_triggers(sched_name,trigger_group);
create index idx_qrtz_t_state on qrtz_triggers(sched_name,trigger_state);
create index idx_qrtz_t_n_state on qrtz_triggers(sched_name,trigger_name,trigger_group,trigger_state);
create index idx_qrtz_t_n_g_state on qrtz_triggers(sched_name,trigger_group,trigger_state);
create index idx_qrtz_t_next_fire_time on qrtz_triggers(sched_name,next_fire_time);
create index idx_qrtz_t_nft_st on qrtz_triggers(sched_name,trigger_state,next_fire_time);
create index idx_qrtz_t_nft_misfire on qrtz_triggers(sched_name,misfire_instr,next_fire_time);
create index idx_qrtz_t_nft_st_misfire on qrtz_triggers(sched_name,misfire_instr,next_fire_time,trigger_state);
create index idx_qrtz_t_nft_st_misfire_grp on qrtz_triggers(sched_name,misfire_instr,next_fire_time,trigger_group,trigger_state);

create index idx_qrtz_ft_trig_inst_name on qrtz_fired_triggers(sched_name,instance_name);
create index idx_qrtz_ft_inst_job_req_rcvry on qrtz_fired_triggers(sched_name,instance_name,requests_recovery);
create index idx_qrtz_ft_j_g on qrtz_fired_triggers(sched_name,job_name,job_group);
create index idx_qrtz_ft_jg on qrtz_fired_triggers(sched_name,job_group);
create index idx_qrtz_ft_t_g on qrtz_fired_triggers(sched_name,trigger_name,trigger_group);
create index idx_qrtz_ft_tg on qrtz_fired_triggers(sched_name,trigger_group);





/* ====================================================================================================================
    qrtz_job_log -- Job执行日志表
==================================================================================================================== */
CREATE TABLE qrtz_job_log
(
  id                      bigint          NOT NULL    auto_increment      COMMENT '编号',
  listener_name           varchar(120)    NOT NULL                        COMMENT '监听器名称',
  sched_name              varchar(120)    NOT NULL                        COMMENT 'Scheduler名称',
  instance_name           varchar(200)    NOT NULL                        COMMENT 'Scheduler实例的唯一标识，配置文件中的Instance Id',
  job_name                varchar(200)    NOT NULL                        COMMENT 'Job key',
  job_group               varchar(200)    NOT NULL                        COMMENT 'Job group 名称',
  job_class_name          varchar(250)    NOT NULL                        COMMENT 'Job类名称',
  trigger_name            varchar(200)    NOT NULL                        COMMENT 'Trigger key',
  trigger_group           varchar(200)    NOT NULL                        COMMENT 'Trigger group名称',
  start_time              datetime        NOT NULL                        COMMENT '开始执行时间',
  end_time                datetime                                        COMMENT '执行结束时间',
  process_time            bigint                                          COMMENT '执行用时(ms)',
  pre_run_time            datetime                                        COMMENT '上一次执行时间',
  next_run_time           datetime                                        COMMENT '下一次执行时间',
  run_count               int             NOT NULL                        COMMENT '执行次数',
  ip_address              varchar(200)    NOT NULL                        COMMENT '执行节点IP,可能有多个(‘;’分隔)',
  status                  char(1)         NOT NULL                        COMMENT '任务执行状态(0:成功;1:失败)',
  exception_info          mediumtext                                      COMMENT '异常信息',
  is_veto                 char(1)         NOT NULL                        COMMENT '是否被否决（0：否；1：是）',
  before_job_data         mediumtext                                      COMMENT '执行前的JobDataMap数据',
  after_job_data          mediumtext                                      COMMENT '执行后的JobDataMap数据',
  PRIMARY KEY (id)
) COMMENT = 'Job执行日志表';



/* ====================================================================================================================
    qrtz_trigger_log -- Trigger触发日志表
==================================================================================================================== */
CREATE TABLE qrtz_trigger_log
(
  id                          bigint          NOT NULL    auto_increment      COMMENT '编号',
  listener_name               varchar(120)    NOT NULL                        COMMENT '监听器名称',
  sched_name                  varchar(120)    NOT NULL                        COMMENT 'Scheduler名称',
  instance_name               varchar(200)    NOT NULL                        COMMENT 'Scheduler实例的唯一标识，配置文件中的Instance Id',
  job_name                    varchar(200)    NOT NULL                        COMMENT 'Job key',
  job_group                   varchar(200)    NOT NULL                        COMMENT 'Job group 名称',
  job_class_name              varchar(250)    NOT NULL                        COMMENT 'Job类名称',
  trigger_name                varchar(200)    NOT NULL                        COMMENT 'Trigger key',
  trigger_group               varchar(200)    NOT NULL                        COMMENT 'Trigger group名称',
  start_time                  datetime        NOT NULL                        COMMENT '开始触发时间',
  end_time                    datetime                                        COMMENT '触发完成时间',
  process_time                bigint                                          COMMENT '用时(ms)',
  pre_run_time                datetime                                        COMMENT '上一次触发时间',
  next_run_time               datetime                                        COMMENT '下一次触发时间',
  run_count                   int             NOT NULL                        COMMENT '触发次数',
  ip_address                  varchar(200)    NOT NULL                        COMMENT '触发节点IP,可能有多个(‘;’分隔)',
  mis_fired                   char(1)         NOT NULL                        COMMENT '是否错过了触发（0：否；1：是）',
  before_job_data             mediumtext                                      COMMENT '执行前的JobDataMap数据',
  after_job_data              mediumtext                                      COMMENT '执行后的JobDataMap数据',
  trigger_instruction_code    varchar(100)                                    COMMENT '触发指令码',
  instr_code                  varchar(100)                                    COMMENT '触发指令码说明',
  PRIMARY KEY (id)
) COMMENT = 'Trigger触发日志表';


/* ====================================================================================================================
    qrtz_scheduler_log -- Scheduler调度日志表
==================================================================================================================== */
CREATE TABLE qrtz_scheduler_log
(
  id                          bigint          NOT NULL    auto_increment      COMMENT '编号',
  listener_name               varchar(120)    NOT NULL                        COMMENT '监听器名称',
  sched_name                  varchar(120)    NOT NULL                        COMMENT 'Scheduler名称',
  instance_name               varchar(200)    NOT NULL                        COMMENT 'Scheduler实例的唯一标识，配置文件中的Instance Id',
  method_name                 varchar(120)    NOT NULL                        COMMENT '触发事件调用的方法',
  log_data                    mediumtext      NOT NULL                        COMMENT '触发事件记录的日志数据',
  ip_address                  varchar(200)    NOT NULL                        COMMENT '触发节点IP,可能有多个(‘;’分隔)',
  log_time                    datetime        NOT NULL                        COMMENT '记录时间',
  PRIMARY KEY (id)
) COMMENT = 'Scheduler调度日志表';













