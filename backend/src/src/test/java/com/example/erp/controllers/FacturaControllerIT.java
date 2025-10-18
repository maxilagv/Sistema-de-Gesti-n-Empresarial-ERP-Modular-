package com.example.erp.controllers;

import com.example.erp.BaseIntegrationTest;
import com.example.erp.entities.Producto;
import com.example.erp.entities.Usuario;
import com.example.erp.repositories.ProductoRepository;
import com.example.erp.repositories.UsuarioRepository;
import com.example.erp.support.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FacturaControllerIT extends BaseIntegrationTest {

    @Autowired UsuarioRepository usuarioRepository;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired ProductoRepository productoRepository;

    private String userToken;
    private Long productoId;

    @BeforeEach
    void setup() {
        usuarioRepository.deleteAll();
        productoRepository.deleteAll();
        Usuario user = TestDataFactory.user("bob", "pass", passwordEncoder);
        usuarioRepository.save(user);
        userToken = tokenFor("bob", java.util.List.of("ROLE_USER"));

        Producto p = TestDataFactory.producto("P", new BigDecimal("10.00"), false);
        productoId = productoRepository.save(p).getId();
    }

    @Test
    void listarYObtenerFactura() throws Exception {
        // genera una factura
        mockMvc.perform(post("/api/ventas/facturar")
                .header("Authorization", bearer(userToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"productoId\":" + productoId + ",\"cantidad\":1}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists());

        // lista paginada
        mockMvc.perform(get("/api/facturas?page=0&size=10")
                .header("Authorization", bearer(userToken)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(1))));
    }
}

