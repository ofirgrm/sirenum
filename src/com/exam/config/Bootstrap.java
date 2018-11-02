package com.exam.config;

import com.exam.domain.Rate;
import com.exam.domain.Shift;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

/**
 * Bootstrap class for initializing the (test) data for the application
 *
 */
public class Bootstrap {

    private List<Shift> shifts;
    private List<Rate> rates;

    /**
     * Initiate the Shifts and Rates and bootstrap them with initialization data
     */
    public void prepare() {
        this.shifts = new ArrayList<>();
        this.rates = new ArrayList<>();
        // bootstrap shifts and rates
        this.prepareShifts();
        this.prepareRates();
    }

    /**
     * Prepare 2 bootstrap shifts
     */
    private void prepareShifts() {
        // set John first shift
        LocalDateTime johnShift1Start = LocalDateTime.of(2017, 6, 23, 9, 0);
        LocalDateTime johnShift1End = LocalDateTime.of(2017, 6, 23, 17, 0);
        // set John second shift
        LocalDateTime johnShift2Start = LocalDateTime.of(2017, 6, 24, 6, 0);
        LocalDateTime johnShift2End = LocalDateTime.of(2017, 6, 24, 14, 0);
        // add John shifts to the list of shifts
        shifts.add(new Shift("John", johnShift1Start, johnShift1End));
        shifts.add(new Shift("John", johnShift2Start, johnShift2End));
    }

    /**
     * Prepare bootstrap 3 hourly rates and one default rate
     */
    private void prepareRates() {
        // set the Currency as GBP (symbol) which is applicable in all rates
        Currency gbp = Currency.getInstance("GBP");
        rates.add(new Rate("Morning", gbp, 15d, LocalTime.of(5, 0), LocalTime.of(10, 0)));
        rates.add(new Rate("Evening", gbp, 18d, LocalTime.of(16, 30), LocalTime.of(20, 0)));
        rates.add(new Rate("Night", gbp, 20d, LocalTime.of(20, 0), LocalTime.of(23, 0)));
        rates.add(new Rate("Default", gbp, 10d, null, null));
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public List<Rate> getRates() {
        return rates;
    }
}
