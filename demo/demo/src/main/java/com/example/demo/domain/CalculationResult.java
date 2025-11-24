package com.example.demo.domain;

import java.math.BigDecimal;

public record CalculationResult(BigDecimal finalScore, String details) {
    public CalculationResult {
        if (finalScore == null) throw new DomainException("Final score cannot be null");
        if (details == null) throw new DomainException("Details cannot be null");
    }
}