package com.example.erp.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "factura_detalle")
public class FacturaDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "desc_producto", length = 255)
    private String descripcionProducto;

    @Column(precision = 19, scale = 3, nullable = false)
    private BigDecimal cantidad = BigDecimal.ONE;

    @Column(name = "precio_unitario", precision = 19, scale = 2, nullable = false)
    private BigDecimal precioUnitario = BigDecimal.ZERO;

    @Column(name = "descuento_pct", precision = 5, scale = 2)
    private BigDecimal descuentoPct;

    @Column(name = "descuento_valor", precision = 19, scale = 2)
    private BigDecimal descuentoValor;

    @Column(name = "impuestos_json", columnDefinition = "TEXT")
    private String impuestosJson;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "impuestos_total", precision = 19, scale = 2, nullable = false)
    private BigDecimal impuestosTotal = BigDecimal.ZERO;

    @Column(name = "total_linea", precision = 19, scale = 2, nullable = false)
    private BigDecimal totalLinea = BigDecimal.ZERO;

    public FacturaDetalle() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Factura getFactura() { return factura; }
    public void setFactura(Factura factura) { this.factura = factura; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
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
    public String getImpuestosJson() { return impuestosJson; }
    public void setImpuestosJson(String impuestosJson) { this.impuestosJson = impuestosJson; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getImpuestosTotal() { return impuestosTotal; }
    public void setImpuestosTotal(BigDecimal impuestosTotal) { this.impuestosTotal = impuestosTotal; }
    public BigDecimal getTotalLinea() { return totalLinea; }
    public void setTotalLinea(BigDecimal totalLinea) { this.totalLinea = totalLinea; }
}

