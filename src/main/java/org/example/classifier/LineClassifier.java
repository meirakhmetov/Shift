package org.example.classifier;

import java.util.regex.Pattern;

public class LineClassifier {
    public enum Type { INTEGER, FLOAT, STRING }

    private static final Pattern INT  = Pattern.compile("^[+-]?\\d+$");
    private static final Pattern FLT  = Pattern.compile("^[+-]?(?:\\d+\\.\\d*|\\.\\d+|\\d+)(?:[eE][+-]?\\d+)?$");

    public Type classify(String line) {
        String t = line.trim();
        if (t.isEmpty()) return Type.STRING;
        if (INT.matcher(t).matches()) return Type.INTEGER;
        if (FLT.matcher(t).matches()) return Type.FLOAT;
        return Type.STRING;
    }
}
