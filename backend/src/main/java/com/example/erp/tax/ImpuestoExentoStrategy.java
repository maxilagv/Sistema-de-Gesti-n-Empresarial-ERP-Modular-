package com.example.erp.tax;

import java.math.BigDecimal;

public class ImpuestoExentoStrategy implements ImpuestoStrategy {
    @Override
    public BigDecimal calcular(BigDecimal base) {
        return BigDecimal.ZERO;
    }
}
