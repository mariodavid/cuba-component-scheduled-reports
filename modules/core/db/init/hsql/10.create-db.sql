-- begin DDCSR_SCHEDULED_REPORT
create table DDCSR_SCHEDULED_REPORT (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    FREQUENCY_FREQUENCY varchar(50) not null,
    FREQUENCY_CUSTOM_CRON varchar(255),
    FREQUENCY_DAILY_HOUR integer,
    FREQUENCY_DAILY_MINUTE integer,
    FREQUENCY_HOURLY_MINUTE integer,
    FREQUENCY_MONTHLY_DAY integer,
    FREQUENCY_MONTHLY_HOUR integer,
    MONTHLY_MINUTE integer,
    --
    REPORT_ID varchar(36) not null,
    REPORT_TEMPLATE_ID varchar(36),
    SCHEDULED_TASK_ID varchar(36) not null,
    NAME varchar(255) not null,
    CODE varchar(255),
    ACTIVE boolean,
    SEND_EMAIL boolean,
    EMAIL_TEMPLATE_ID varchar(36),
    --
    primary key (ID)
)^
-- end DDCSR_SCHEDULED_REPORT
-- begin DDCSR_SCHEDULED_REPORT_EXEC
create table DDCSR_SCHEDULED_REPORT_EXEC (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    SCHEDULED_REPORT_ID varchar(36) not null,
    SUCCESSFUL_ boolean,
    EXECUTED_AT date not null,
    REPORT_FILE_ID varchar(36),
    --
    primary key (ID)
)^
-- end DDCSR_SCHEDULED_REPORT_EXEC
-- begin DDCSR_SR_EXEC_MESSAGE_LINK
create table DDCSR_SR_EXEC_MESSAGE_LINK (
    SCHEDULED_REPORT_EXECUTION_ID varchar(36) not null,
    SENDING_MESSAGE_ID varchar(36) not null,
    primary key (SCHEDULED_REPORT_EXECUTION_ID, SENDING_MESSAGE_ID)
)^
-- end DDCSR_SR_EXEC_MESSAGE_LINK
