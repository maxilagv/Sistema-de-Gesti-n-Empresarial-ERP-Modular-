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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class VentaControllerIT extends BaseIntegrationTest {

    @Autowired UsuarioRepository usuarioRepository;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired ProductoRepository productoRepository;

    private String userToken;
    private Long productoIdNormal;
    private Long productoIdExento;

    @BeforeEach
    void setup() {
        usuarioRepository.deleteAll();
        productoRepository.deleteAll();
        Usuario user = TestDataFactory.user("bob", "pass", passwordEncoder);
        usuarioRepository.save(user);
        userToken = tokenFor("bob", java.util.List.of("ROLE_USER"));

        Producto p1 = TestDataFactory.producto("P", new BigDecimal("10.00"), false);
        Producto p2 = TestDataFactory.producto("E", new BigDecimal("10.00"), true);
        productoIdNormal = productoRepository.save(p1).getId();
        productoIdExento = productoRepository.save(p2).getId();
    }

    @Test
    void facturarProductoNormalYExento() throws Exception {
        // Normal: impuesto 19% sobre 20.00 = 3.80 → total 23.80
        mockMvc.perform(post("/api/ventas/facturar")
                .header("Authorization", bearer(userToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"productoId\":" + productoIdNormal + ",\"cantidad\":2}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.subtotal").value(20.00))
            .andExpect(jsonPath("$.impuesto").value(3.80))
            .andExpect(jsonPath("$.total").value(23.80));

        // Exento: impuesto = 0
        mockMvc.perform(post("/api/ventas/facturar")
                .header("Authorization", bearer(userToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"productoId\":" + productoIdExento + ",\"cantidad\":2}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.subtotal").value(20.00))
            .andExpect(jsonPath("$.impuesto").value(0.00))
            .andExpect(jsonPath("$.total").value(20.00));
    }

    @Test
    void facturarProductoInexistenteDa404() throws Exception {
        mockMvc.perform(post("/api/ventas/facturar")
                .header("Authorization", bearer(userToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"productoId\":99999,\"cantidad\":1}"))
            .andExpect(status().isNotFound());
    }
}

