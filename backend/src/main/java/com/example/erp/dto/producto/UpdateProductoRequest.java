package com.example.erp.dto.producto;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class UpdateProductoRequest {
    private String nombre;
    @Positive
    private BigDecimal precio;
    private Boolean exentoImpuesto;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public Boolean getExentoImpuesto() { return exentoImpuesto; }
    public void setExentoImpuesto(Boolean exentoImpuesto) { this.exentoImpuesto = exentoImpuesto; }
}

