-- begin DDCSR_SCHEDULED_REPORT_CONFIG
create table DDCSR_SCHEDULED_REPORT_CONFIG (
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
    SCHEDULED_TASK_ID uuid not null,
    NAME varchar(255) not null,
    CODE varchar(255),
    ACTIVE boolean,
    --
    primary key (ID)
)^
-- end DDCSR_SCHEDULED_REPORT_CONFIG
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
    CONFIG_ID uuid not null,
    SUCCESSFUL_ boolean,
    EXECUTED_AT date not null,
    REPORT_FILE_ID uuid,
    --
    primary key (ID)
)^
-- end DDCSR_SCHEDULED_REPORT_EXEC
