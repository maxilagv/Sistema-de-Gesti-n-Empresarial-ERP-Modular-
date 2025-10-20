package com.example.erp.web.dto;

import jakarta.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;

public class VentaRequest {
    private Long clienteId; // opcional
    private String moneda; // opcional
    private BigDecimal tasaCambio; // opcional

    @NotEmpty
    private List<VentaItemRequest> items;

    public VentaRequest() {}

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
    public BigDecimal getTasaCambio() { return tasaCambio; }
    public void setTasaCambio(BigDecimal tasaCambio) { this.tasaCambio = tasaCambio; }
    public List<VentaItemRequest> getItems() { return items; }
    public void setItems(List<VentaItemRequest> items) { this.items = items; }
}

