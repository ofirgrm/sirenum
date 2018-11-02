package com.exam.domain;

import java.time.LocalTime;
import java.util.Currency;

public class Rate {

    private String name;
    private Currency hourlyRateCurrency;
    private double hourlyRate;
    private LocalTime timeOfDayStart;
    private LocalTime timeOfDayEnd;

    public Rate(String name, Currency hourlyRateCurrency, Double hourlyRate, LocalTime timeOfDayStart, LocalTime timeOfDayEnd) {
        this.name = name;
        this.hourlyRateCurrency = hourlyRateCurrency;
        this.hourlyRate = hourlyRate;
        this.timeOfDayStart = timeOfDayStart;
        this.timeOfDayEnd = timeOfDayEnd;
    }

    public String getName() {
        return name;
    }

    public Currency getHourlyRateCurrency() {
        return hourlyRateCurrency;
    }

    public LocalTime getTimeOfDayStart() {
        return timeOfDayStart;
    }

    public LocalTime getTimeOfDayEnd() {
        return timeOfDayEnd;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }
}
