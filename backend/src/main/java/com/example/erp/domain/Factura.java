package com.example.erp.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "factura")
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id")
    private Venta venta; // opcional: factura directa vs desde venta

    @Column(nullable = false)
    private OffsetDateTime fechaEmision = OffsetDateTime.now();

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "impuesto_total", precision = 19, scale = 2, nullable = false)
    private BigDecimal impuestoTotal = BigDecimal.ZERO;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(length = 20, nullable = false)
    private String estado = "EMITIDA"; // EMITIDA, ANULADA

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FacturaDetalle> items = new ArrayList<>();

    public Factura() {}

    public void addItem(FacturaDetalle d) {
        d.setFactura(this);
        this.items.add(d);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Venta getVenta() { return venta; }
    public void setVenta(Venta venta) { this.venta = venta; }
    public OffsetDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(OffsetDateTime fechaEmision) { this.fechaEmision = fechaEmision; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getImpuestoTotal() { return impuestoTotal; }
    public void setImpuestoTotal(BigDecimal impuestoTotal) { this.impuestoTotal = impuestoTotal; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public List<FacturaDetalle> getItems() { return items; }
    public void setItems(List<FacturaDetalle> items) { this.items = items; }
}

