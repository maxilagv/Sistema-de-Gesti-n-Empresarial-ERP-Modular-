package com.example.erp.tax;

import java.math.BigDecimal;

public class ImpuestoNormalStrategy implements ImpuestoStrategy {
    private static final BigDecimal TASA = new BigDecimal("0.19");

    @Override
    public BigDecimal calcular(BigDecimal base) {
        if (base == null) return BigDecimal.ZERO;
        return base.multiply(TASA);
    }
}
