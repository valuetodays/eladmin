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

CREATE TABLE if not exists "qrtz_job_details"
(
    "sched_name" varchar(120) NOT NULL,
    "job_name" varchar(200) NOT NULL,
    "job_group" varchar(200) NOT NULL,
    "description" varchar(250) NULL,
    "job_class_name" varchar(250) NOT NULL,
    "is_durable" varchar(1) NOT NULL,
    "is_nonconcurrent" varchar(1) NOT NULL,
    "is_update_data" varchar(1) NOT NULL,
    "requests_recovery" varchar(1) NOT NULL,
    "job_data" BYTEA NULL
);


-- 主键
alter table "qrtz_job_details"
add constraint pk_qrtz_job_details primary key ("sched_name", "job_name", "job_group");


CREATE TABLE if not exists "qrtz_triggers"
(
    "sched_name" varchar(120) NOT NULL,
    "trigger_name" varchar(200) NOT NULL,
    "trigger_group" varchar(200) NOT NULL,
    "job_name" varchar(200) NOT NULL,
    "job_group" varchar(200) NOT NULL,
    "description" varchar(250) NULL,
    "next_fire_time" bigint NULL,
    "prev_fire_time" bigint NULL,
    "priority" integer NULL,
    "trigger_state" varchar(16) NOT NULL,
    "trigger_type" varchar(8) NOT NULL,
    "start_time" bigint NOT NULL,
    "end_time" bigint NULL,
    "calendar_name" varchar(200) NULL,
    "misfire_instr" smallint NULL,
    "job_data" BYTEA NULL
);


-- 主键
alter table "qrtz_triggers"
add constraint pk_qrtz_triggers primary key ("sched_name","trigger_name","trigger_group");


CREATE TABLE if not exists "qrtz_simple_triggers"
(
    "sched_name" varchar(120) NOT NULL,
    "trigger_name" varchar(200) NOT NULL,
    "trigger_group" varchar(200) NOT NULL,
    "repeat_count" bigint NOT NULL,
    "repeat_interval" bigint NOT NULL,
    "times_triggered" bigint NOT NULL
);

-- 主键
alter table "qrtz_simple_triggers"
add constraint pk_qrtz_simple_triggers primary key ("sched_name","trigger_name","trigger_group");

CREATE TABLE if not exists "qrtz_cron_triggers"
(
    sched_name varchar(120) not null,
    trigger_name varchar(200) not null,
    trigger_group varchar(200) not null,
    cron_expression varchar(120) not null,
    time_zone_id varchar(80)
);

alter table "qrtz_cron_triggers"
add constraint pk_qrtz_cron_triggers primary key ("sched_name","trigger_name","trigger_group");

CREATE TABLE if not exists "qrtz_simprop_triggers"
(
    "sched_name" varchar(120) NOT NULL,
    "trigger_name" varchar(200) NOT NULL,
    "trigger_group" varchar(200) NOT NULL,
    "str_prop_1" varchar(512) NULL,
    "str_prop_2" varchar(512) NULL,
    "str_prop_3" varchar(512) NULL,
    "int_prop_1" int NULL,
    "int_prop_2" int NULL,
    "long_prop_1" bigint NULL,
    "long_prop_2" bigint NULL,
    "dec_prop_1" numeric(13,4) NULL,
    "dec_prop_2" numeric(13,4) NULL,
    "bool_prop_1" varchar(1) NULL,
    "bool_prop_2" varchar(1) NULL
);


-- 主键
alter table "qrtz_simprop_triggers"
add constraint pk_qrtz_simprop_triggers primary key ("sched_name","trigger_name","trigger_group");


CREATE TABLE if not exists "qrtz_calendars"
(
    "sched_name" varchar(120) NOT NULL,
    "calendar_name" varchar(200) NOT NULL,
    "calendar" BYTEA NOT NULL
);

-- 主键
alter table "qrtz_calendars"
add constraint pk_qrtz_calendars primary key ("sched_name", "calendar_name");


CREATE TABLE if not exists "qrtz_paused_trigger_grps"
(
    "sched_name" varchar(120) NOT NULL,
    "trigger_group" varchar(200) NOT NULL
);

-- 主键
alter table "qrtz_paused_trigger_grps"
add constraint pk_qrtz_paused_trigger_grps primary key ("sched_name", "trigger_group");


CREATE TABLE if not exists "qrtz_fired_triggers"
(
    "sched_name" varchar(120) NOT NULL,
    "entry_id" varchar(95) NOT NULL,
    "trigger_name" varchar(200) NOT NULL,
    "trigger_group" varchar(200) NOT NULL,
    "instance_name" varchar(200) NOT NULL,
    "fired_time" bigint NOT NULL,
    "sched_time" bigint NOT NULL,
    "priority" integer NOT NULL,
    "state" varchar(16) NOT NULL,
    "job_name" varchar(200) NULL,
    "job_group" varchar(200) NULL,
    "is_nonconcurrent" varchar(1) NULL,
    "requests_recovery" varchar(1) NULL
);

-- 主键
alter table "qrtz_fired_triggers"
add constraint pk_qrtz_fired_triggers primary key ("sched_name", "entry_id");


CREATE TABLE if not exists "qrtz_scheduler_state"
(
    "sched_name" varchar(120) NOT NULL,
    "instance_name" varchar(200) NOT NULL,
    "last_checkin_time" bigint NOT NULL,
    "checkin_interval" bigint NOT NULL
);

-- 主键
alter table "qrtz_scheduler_state"
add constraint pk_qrtz_scheduler_state primary key ("sched_name","instance_name");

CREATE TABLE if not exists "qrtz_locks"
(
    "sched_name" varchar(120) NOT NULL,
    "lock_name" varchar(40) NOT NULL
);

-- 主键
alter table "qrtz_locks"
add constraint pk_qrtz_locks primary key ("sched_name", "lock_name");



create index idx_qrtz_j_req_recovery on qrtz_job_details(sched_name, requests_recovery);
create index idx_qrtz_j_grp on qrtz_job_details(sched_name, job_group);

create index idx_qrtz_t_j on qrtz_triggers(sched_name, job_name, job_group);
create index idx_qrtz_t_jg on qrtz_triggers(sched_name, job_group);
create index idx_qrtz_t_c on qrtz_triggers(sched_name, calendar_name);
create index idx_qrtz_t_g on qrtz_triggers(sched_name, trigger_group);
create index idx_qrtz_t_state on qrtz_triggers(sched_name, trigger_state);
create index idx_qrtz_t_n_state on qrtz_triggers(sched_name, trigger_name, trigger_group, trigger_state);
create index idx_qrtz_t_n_g_state on qrtz_triggers(sched_name, trigger_group, trigger_state);
create index idx_qrtz_t_next_fire_time on qrtz_triggers(sched_name, next_fire_time);
create index idx_qrtz_t_nft_st on qrtz_triggers(sched_name, trigger_state, next_fire_time);
create index idx_qrtz_t_nft_misfire on qrtz_triggers(sched_name, misfire_instr, next_fire_time);
create index idx_qrtz_t_nft_st_misfire on qrtz_triggers(sched_name, misfire_instr, next_fire_time, trigger_state);
create index idx_qrtz_t_nft_st_misfire_grp on qrtz_triggers(sched_name, misfire_instr, next_fire_time, trigger_group, trigger_state);

create index idx_qrtz_ft_trig_inst_name on qrtz_fired_triggers(sched_name, instance_name);
create index idx_qrtz_ft_inst_job_req_rcvry on qrtz_fired_triggers(sched_name, instance_name, requests_recovery);
create index idx_qrtz_ft_j_g on qrtz_fired_triggers(sched_name, job_name, job_group);
create index idx_qrtz_ft_jg on qrtz_fired_triggers(sched_name, job_group);
create index idx_qrtz_ft_t_g on qrtz_fired_triggers(sched_name, trigger_name, trigger_group);
create index idx_qrtz_ft_tg on qrtz_fired_triggers(sched_name, trigger_group);

