package com.example.erp.web.dto;

import java.math.BigDecimal;

public class VentaItemResponse {
    private Long productoId;
    private String descripcionProducto;
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal descuentoPct;
    private BigDecimal descuentoValor;
    private BigDecimal subtotal;
    private BigDecimal impuestosTotal;
    private BigDecimal totalLinea;

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public String getDescripcionProducto() { return descripcionProducto; }
    public void setDescripcionProducto(String descripcionProducto) { this.descripcionProducto = descripcionProducto; }
    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    public BigDecimal getDescuentoPct() { return descuentoPct; }
    public void setDescuentoPct(BigDecimal descuentoPct) { this.descuentoPct = descuentoPct; }
    public BigDecimal getDescuentoValor() { return descuentoValor; }
    public void setDescuentoValor(BigDecimal descuentoValor) { this.descuentoValor = descuentoValor; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getImpuestosTotal() { return impuestosTotal; }
    public void setImpuestosTotal(BigDecimal impuestosTotal) { this.impuestosTotal = impuestosTotal; }
    public BigDecimal getTotalLinea() { return totalLinea; }
    public void setTotalLinea(BigDecimal totalLinea) { this.totalLinea = totalLinea; }
}

