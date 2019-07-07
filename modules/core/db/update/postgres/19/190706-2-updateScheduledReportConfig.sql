-- alter table DDCSR_SCHEDULED_REPORT_CONFIG add column SCHEDULED_TASK_ID uuid ^
-- update DDCSR_SCHEDULED_REPORT_CONFIG set SCHEDULED_TASK_ID = <default_value> ;
-- alter table DDCSR_SCHEDULED_REPORT_CONFIG alter column SCHEDULED_TASK_ID set not null ;
alter table DDCSR_SCHEDULED_REPORT_CONFIG add column SCHEDULED_TASK_ID uuid not null ;
