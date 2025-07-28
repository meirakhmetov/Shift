package org.example.stats;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumericStats<T extends Number> {
    private long count = 0;
    private BigDecimal sum = BigDecimal.ZERO;
    private BigDecimal min, max;

    public void accept(T v) {
        if (v == null) return;
        BigDecimal x = new BigDecimal(v.toString());
        min = (min == null || x.compareTo(min) < 0) ? x : min;
        max = (max == null || x.compareTo(max) > 0) ? x : max;
        sum = sum.add(x);
        count++;
    }

    public String shortSummary() {
        return "count=%d".formatted(count);
    }

    public String fullSummary() {
        if (count == 0) return "count=0";
        BigDecimal avg = sum.divide(BigDecimal.valueOf(count), RoundingMode.HALF_UP);
        return String.format(
                "count=%d; min=%s; max=%s; sum=%s; avg=%s",
                count, min.toPlainString(), max.toPlainString(),
                sum.toPlainString(), avg.toPlainString()
        );
    }
}
