package com.example.demo.application;

import java.math.BigDecimal;

public class ExtraPointsPolicy implements AdjustmentPolicy {
    private final boolean extraPointsAgreed;
    private static final BigDecimal EXTRA_VALUE = new BigDecimal("1.00");

    public ExtraPointsPolicy(boolean extraPointsAgreed) {
        this.extraPointsAgreed = extraPointsAgreed;
    }

    @Override
    public BigDecimal apply(BigDecimal currentScore, StringBuilder details) {
        if (extraPointsAgreed) {
            details.append("Extra points applied (+1.00). ");
            return currentScore.add(EXTRA_VALUE);
        }
        return currentScore;
    }
}