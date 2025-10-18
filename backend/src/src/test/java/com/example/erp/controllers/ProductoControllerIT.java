package com.example.erp.controllers;

import com.example.erp.BaseIntegrationTest;
import com.example.erp.entities.Usuario;
import com.example.erp.repositories.UsuarioRepository;
import com.example.erp.support.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductoControllerIT extends BaseIntegrationTest {

    @Autowired UsuarioRepository usuarioRepository;
    @Autowired PasswordEncoder passwordEncoder;

    private String adminToken;
    private String userToken;

    @BeforeEach
    void setup() {
        usuarioRepository.deleteAll();
        Usuario admin = TestDataFactory.admin("admin", "pass", passwordEncoder);
        Usuario user = TestDataFactory.user("bob", "pass", passwordEncoder);
        usuarioRepository.save(admin);
        usuarioRepository.save(user);
        adminToken = tokenFor("admin", java.util.List.of("ROLE_ADMIN"));
        userToken = tokenFor("bob", java.util.List.of("ROLE_USER"));
    }

    @Test
    void getRequiresAuth() throws Exception {
        mockMvc.perform(get("/api/productos"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void userCanGetButCannotWrite() throws Exception {
        mockMvc.perform(get("/api/productos").header("Authorization", bearer(userToken)))
            .andExpect(status().isOk());

        mockMvc.perform(post("/api/productos")
                .header("Authorization", bearer(userToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"P\",\"precio\":100}"))
            .andExpect(status().isForbidden());
    }

    @Test
    void adminCrudAndValidation() throws Exception {
        // invalid create
        mockMvc.perform(post("/api/productos")
                .header("Authorization", bearer(adminToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"\",\"precio\":-1}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.fieldErrors.nombre").exists())
            .andExpect(jsonPath("$.fieldErrors.precio").exists());

        // valid create
        String body = "{\"nombre\":\"Prod\",\"precio\":100,\"exentoImpuesto\":false}";
        mockMvc.perform(post("/api/productos")
                .header("Authorization", bearer(adminToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.nombre").value("Prod"));
    }
}

