package com.example.demo.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Evaluation {
    private static final BigDecimal MIN_SCORE = BigDecimal.ZERO;
    private static final BigDecimal MAX_SCORE = new BigDecimal("20");
    private static final BigDecimal MIN_WEIGHT = BigDecimal.ZERO;
    private static final BigDecimal MAX_WEIGHT = new BigDecimal("100");
    private static final int SCALE = 4;

    private final BigDecimal score;
    private final BigDecimal weightPercentage;

    public Evaluation(BigDecimal score, BigDecimal weightPercentage) {
        validate(score, weightPercentage);
        this.score = score;
        this.weightPercentage = weightPercentage;
    }

    private void validate(BigDecimal score, BigDecimal weight) {
        if (Objects.isNull(score) || Objects.isNull(weight)) {
            throw new DomainException("Score and weight cannot be null");
        }
        if (score.compareTo(MIN_SCORE) < 0 || score.compareTo(MAX_SCORE) > 0) {
            throw new DomainException("Score must be between 0 and 20");
        }
        if (weight.compareTo(MIN_WEIGHT) <= 0 || weight.compareTo(MAX_WEIGHT) > 0) {
            throw new DomainException("Weight must be between 0 and 100");
        }
    }

    public BigDecimal getWeightedScore() {
        return score.multiply(weightPercentage)
                .divide(MAX_WEIGHT, SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal getWeightPercentage() {
        return weightPercentage;
    }
}