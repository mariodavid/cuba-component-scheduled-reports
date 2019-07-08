package de.diedavids.cuba.scheduledreports.web


import de.diedavids.cuba.scheduledreports.entity.ScheduledFrequency
import de.diedavids.cuba.scheduledreports.entity.ScheduledFrequencyType
import spock.lang.Specification
import spock.lang.Unroll

class ScheduledFrequencyCronGeneratorSpec extends Specification {
    ScheduledFrequencyCronGenerator sut

    void setup() {
        sut = new ScheduledFrequencyCronGenerator()
    }

    @Unroll
    def "createCronExpression cron expression for #hourlyMinute is #expectedExpression"() {
        given:
        def frequency = new ScheduledFrequency(
                frequency: ScheduledFrequencyType.HOURLY,
                hourlyMinute: hourlyMinute,
        )
        when:
        def actualExpression = sut.createCronExpression(frequency)
        then:
        expectedExpression == actualExpression

        where:
        hourlyMinute || expectedExpression
        9            || "0 9 * * * *"
        55           || "0 55 * * * *"
    }

    @Unroll
    def "createCronExpression cron expression for #dailyHour:#dailyMinute is #expectedExpression"() {
        given:
        def frequency = new ScheduledFrequency(
                frequency: ScheduledFrequencyType.DAILY,
                dailyMinute: dailyMinute,
                dailyHour: dailyHour,
        )
        when:
        def actualExpression = sut.createCronExpression(frequency)
        then:
        expectedExpression == actualExpression

        where:
        dailyHour | dailyMinute || expectedExpression
        9         | 12          || "0 12 9 * * *"
        10        | 55          || "0 55 10 * * *"
    }

    @Unroll
    def "createCronExpression cron expression for #monthlyDay. #monthlyHour:#monthlyMinute is #expectedExpression"() {
        given:
        def frequency = new ScheduledFrequency(
                frequency: ScheduledFrequencyType.MONTHLY,
                monthlyMinute: monthlyMinute,
                monthlyHour: monthlyHour,
                monthlyDay: monthlyDay
        )
        when:
        def actualExpression = sut.createCronExpression(frequency)
        then:
        expectedExpression == actualExpression

        where:
        monthlyDay | monthlyHour | monthlyMinute || expectedExpression
        2           | 9           | 12            || "0 12 9 2 * *"
        11          | 10          | 55            || "0 55 10 11 * *"
    }

}
