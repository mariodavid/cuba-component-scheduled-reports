package de.diedavids.cuba.scheduledreports.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ScheduledFrequencyType implements EnumClass<String> {

    MONTHLY("MONTHLY"),
    DAILY("DAILY"),
    HOURLY("HOURLY"),
    CUSTOM("CUSTOM");

    private String id;

    ScheduledFrequencyType(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static ScheduledFrequencyType fromId(String id) {
        for (ScheduledFrequencyType at : ScheduledFrequencyType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}