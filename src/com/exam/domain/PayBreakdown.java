package com.exam.domain;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Currency;

public class PayBreakdown {

    private String workerId;
    private String rateName;
    private LocalTime totalWorkTime;
    private double totalPay;
    private Currency totalPayCurrency;

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getRateName() {
        return rateName;
    }

    public void setRateName(String rateName) {
        this.rateName = rateName;
    }

    public LocalTime getTotalWorkTime() {
        return totalWorkTime;
    }

    public void addTotalWorkTime(Duration duration) {
        if (this.totalWorkTime == null){
            this.totalWorkTime = LocalTime.of(0, 0);
        }
        this.totalWorkTime = this.totalWorkTime.plus(duration);
    }

    public double getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(double totalPay) {
        this.totalPay = totalPay;
    }

    public Currency getTotalPayCurrency() {
        return totalPayCurrency;
    }

    public void setTotalPayCurrency(Currency totalPayCurrency) {
        this.totalPayCurrency = totalPayCurrency;
    }
}
