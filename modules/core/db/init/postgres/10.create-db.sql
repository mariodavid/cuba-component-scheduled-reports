-- begin DDCSR_SCHEDULED_REPORT
create table DDCSR_SCHEDULED_REPORT (
    ID uuid,
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
    REPORT_ID uuid not null,
    REPORT_TEMPLATE_ID uuid,
    SCHEDULED_TASK_ID uuid not null,
    NAME varchar(255) not null,
    CODE varchar(255),
    ACTIVE boolean,
    EMAIL_TEMPLATE_ID uuid,
    --
    primary key (ID)
)^
-- end DDCSR_SCHEDULED_REPORT
-- begin DDCSR_SCHEDULED_REPORT_EXEC
create table DDCSR_SCHEDULED_REPORT_EXEC (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    SCHEDULED_REPORT_ID uuid not null,
    SUCCESSFUL_ boolean,
    EXECUTED_AT date not null,
    REPORT_FILE_ID uuid,
    --
    primary key (ID)
)^
-- end DDCSR_SCHEDULED_REPORT_EXEC
-- begin DDCSR_SR_EXEC_MESSAGE_LINK
create table DDCSR_SR_EXEC_MESSAGE_LINK (
    SCHEDULED_REPORT_EXECUTION_ID uuid,
    SENDING_MESSAGE_ID uuid,
    primary key (SCHEDULED_REPORT_EXECUTION_ID, SENDING_MESSAGE_ID)
)^
-- end DDCSR_SR_EXEC_MESSAGE_LINK
