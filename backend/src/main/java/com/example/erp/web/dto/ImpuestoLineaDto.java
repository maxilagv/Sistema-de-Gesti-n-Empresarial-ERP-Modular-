package com.example.erp.web.dto;

import java.math.BigDecimal;

public class ImpuestoLineaDto {
    private String codigo; // ej: IVA
    private BigDecimal tasa; // 0.12 = 12%
    private Boolean incluido; // true si el precioUnitario ya incluye el impuesto

    public ImpuestoLineaDto() {}

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public BigDecimal getTasa() { return tasa; }
    public void setTasa(BigDecimal tasa) { this.tasa = tasa; }
    public Boolean getIncluido() { return incluido; }
    public void setIncluido(Boolean incluido) { this.incluido = incluido; }
}

