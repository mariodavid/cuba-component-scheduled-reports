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
    CONFIG_ID varchar(36) not null,
    SUCCESSFUL_ boolean,
    EXECUTED_AT timestamp not null,
    --
    primary key (ID)
);