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
);