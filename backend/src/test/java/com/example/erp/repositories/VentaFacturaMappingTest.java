package com.example.erp.repositories;

import com.example.erp.entities.Factura;
import com.example.erp.entities.Producto;
import com.example.erp.entities.Venta;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class VentaFacturaMappingTest {

    @Autowired VentaRepository ventaRepository;
    @Autowired FacturaRepository facturaRepository;
    @Autowired ProductoRepository productoRepository;

    @Test
    void unicaFacturaPorVenta() {
        Producto p = new Producto();
        p.setNombre("P");
        p.setPrecio(new BigDecimal("5.00"));
        productoRepository.saveAndFlush(p);

        Venta v = new Venta();
        v.setProducto(p);
        v.setCantidad(1);
        v.setPrecioUnitario(new BigDecimal("5.00"));
        v = ventaRepository.saveAndFlush(v);

        Factura f1 = new Factura();
        f1.setVenta(v);
        f1.setSubtotal(new BigDecimal("5.00"));
        f1.setImpuesto(new BigDecimal("0.95"));
        f1.setTotal(new BigDecimal("5.95"));
        facturaRepository.saveAndFlush(f1);

        Factura f2 = new Factura();
        f2.setVenta(v); // misma venta
        f2.setSubtotal(new BigDecimal("5.00"));
        f2.setImpuesto(new BigDecimal("0.95"));
        f2.setTotal(new BigDecimal("5.95"));

        assertThrows(DataIntegrityViolationException.class, () -> {
            facturaRepository.saveAndFlush(f2);
        });
    }
}

