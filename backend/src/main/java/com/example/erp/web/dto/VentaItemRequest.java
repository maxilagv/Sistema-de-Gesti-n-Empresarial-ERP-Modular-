package com.example.erp.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class VentaItemRequest {
    @NotNull
    private Long productoId;

    @NotNull @DecimalMin("1")
    private BigDecimal cantidad;

    private BigDecimal precioUnitario; // opcional, si null se usa precio producto

    private BigDecimal descuentoPct; // 0..100 opcional

    private BigDecimal descuentoValor; // opcional

    private List<ImpuestoLineaDto> impuestos; // opcional

    private String moneda; // opcional
    private BigDecimal tasaCambio; // opcional

    public VentaItemRequest() {}

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    public BigDecimal getDescuentoPct() { return descuentoPct; }
    public void setDescuentoPct(BigDecimal descuentoPct) { this.descuentoPct = descuentoPct; }
    public BigDecimal getDescuentoValor() { return descuentoValor; }
    public void setDescuentoValor(BigDecimal descuentoValor) { this.descuentoValor = descuentoValor; }
    public List<ImpuestoLineaDto> getImpuestos() { return impuestos; }
    public void setImpuestos(List<ImpuestoLineaDto> impuestos) { this.impuestos = impuestos; }
    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
    public BigDecimal getTasaCambio() { return tasaCambio; }
    public void setTasaCambio(BigDecimal tasaCambio) { this.tasaCambio = tasaCambio; }
}
