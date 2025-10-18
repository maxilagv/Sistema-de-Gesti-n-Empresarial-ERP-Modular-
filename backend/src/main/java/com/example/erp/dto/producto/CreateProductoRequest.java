package com.example.erp.dto.producto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class CreateProductoRequest {
    @NotBlank
    private String nombre;
    @NotNull
    @Positive
    private BigDecimal precio;
    private boolean exentoImpuesto;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public boolean isExentoImpuesto() { return exentoImpuesto; }
    public void setExentoImpuesto(boolean exentoImpuesto) { this.exentoImpuesto = exentoImpuesto; }
}

