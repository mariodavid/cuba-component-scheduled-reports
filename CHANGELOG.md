# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [0.3.0] - 2020-09-10

### Dependencies
- CUBA 7.2.x
- Reports 7.2.x
- Email Template 1.4.2

## [0.2.0] - 2019-09-30

### Dependencies
- CUBA 7.1.x
- Reports 7.1.x
- Email Template 1.2.0

## [0.1.2] - 2019-07-27

### Added
- description field for `ScheduledReport`
- better context in log messages

### Bugfix
- typo in views.xml

## [0.1.1] - 2019-07-23

### Bugfix
- fixed problem with not existing code attribute in scheduled report entity

### Changed
- Executed At of `ScheduledReportExecution` is now a timestamp instead of Date

## [0.1.0] - 2019-07-17

### Added
- Scheduled Report for running a report on a preconfigured schedule (hourly, daily, monthly, custom)
- Ability to persist and send out the report document as an Email through an Email template
- Logging of executed reports

### Dependencies
- CUBA 7.0.x
- Reports 7.0.x
- Email Template 1.1.3