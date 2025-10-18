package com.example.erp.services;

import com.example.erp.tax.ImpuestoExentoStrategy;
import com.example.erp.tax.ImpuestoNormalStrategy;
import com.example.erp.tax.ImpuestoStrategy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CalculoImpuestoService {

    private final ImpuestoStrategy normal = new ImpuestoNormalStrategy();
    private final ImpuestoStrategy exento = new ImpuestoExentoStrategy();

    public BigDecimal calcular(BigDecimal base, boolean esExento) {
        return (esExento ? exento : normal).calcular(base);
    }
}
