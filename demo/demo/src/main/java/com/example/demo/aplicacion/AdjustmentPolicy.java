package com.example.demo.application;

import java.math.BigDecimal;

public interface AdjustmentPolicy {
    BigDecimal apply(BigDecimal currentScore, StringBuilder details);
}