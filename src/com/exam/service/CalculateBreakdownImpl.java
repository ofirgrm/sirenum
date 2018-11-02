package com.exam.service;

import com.exam.domain.PayBreakdown;
import com.exam.domain.Rate;
import com.exam.domain.Shift;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class CalculateBreakdownImpl implements CalculateBreakdown {

    /**
     * Calculate the pay breakdown.
     * Uses an hashmap to hold the breakdown result by as following:
     *  worker -> rate -> PayBreakdown object
     * First calculate which rates are intersecting with the shift and then use iterate over them
     * Whenever a shift doesn't intersect with any rate, use the default
     *
     * @param shifts list of given shifts
     * @param rates list of given rates
     * @return
     */
    @Override
    public List<PayBreakdown> calculate(List<Shift> shifts, List<Rate> rates) {
        // create a map of Worker ID -> workerMap
        // workerMap is a map of Rate name -> PayBreakdown object
        Map<String, Map<String,PayBreakdown>> payBreakdownMap = new HashMap<>();

        // iterate over all the shifts
        for (Shift shift : shifts){

            // create the worker map (rate -> PayBreakdown) if it didn't existed before
            // otherwise, extract it from the main Map
            Map<String, PayBreakdown> workerPayBreakdownMap = payBreakdownMap.get(shift.getWorkerId());
            if (workerPayBreakdownMap == null){
                workerPayBreakdownMap = new HashMap<>();
                payBreakdownMap.put(shift.getWorkerId(), workerPayBreakdownMap);
            }
            // assign the shift times to start and end
            // start will be incremented for each rate it intersect with (or the default)
            LocalTime start = shift.getStart().toLocalTime();
            LocalTime end = shift.getEnd().toLocalTime();
            // calculate the intersected rates and return it as a list
            List<Rate> intersectedRates = getOrderedListOfIntersectedRates(rates, start, end);
            // get the default rate from the list of rates
            Rate defaultRate = getDefaultRate(rates);
            // iterate over all intersected rates and check if the current start of shift
            // intersect, if true -> use the rate. If false -> use the default
            for (Rate rate : intersectedRates) {
                LocalTime rateStart = rate.getTimeOfDayStart();
                LocalTime rateEnd = rate.getTimeOfDayEnd();
                // first check if rate started at the shift start
                // and if not use the default
                if (start.isBefore(rateStart)){
                    Duration duration = Duration.between(start, rateStart);
                    calculatePayBreakdown(defaultRate, duration, shift, workerPayBreakdownMap);
                    start = rateStart;
                }
                // now start is either the shift start or after the shift start, but not
                // before it ends
                if (start.isAfter(rateStart) || start.equals(rateStart)) {
                    LocalTime endTime = end.isAfter(rateEnd) ? rateEnd : end;
                    Duration duration = Duration.between(start, endTime);
                    calculatePayBreakdown(rate, duration, shift,workerPayBreakdownMap);
                }

                // calculate the new incremented start
                start = rate.getTimeOfDayEnd();
            }
            // in case the end of the shift ends with default rate calculate that rate as well
            if (start.isBefore(end)) {
                Duration duration = Duration.between(start, end);
                calculatePayBreakdown(defaultRate, duration, shift, workerPayBreakdownMap);
            }

        }

        // return a flat list of PayBreakdown
        return convertToList(payBreakdownMap);
    }

    /**
     * Returns an intersected list of rates
     * @param rates
     * @param start
     * @param end
     * @return
     */
    public List<Rate> getOrderedListOfIntersectedRates(List<Rate> rates, LocalTime start, LocalTime end) {
        TreeMap<LocalTime, Rate> sortedMap = new TreeMap<>();
        for (Rate rate : rates) {
            if (rate.getTimeOfDayStart() != null &&
                    rate.getTimeOfDayEnd().isAfter(start) &&
                    rate.getTimeOfDayStart().isBefore(end)){
                sortedMap.put(rate.getTimeOfDayStart(), rate);
            }
        }

        return sortedMap.values().stream().collect(Collectors.toList());
    }

    /**
     * Returns the default rate list
     *
     * @param rates
     * @return
     */
    public Rate getDefaultRate(List<Rate> rates) {
        return rates.stream()
                .filter(rate -> rate.getTimeOfDayStart() == null)
                .findAny()
                .orElseThrow(() -> new RuntimeException("Default rate not found"));
    }

    /**
     * Calculate the PayBreakdown objects for the given rate
     *
     * @param rate
     * @param duration
     * @param shift
     * @param workerPayBreakdownMap
     */
    private void calculatePayBreakdown(Rate rate, Duration duration, Shift shift,
                                               Map<String, PayBreakdown> workerPayBreakdownMap) {
        double rateAmount = rate.getHourlyRate() * duration.toMinutes() / 60;
        PayBreakdown payBreakdown = workerPayBreakdownMap.get(rate.getName());
        if (payBreakdown == null){
            payBreakdown = new PayBreakdown();
            payBreakdown.setRateName(rate.getName());
            payBreakdown.setTotalPayCurrency(rate.getHourlyRateCurrency());
            payBreakdown.setWorkerId(shift.getWorkerId());
            workerPayBreakdownMap.put(rate.getName(), payBreakdown);
        }
        rateAmount += payBreakdown.getTotalPay();
        payBreakdown.setTotalPay(rateAmount);
        payBreakdown.addTotalWorkTime(duration);

    }

    /**
     * Flatten the map values into a list
     *
     * @param payBreakdownMap
     * @return
     */
    private List<PayBreakdown> convertToList(Map<String,Map<String,PayBreakdown>> payBreakdownMap) {
        return payBreakdownMap.values().stream().flatMap(workerMap -> workerMap.values().stream())
                .collect(Collectors.toList());
    }

}
