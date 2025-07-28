package org.example.cli;

import org.apache.commons.cli.*;
import org.example.exception.AppException;
import java.util.List;

public class CommandLineParser {
    private final Options options = new Options();
    private CommandLine cmd;

    private CommandLineParser() {
        options.addOption("o", "output", true,
                "папка для результатов (по умолчанию \"./\")");
        options.addOption("p", "prefix", true,
                "префикс для имён файлов (по умолчанию пустой)");
        options.addOption("a", "append", false,
                "добавлять в существующие файлы");
        options.addOption("s", "short", false,
                "краткая статистика (count)");
        options.addOption("f", "full", false,
                "полная статистика");
        options.addOption("h", "help", false,
                "показать справку");
    }

    public static CommandLineParser parse(String[] args) {
        CommandLineParser commandLineParser = new CommandLineParser();
        commandLineParser.parseOptions(args);
        return commandLineParser;
    }

    private void parseOptions(String[] args) {
        try {
            cmd = new DefaultParser().parse(options, args);
        } catch (ParseException e) {
            throw new AppException("неверные аргументы", e);
        }
        if (cmd.hasOption("h")) {
            new HelpFormatter()
                    .printHelp("java -jar util.jar [опции] <файлы...>",
                            options, true);
            System.exit(0);
        }
        List<String> files = cmd.getArgList();
        if (files.isEmpty()) {
            throw new AppException("не указаны входные файлы");
        }
    }

    public String getOutputDir() {
        return cmd.getOptionValue("o", ".");
    }
    public String getPrefix() {
        return cmd.getOptionValue("p", "");
    }
    public boolean isAppend() {
        return cmd.hasOption("a");
    }
    public boolean isShortStatistics() {
        return cmd.hasOption("s");
    }
    public boolean isFullStatistics() {
        return cmd.hasOption("f");
    }
    public List<String> getInputFiles() {
        return cmd.getArgList();
    }
}
