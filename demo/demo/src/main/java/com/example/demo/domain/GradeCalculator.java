package com.example.demo.domain;

import java.util.List;

public interface GradeCalculator {
    CalculationResult calculate(List<Evaluation> evaluations, boolean hasMinimumAttendance, boolean extraPointsAgreed);
}