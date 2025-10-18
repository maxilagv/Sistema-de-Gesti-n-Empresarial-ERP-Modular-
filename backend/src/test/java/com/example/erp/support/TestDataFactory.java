package com.example.erp.support;

import com.example.erp.entities.Producto;
import com.example.erp.entities.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

public class TestDataFactory {
    public static Usuario admin(String username, String rawPassword, PasswordEncoder encoder) {
        Usuario u = new Usuario();
        u.setUsername(username);
        u.setPassword(encoder.encode(rawPassword));
        u.setEmail(username + "@example.com");
        u.setRole("ADMIN");
        u.setEnabled(true);
        return u;
    }

    public static Usuario user(String username, String rawPassword, PasswordEncoder encoder) {
        Usuario u = new Usuario();
        u.setUsername(username);
        u.setPassword(encoder.encode(rawPassword));
        u.setEmail(username + "@example.com");
        u.setRole("USER");
        u.setEnabled(true);
        return u;
    }

    public static Producto producto(String nombre, BigDecimal precio, boolean exento) {
        Producto p = new Producto();
        p.setNombre(nombre);
        p.setPrecio(precio);
        p.setExentoImpuesto(exento);
        return p;
    }
}

