package com.example.demo.application;

import com.example.demo.domain.CalculationResult;
import com.example.demo.domain.DomainException;
import com.example.demo.domain.Evaluation;
import com.example.demo.domain.GradeCalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

public class GradeCalculatorService implements GradeCalculator {

    private static final int MAX_EVALUATIONS = 10;
    private static final BigDecimal MAX_SCORE_CAP = new BigDecimal("20.00");

    @Override
    public CalculationResult calculate(List<Evaluation> evaluations, boolean hasMinimumAttendance, boolean extraPointsAgreed) {
        validateEvaluations(evaluations);

        StringBuilder details = new StringBuilder();

        // 1. Cálculo Base
        BigDecimal score = calculateBaseScore(evaluations, details);

        // 2. Aplicación de Políticas (Pattern: Chain of Responsibility / Strategy)
        List<AdjustmentPolicy> policies = List.of(
                new AttendancePolicy(hasMinimumAttendance),
                new ExtraPointsPolicy(extraPointsAgreed)
        );

        for (AdjustmentPolicy policy : policies) {
            score = policy.apply(score, details);
        }

        // 3. Cap final (Asegurar que no pase de 20)
        score = score.min(MAX_SCORE_CAP).setScale(2, RoundingMode.HALF_UP);

        return new CalculationResult(score, details.toString());
    }

    private void validateEvaluations(List<Evaluation> evaluations) {
        if (Objects.isNull(evaluations) || evaluations.isEmpty()) {
            throw new DomainException("Evaluations list cannot be empty");
        }
        if (evaluations.size() > MAX_EVALUATIONS) {
            throw new DomainException("Maximum number of evaluations allowed is " + MAX_EVALUATIONS);
        }
    }

    private BigDecimal calculateBaseScore(List<Evaluation> evaluations, StringBuilder details) {
        BigDecimal totalScore = BigDecimal.ZERO;
        BigDecimal totalWeight = BigDecimal.ZERO;

        for (Evaluation eval : evaluations) {
            totalScore = totalScore.add(eval.getWeightedScore());
            totalWeight = totalWeight.add(eval.getWeightPercentage());
        }

        details.append(String.format("Base Score: %s (Weight: %s%%). ",
                totalScore.setScale(2, RoundingMode.HALF_UP),
                totalWeight.setScale(2, RoundingMode.HALF_UP)));

        return totalScore;
    }
}