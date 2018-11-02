package com.exam.service;

import com.exam.domain.PayBreakdown;
import com.exam.domain.Rate;
import com.exam.domain.Shift;

import java.util.List;

public interface CalculateBreakdown {

    List<PayBreakdown> calculate(List<Shift> shifts, List<Rate> rates);

}
