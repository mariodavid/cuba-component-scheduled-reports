create table DDCSR_SCHEDULED_REPORT_CONFIG (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    REPORT_ID varchar(36) not null,
    NAME varchar(255) not null,
    CODE varchar(255),
    FREQUENCY varchar(50) not null,
    --
    primary key (ID)
);