[![Build Status](https://travis-ci.com/mariodavid/cuba-component-scheduled-reports.svg?branch=master)](https://travis-ci.com/mariodavid/cuba-component-scheduled-reports)
[ ![Download](https://api.bintray.com/packages/mariodavid/cuba-components/cuba-component-scheduled-reports/images/download.svg) ](https://bintray.com/mariodavid/cuba-components/cuba-component-scheduled-reports/_latestVersion)
[![license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

# CUBA Platform Application Component - Scheduled Reports

This application component let's you schedule reports and execute them periodically.

It allows to define a schedule for a report to be executed. The target report file is stored CUBAs File Storage
and additionally optionally can be send out via Email.

## Installation

1. `scheduled-reports` is available in the [CUBA marketplace](https://www.cuba-platform.com/marketplace/scheduled-reports)
2. Select a version of the add-on which is compatible with the platform version used in your project:

| Platform Version | Add-on Version |
| ---------------- | -------------- |
| 7.0.x            | 0.1.x          |


The latest version is: [ ![Download](https://api.bintray.com/packages/mariodavid/cuba-components/cuba-component-scheduled-reports/images/download.svg) ](https://bintray.com/mariodavid/cuba-components/cuba-component-instant-launcher/_latestVersion)

Add custom application component to your project:

* Artifact group: `de.diedavids.cuba.scheduledreports`
* Artifact name: `scheduledreports-global`
* Version: *add-on version*

```groovy
dependencies {
  appComponent("de.diedavids.cuba.scheduledreports:scheduledreports-global:*addon-version*")
}
```

### CHANGELOG

Information on changes that happen through the different versions of the application component can be found in the [CHANGELOG](https://github.com/mariodavid/cuba-component-instant-launcher/blob/master/CHANGELOG.md).
The Changelog also contains information about breaking changes and tips on how to resolve them.

## Supported DBMS

The following databases are supported by this application component:

* HSQLDB
* PostgreSQL


## Example usage

To see this application component in action, check out this example: [cuba-example-using-scheduled-reports](https://github.com/mariodavid/cuba-example-using-scheduled-reports).


## Using the application component

The `scheduled-reports` application component enriches the Reports main menu with ability to define `Scheduled Reports`.

A `Scheduled Report` consists mainly of a reference to the report instance, that should be executed as well as a schedule
that defines on how often the report should be executed.

![sales report scheduled report](https://github.com/mariodavid/cuba-example-using-scheduled-reports/blob/master/img/sales-report-scheduled-report.png)


### Emailing Report

Besides the file generation itself, it is also possible to send out the report via email. In order to do this, a Email template
can be defined, that includes information on the receivers, the Email subject and body etc. The report file is attached
to the email.

![sales report scheduled email template](https://github.com/mariodavid/cuba-example-using-scheduled-reports/blob/master/img/sales-report-email-template.png)

### Programmatic Scheduled Report Extensions

Currently (v. 0.1.0) the ability to define certain parameters of the Report or the Email template are limited to the abilities
of the corresponding application components:

* [Reports](https://github.com/cuba-platform/reports)
* [Email Templates](https://github.com/cuba-platform/emailtemplate-addon)


Since there is the need to dynamically define certain information, it is possible to extend the functionality of a scheduled report
programmatically that is executed at the point when the scheduled report is run.

In order to enhance a scheduled report programmatically, a Spring bean has to be created in the core module of the application, which implements the `ScheduledReportExtension` interface.
This interface allows to programmatically define the following options during the execution of a scheduled report:

* define the report parameter
* define the report template
* define the target filename
* veto right to prevent execution


### Application Event: `ScheduledReportRun`

After the scheduled report was executed, the Spring application event `ScheduledReportRun` is published. It is possible
to register to this application event in order to programmatically use the result of the scheduled report execution.

An example can be found in the example project: [BigCustomersListSaver](https://github.com/mariodavid/cuba-example-using-scheduled-reports/blob/master/modules/core/src/de/diedavids/cuba/ceusr/core/BigCustomersListSaver.java#L27):

```
@Component(BigCustomersListSaver.NAME)
public class BigCustomersListSaver implements ApplicationListener<ScheduledReportRun> {

    public static final String NAME = "ceusr_BigCustomersListSaver";

    @Inject
    protected DataManager dataManager;

    @Override
    public void onApplicationEvent(ScheduledReportRun scheduledReportRun) {

        ScheduledReportExecution reportExecution = scheduledReportRun.getReportExecution();
        String scheduledReportCode = reportExecution.getScheduledReport().getCode();

        if (scheduledReportCode.equals("big-customers")) {
            BigCustomersList bigCustomersList = dataManager.create(BigCustomersList.class);
            bigCustomersList.setFrom(toLocalDate(reportExecution.getExecutedAt()));
            bigCustomersList.setBigCustomerListFile(scheduledReportRun.getReportFile());
            bigCustomersList.setScheduledReportExecution(reportExecution);
            dataManager.commit(bigCustomersList);
        };
    }

    private LocalDate toLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

}
```

### Scheduled Report Execution

Every scheduled report execution is logged and can be seen via the menu `Reports > Scheduled Reports > Executions`. It contains
information about the execution timestamp, the report file and optionally a list of outgoing emails and their sending status.

![big customer scheduled report execution](https://github.com/mariodavid/cuba-example-using-scheduled-reports/blob/master/img/big-customers-execution.png)

![sales report scheduled execution details](https://github.com/mariodavid/cuba-example-using-scheduled-reports/blob/master/img/sales-report-execution.png)
