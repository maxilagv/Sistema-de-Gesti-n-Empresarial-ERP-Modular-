package com.example.erp.dto.producto;

import java.math.BigDecimal;

public class ProductoResponse {
    private Long id;
    private String nombre;
    private BigDecimal precio;
    private boolean exentoImpuesto;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public boolean isExentoImpuesto() { return exentoImpuesto; }
    public void setExentoImpuesto(boolean exentoImpuesto) { this.exentoImpuesto = exentoImpuesto; }
}

