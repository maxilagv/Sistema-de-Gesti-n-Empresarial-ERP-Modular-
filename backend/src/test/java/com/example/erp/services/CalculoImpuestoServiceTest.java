package com.example.erp.services;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CalculoImpuestoServiceTest {
    private final CalculoImpuestoService service = new CalculoImpuestoService();

    @Test
    void impuestoNormal() {
        BigDecimal base = new BigDecimal("100.00");
        assertEquals(new BigDecimal("19.00"), service.calcular(base, false));
    }

    @Test
    void impuestoExento() {
        BigDecimal base = new BigDecimal("100.00");
        assertEquals(new BigDecimal("0"), service.calcular(base, true));
    }

    @Test
    void baseNulaDevuelveCero() {
        assertEquals(new BigDecimal("0"), service.calcular(null, false));
    }
}

