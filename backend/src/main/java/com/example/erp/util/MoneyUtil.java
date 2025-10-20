package com.example.erp.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MoneyUtil {
    private MoneyUtil() {}

    public static BigDecimal bd(double v) {
        return BigDecimal.valueOf(v);
    }

    public static BigDecimal round2(BigDecimal v) {
        if (v == null) return BigDecimal.ZERO;
        return v.setScale(2, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal round3(BigDecimal v) {
        if (v == null) return BigDecimal.ZERO;
        return v.setScale(3, RoundingMode.HALF_EVEN);
    }
}

