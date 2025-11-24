package com.example.demo.application;

import com.example.demo.domain.CalculationResult;
import com.example.demo.domain.DomainException;
import com.example.demo.domain.Evaluation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GradeCalculatorServiceTest {

	private GradeCalculatorService service;

	@BeforeEach
	void setUp() {
		service = new GradeCalculatorService();
	}

	@Test
	void shouldCalculatePerfectScore() {
		List<Evaluation> evals = List.of(new Evaluation(new BigDecimal("20"), new BigDecimal("100")));
		CalculationResult result = service.calculate(evals, true, false);
		assertEquals(new BigDecimal("20.00"), result.finalScore());
	}

	@Test
	void shouldApplyStrategiesCorrectly() {
		// 20 puntos, pero sin asistencia (x0.8) + Puntos extra (+1)
		// 20 * 0.8 = 16 + 1 = 17
		List<Evaluation> evals = List.of(new Evaluation(new BigDecimal("20"), new BigDecimal("100")));
		CalculationResult result = service.calculate(evals, false, true);

		assertEquals(new BigDecimal("17.00"), result.finalScore());
		assertTrue(result.details().contains("Attendance penalty"));
		assertTrue(result.details().contains("Extra points"));
	}

	@Test
	void shouldThrowExceptionIfListIsNull() {
		assertThrows(DomainException.class, () -> service.calculate(null, true, true));
	}

	@Test
	void shouldThrowExceptionIfListIsEmpty() {
		List<Evaluation> empty = List.of();
		assertThrows(DomainException.class, () -> service.calculate(empty, true, true));
	}

	@Test
	void shouldThrowExceptionIfMoreThan10Evals() {
		List<Evaluation> largeList = new ArrayList<>();
		for(int i=0; i<11; i++) largeList.add(new Evaluation(BigDecimal.ONE, BigDecimal.ONE));

		assertThrows(DomainException.class, () -> service.calculate(largeList, true, true));
	}

	// Test para cubrir la clase Evaluation y sus validaciones (Cobertura 100%)
	@Test
	void shouldThrowExceptionOnInvalidEvaluationData() {
		BigDecimal valid = BigDecimal.TEN;
		BigDecimal invalidScore = new BigDecimal("21");
		BigDecimal invalidWeight = new BigDecimal("-1");

		assertThrows(DomainException.class, () -> new Evaluation(null, valid));
		assertThrows(DomainException.class, () -> new Evaluation(valid, null));
		assertThrows(DomainException.class, () -> new Evaluation(invalidScore, valid));
		assertThrows(DomainException.class, () -> new Evaluation(valid, invalidWeight));
	}
}