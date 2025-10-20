package com.example.erp.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venta")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(nullable = false)
    private OffsetDateTime fecha = OffsetDateTime.now();

    @Column(length = 3)
    private String moneda; // ISO 4217, opcional por ahora

    @Column(name = "tasa_cambio", precision = 19, scale = 6)
    private BigDecimal tasaCambio; // opcional por ahora

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "descuento_total", precision = 19, scale = 2, nullable = false)
    private BigDecimal descuentoTotal = BigDecimal.ZERO;

    @Column(name = "impuesto_total", precision = 19, scale = 2, nullable = false)
    private BigDecimal impuestoTotal = BigDecimal.ZERO;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(length = 20, nullable = false)
    private String estado = "CREADA"; // CREADA, CONFIRMADA, ANULADA

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VentaDetalle> items = new ArrayList<>();

    public Venta() {}

    public void addItem(VentaDetalle d) {
        d.setVenta(this);
        this.items.add(d);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public OffsetDateTime getFecha() { return fecha; }
    public void setFecha(OffsetDateTime fecha) { this.fecha = fecha; }
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
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public List<VentaDetalle> getItems() { return items; }
    public void setItems(List<VentaDetalle> items) { this.items = items; }
}

