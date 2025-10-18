package com.example.erp.services;

import com.example.erp.entities.Usuario;
import com.example.erp.exceptions.ResourceNotFoundException;
import com.example.erp.repositories.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> findAll() { return usuarioRepository.findAll(); }

    public Page<Usuario> findAll(Pageable pageable) { return usuarioRepository.findAll(pageable); }

    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + id));
    }

    public Usuario create(Usuario u) {
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        return usuarioRepository.save(u);
    }

    public Usuario update(Long id, String email, String rawPassword, String role, Boolean enabled) {
        Usuario u = findById(id);
        if (email != null) u.setEmail(email);
        if (rawPassword != null) u.setPassword(passwordEncoder.encode(rawPassword));
        if (role != null) u.setRole(role);
        if (enabled != null) u.setEnabled(enabled);
        return usuarioRepository.save(u);
    }

    public void delete(Long id) { usuarioRepository.delete(findById(id)); }
}
