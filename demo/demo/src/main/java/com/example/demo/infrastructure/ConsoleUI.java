package com.example.demo.infrastructure;

import com.example.demo.application.GradeCalculatorService;
import com.example.demo.domain.CalculationResult;
import com.example.demo.domain.DomainException;
import com.example.demo.domain.Evaluation;
import com.example.demo.domain.GradeCalculator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {

    private final GradeCalculator gradeCalculator;

    public ConsoleUI() {
        this.gradeCalculator = new GradeCalculatorService();
    }

    public void start() {
        System.out.println("=== CS-GradeCalculator ===");

        // Try-with-resources para asegurar que Scanner se cierra (Sonar Rule)
        try (Scanner scanner = new Scanner(System.in)) {

            List<Evaluation> evaluations = collectEvaluations(scanner);
            boolean attendance = askBoolean(scanner, "Has minimum attendance? (true/false): ");
            boolean extraPoints = askBoolean(scanner, "Extra points agreed? (true/false): ");

            long startTime = System.currentTimeMillis();
            CalculationResult result = gradeCalculator.calculate(evaluations, attendance, extraPoints);
            long endTime = System.currentTimeMillis();

            printResult(result, endTime - startTime);

        } catch (DomainException e) {
            System.err.println("Validation Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Input Error: Invalid number format.");
        } catch (Exception e) {
            // Sonar quiere que loguees esto, pero System.err es aceptable en Console Apps
            System.err.println("Unexpected Error: " + e.getClass().getSimpleName());
        }
    }

    private List<Evaluation> collectEvaluations(Scanner scanner) {
        List<Evaluation> list = new ArrayList<>();
        System.out.print("Number of evaluations: ");
        int count = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < count; i++) {
            System.out.printf("--- Eval %d ---%n", i + 1);
            System.out.print("Score (0-20): ");
            BigDecimal score = new BigDecimal(scanner.nextLine());
            System.out.print("Weight (0-100): ");
            BigDecimal weight = new BigDecimal(scanner.nextLine());
            list.add(new Evaluation(score, weight));
        }
        // Retornamos copia inmutable para seguridad
        return List.copyOf(list);
    }

    private boolean askBoolean(Scanner scanner, String msg) {
        System.out.print(msg);
        return Boolean.parseBoolean(scanner.nextLine());
    }

    private void printResult(CalculationResult result, long duration) {
        System.out.println("\n=== RESULT ===");
        System.out.println("Final Grade: " + result.finalScore());
        System.out.println("Details: " + result.details());
        System.out.println("Time: " + duration + "ms");
    }
}