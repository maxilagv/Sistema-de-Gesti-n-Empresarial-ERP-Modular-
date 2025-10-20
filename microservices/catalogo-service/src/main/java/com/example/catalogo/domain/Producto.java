package com.example.catalogo.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal precio;

    @Column(name = "exento_impuesto", nullable = false)
    private boolean exentoImpuesto = false;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public boolean isExentoImpuesto() { return exentoImpuesto; }
    public void setExentoImpuesto(boolean exentoImpuesto) { this.exentoImpuesto = exentoImpuesto; }
}

