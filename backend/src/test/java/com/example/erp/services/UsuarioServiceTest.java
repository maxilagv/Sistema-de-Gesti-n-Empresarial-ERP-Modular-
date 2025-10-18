package com.example.erp.services;

import com.example.erp.entities.Usuario;
import com.example.erp.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock UsuarioRepository usuarioRepository;
    @Mock PasswordEncoder passwordEncoder;
    @InjectMocks UsuarioService usuarioService;

    @Test
    void createEncodesPassword() {
        Usuario u = new Usuario();
        u.setUsername("bob");
        u.setPassword("secret");
        u.setEmail("bob@example.com");
        when(passwordEncoder.encode("secret")).thenReturn("ENC(secret)");
        when(usuarioRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Usuario saved = usuarioService.create(u);
        assertEquals("ENC(secret)", saved.getPassword());
    }
}

