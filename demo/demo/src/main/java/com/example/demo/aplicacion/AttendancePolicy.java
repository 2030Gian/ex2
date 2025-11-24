package com.example.demo.application;

import java.math.BigDecimal;

public class AttendancePolicy implements AdjustmentPolicy {
    private final boolean hasMinimumAttendance;
    private static final BigDecimal PENALTY_FACTOR = new BigDecimal("0.80");

    public AttendancePolicy(boolean hasMinimumAttendance) {
        this.hasMinimumAttendance = hasMinimumAttendance;
    }

    @Override
    public BigDecimal apply(BigDecimal currentScore, StringBuilder details) {
        if (!hasMinimumAttendance) {
            details.append("Attendance penalty applied (-20%). ");
            return currentScore.multiply(PENALTY_FACTOR);
        }
        details.append("Minimum attendance reached. ");
        return currentScore;
    }
}