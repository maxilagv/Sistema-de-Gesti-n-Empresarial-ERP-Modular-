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

class UsuarioControllerIT extends BaseIntegrationTest {

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
    void onlyAdminCanListAndCreate() throws Exception {
        mockMvc.perform(get("/api/usuarios").header("Authorization", bearer(userToken)))
            .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/usuarios").header("Authorization", bearer(adminToken)))
            .andExpect(status().isOk());

        mockMvc.perform(post("/api/usuarios")
                .header("Authorization", bearer(adminToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"newuser\",\"password\":\"12345678\",\"email\":\"new@ex.com\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("newuser"))
            .andExpect(jsonPath("$.email").value("new@ex.com"))
            .andExpect(jsonPath("$.password").doesNotExist());
    }
}

