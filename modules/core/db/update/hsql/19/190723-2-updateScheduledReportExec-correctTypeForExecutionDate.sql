alter table DDCSR_SCHEDULED_REPORT_EXEC alter column EXECUTED_AT rename to EXECUTED_AT__U03332 ^
alter table DDCSR_SCHEDULED_REPORT_EXEC alter column EXECUTED_AT__U03332 set null ;
alter table DDCSR_SCHEDULED_REPORT_EXEC add column EXECUTED_AT timestamp ^
update DDCSR_SCHEDULED_REPORT_EXEC set EXECUTED_AT = current_timestamp where EXECUTED_AT is null ;
alter table DDCSR_SCHEDULED_REPORT_EXEC alter column EXECUTED_AT set not null ;
