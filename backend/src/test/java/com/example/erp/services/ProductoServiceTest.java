package com.example.erp.services;

import com.example.erp.entities.Producto;
import com.example.erp.exceptions.ResourceNotFoundException;
import com.example.erp.repositories.ProductoRepository;
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
class ProductoServiceTest {

    @Mock
    ProductoRepository productoRepository;

    @InjectMocks
    ProductoService productoService;

    @Test
    void findByIdNotFound() {
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productoService.findById(1L));
    }

    @Test
    void updateUpdatesFields() {
        Producto p = new Producto();
        p.setId(1L);
        p.setNombre("A");
        p.setPrecio(new BigDecimal("10.00"));

        when(productoRepository.findById(1L)).thenReturn(Optional.of(p));
        when(productoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Producto data = new Producto();
        data.setNombre("B");
        data.setPrecio(new BigDecimal("12.50"));
        data.setExentoImpuesto(true);

        Producto out = productoService.update(1L, data);
        assertEquals("B", out.getNombre());
        assertEquals(new BigDecimal("12.50"), out.getPrecio());
        assertTrue(out.isExentoImpuesto());
    }
}

