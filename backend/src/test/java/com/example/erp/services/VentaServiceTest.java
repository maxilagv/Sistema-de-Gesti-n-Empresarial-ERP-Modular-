package com.example.erp.services;

import com.example.erp.entities.Factura;
import com.example.erp.entities.Producto;
import com.example.erp.entities.Venta;
import com.example.erp.exceptions.ResourceNotFoundException;
import com.example.erp.repositories.FacturaRepository;
import com.example.erp.repositories.ProductoRepository;
import com.example.erp.repositories.VentaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock VentaRepository ventaRepository;
    @Mock FacturaRepository facturaRepository;
    @Mock ProductoRepository productoRepository;
    @Mock CalculoImpuestoService calculoImpuestoService;

    @InjectMocks VentaService ventaService;

    @Test
    void facturarConProductoNoEncontradoLanza404() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
            () -> ventaService.facturar(99L, 1, new BigDecimal("10.00")));
    }

    @Test
    void calculaSubtotalImpuestoYTotalConRedondeo() {
        Producto p = new Producto();
        p.setId(1L); p.setNombre("X"); p.setPrecio(new BigDecimal("35.99")); p.setExentoImpuesto(false);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(p));
        when(ventaRepository.save(any())).thenAnswer(inv -> { Venta v = inv.getArgument(0); v.setId(10L); return v; });
        when(facturaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(calculoImpuestoService.calcular(new BigDecimal("71.98"), false))
            .thenReturn(new BigDecimal("13.6762"));

        Factura f = ventaService.facturar(1L, 2, null); // usa precio del producto
        assertEquals(new BigDecimal("71.98"), f.getSubtotal());
        assertEquals(new BigDecimal("13.68"), f.getImpuesto());
        assertEquals(new BigDecimal("85.66"), f.getTotal());
        assertEquals(10L, f.getVenta().getId());
    }
}

