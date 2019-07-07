alter table DDCSR_SCHEDULED_REPORT_EXEC alter column EXECUTED_AT rename to EXECUTED_AT__U38266 ^
alter table DDCSR_SCHEDULED_REPORT_EXEC alter column EXECUTED_AT__U38266 set null ;
alter table DDCSR_SCHEDULED_REPORT_EXEC add column REPORT_FILE_ID varchar(36) ;
alter table DDCSR_SCHEDULED_REPORT_EXEC add column EXECUTED_AT date ^
update DDCSR_SCHEDULED_REPORT_EXEC set EXECUTED_AT = current_date where EXECUTED_AT is null ;
alter table DDCSR_SCHEDULED_REPORT_EXEC alter column EXECUTED_AT set not null ;
