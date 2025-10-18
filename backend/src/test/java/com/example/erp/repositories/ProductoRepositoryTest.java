package com.example.erp.repositories;

import com.example.erp.entities.Producto;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ProductoRepositoryTest {

    @Autowired ProductoRepository productoRepository;

    @Test
    void validaNombreYPrecio() {
        Producto p = new Producto();
        p.setNombre(""); // inválido
        p.setPrecio(new BigDecimal("-10")); // inválido por @Positive
        assertThrows(ConstraintViolationException.class, () -> {
            productoRepository.saveAndFlush(p);
        });
    }

    @Test
    void persisteProductoValido() {
        Producto p = new Producto();
        p.setNombre("Valid");
        p.setPrecio(new BigDecimal("10.00"));
        Producto saved = productoRepository.saveAndFlush(p);
        assertNotNull(saved.getId());
    }
}

