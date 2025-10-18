package com.example.erp.repositories;

import com.example.erp.entities.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Autowired UsuarioRepository usuarioRepository;

    @Test
    void findByUsername() {
        Usuario u = new Usuario();
        u.setUsername("eve");
        u.setPassword("x");
        u.setEmail("eve@example.com");
        u.setRole("USER");
        u.setEnabled(true);
        usuarioRepository.save(u);

        Optional<Usuario> found = usuarioRepository.findByUsername("eve");
        assertTrue(found.isPresent());
        assertEquals("eve", found.get().getUsername());
    }
}

