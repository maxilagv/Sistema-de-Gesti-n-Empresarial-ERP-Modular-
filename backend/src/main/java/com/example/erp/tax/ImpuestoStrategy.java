package com.example.erp.tax;

import java.math.BigDecimal;

public interface ImpuestoStrategy {
    BigDecimal calcular(BigDecimal base);
}
