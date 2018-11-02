package com.exam.service;

import com.exam.config.Bootstrap;
import com.exam.domain.PayBreakdown;
import com.exam.domain.Rate;
import com.exam.domain.Shift;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Test which uses the bootstrap to check each part of the service component (CalculateBreakdown)
 */
public class CalculateBreakdownImplTest {

    private CalculateBreakdownImpl calculateBreakdown;
    private List<Shift> shifts;
    private List<Rate> rates;

    @Before
    public void setUp() throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.prepare();
        this.shifts = bootstrap.getShifts();
        this.rates = bootstrap.getRates();
        this.calculateBreakdown = new CalculateBreakdownImpl();
    }

    @Test
    public void testCalculateBreakdown() {
        List<PayBreakdown> breakDownList = this.calculateBreakdown.calculate(this.shifts, this.rates);

        assertNotNull(breakDownList);
        assertThat(breakDownList.size(), is(3));
        boolean isDefault = false;
        boolean isMorning = false;
        boolean isEvening = false;
        for (PayBreakdown payBreakdown : breakDownList) {
            if (payBreakdown.getRateName().equals("Default")) {
                assertThat(payBreakdown.getTotalPay(), is(105d));
                assertThat(payBreakdown.getTotalWorkTime(), is(LocalTime.of(10, 30)));
                isDefault = true;
            }
            if (payBreakdown.getRateName().equals("Morning")) {
                assertThat(payBreakdown.getTotalPay(), is(75d));
                assertThat(payBreakdown.getTotalWorkTime(), is(LocalTime.of(5, 0)));
                isMorning = true;
            }
            if (payBreakdown.getRateName().equals("Evening")) {
                assertThat(payBreakdown.getTotalPay(), is(9d));
                assertThat(payBreakdown.getTotalWorkTime(), is(LocalTime.of(0, 30)));
                isEvening = true;
            }
        }
        assertTrue(isDefault);
        assertTrue(isMorning);
        assertTrue(isEvening);

    }

    @Test
    public void testGetOrderedListOfIntersectedRates() {
        List<Rate> rates = this.calculateBreakdown.getOrderedListOfIntersectedRates
                (this.rates, LocalTime.of(9,0), LocalTime.of(17,0));

        assertThat(rates.get(0).getTimeOfDayStart(), is(LocalTime.of(5,0)));
        assertThat(rates.get(1).getTimeOfDayStart(), is(LocalTime.of(16,30)));
    }

    @Test
    public void testGetDefaultRates() {
        Rate defaultRate = this.calculateBreakdown.getDefaultRate(rates);

        assertThat(defaultRate.getName(), is("Default"));
    }
}