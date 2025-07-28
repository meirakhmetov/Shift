package org.example.stats;

public class StringStats {
    private long count = 0;
    private int minLen = Integer.MAX_VALUE, maxLen = 0;

    public void accept(String s) {
        if (s == null) return;
        int l = s.length();
        minLen = Math.min(minLen, l);
        maxLen = Math.max(maxLen, l);
        count++;
    }

    public String shortSummary() {
        return "count=%d".formatted(count);
    }

    public String fullSummary() {
        if (count == 0) return "count=0";
        return String.format(
                "count=%d; minLength=%d; maxLength=%d",
                count, minLen, maxLen
        );
    }
}
