package org.example;

import org.example.cli.CommandLineParser;
import org.example.exception.AppException;
import org.example.classifier.LineClassifier;
import org.example.io.FileWritersManager;
import org.example.stats.NumericStats;
import org.example.stats.StringStats;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            CommandLineParser commandLineParser = CommandLineParser.parse(args);

            LineClassifier classifier = new LineClassifier();
            FileWritersManager writers = new FileWritersManager(
                    commandLineParser.getOutputDir(),
                    commandLineParser.getPrefix(),
                    commandLineParser.isAppend()
            );
            NumericStats<BigDecimal> intStats = new NumericStats<>();
            NumericStats<BigDecimal> floatStats = new NumericStats<>();
            StringStats stringStats = new StringStats();

            List<String> inputs = commandLineParser.getInputFiles();
            for (String file : inputs) {
                try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        switch (classifier.classify(line)) {
                            case INTEGER -> {
                                BigDecimal bigDecimal = new BigDecimal(line.trim());
                                writers.writeInteger(bigDecimal.toPlainString());
                                intStats.accept(bigDecimal);
                            }
                            case FLOAT -> {
                                BigDecimal bigDecimal = new BigDecimal(line.trim());
                                writers.writeFloat(bigDecimal.toPlainString());
                                floatStats.accept(bigDecimal);
                            }
                            case STRING -> {
                                writers.writeString(line);
                                stringStats.accept(line);
                            }
                        }
                    }
                } catch (IOException e) {
                    throw new AppException("Не удалось прочитать файл «%s»".formatted(file), e);
                }
            }
            writers.closeAll();

            boolean full = commandLineParser.isFullStatistics();
            System.out.println("=== Статистика ===");
            System.out.printf("Integers: %s%n", full
                    ? intStats.fullSummary()
                    : intStats.shortSummary());
            System.out.printf("Floats:   %s%n", full
                    ? floatStats.fullSummary()
                    : floatStats.shortSummary());
            System.out.printf("Strings:  %s%n", full
                    ? stringStats.fullSummary()
                    : stringStats.shortSummary());

        } catch (AppException e) {
            System.err.printf("Ошибка: %s%n", e.getMessage());
            System.exit(1);
        }
    }
}
