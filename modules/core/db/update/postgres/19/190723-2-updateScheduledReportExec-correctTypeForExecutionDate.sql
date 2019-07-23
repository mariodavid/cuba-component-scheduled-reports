alter table DDCSR_SCHEDULED_REPORT_EXEC rename column executed_at to executed_at__u64642 ;
alter table DDCSR_SCHEDULED_REPORT_EXEC alter column executed_at__u64642 drop not null ;
alter table DDCSR_SCHEDULED_REPORT_EXEC add column EXECUTED_AT timestamp ^
update DDCSR_SCHEDULED_REPORT_EXEC set EXECUTED_AT = current_timestamp where EXECUTED_AT is null ;
alter table DDCSR_SCHEDULED_REPORT_EXEC alter column EXECUTED_AT set not null ;
