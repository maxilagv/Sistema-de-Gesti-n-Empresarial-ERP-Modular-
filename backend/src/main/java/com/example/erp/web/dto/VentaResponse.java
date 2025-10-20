package com.example.erp.web.dto;

import java.math.BigDecimal;
import java.util.List;

public class VentaResponse {
    private Long id;
    private String estado;
    private String moneda;
    private BigDecimal tasaCambio;
    private BigDecimal subtotal;
    private BigDecimal descuentoTotal;
    private BigDecimal impuestoTotal;
    private BigDecimal total;
    private List<VentaItemResponse> items;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
    public BigDecimal getTasaCambio() { return tasaCambio; }
    public void setTasaCambio(BigDecimal tasaCambio) { this.tasaCambio = tasaCambio; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getDescuentoTotal() { return descuentoTotal; }
    public void setDescuentoTotal(BigDecimal descuentoTotal) { this.descuentoTotal = descuentoTotal; }
    public BigDecimal getImpuestoTotal() { return impuestoTotal; }
    public void setImpuestoTotal(BigDecimal impuestoTotal) { this.impuestoTotal = impuestoTotal; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public List<VentaItemResponse> getItems() { return items; }
    public void setItems(List<VentaItemResponse> items) { this.items = items; }
}

