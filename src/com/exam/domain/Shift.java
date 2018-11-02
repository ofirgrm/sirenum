package com.exam.domain;

import java.time.LocalDateTime;

public class Shift {

    private String workerId;
    private LocalDateTime start;
    private LocalDateTime end;

    public Shift(String workerId, LocalDateTime start, LocalDateTime end) {
        this.workerId = workerId;
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public String getWorkerId() {
        return workerId;
    }
}
