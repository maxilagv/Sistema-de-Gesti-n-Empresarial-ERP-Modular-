package com.example.erp.controllers;

import com.example.erp.dto.factura.FacturaResponse;
import com.example.erp.dto.venta.VentaRequest;
import com.example.erp.dto.venta.VentaResponse;
import com.example.erp.entities.Factura;
import com.example.erp.entities.Venta;
import com.example.erp.services.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ventas")
@Validated
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    @Operation(summary = "Listar ventas paginadas")
    public ResponseEntity<Page<VentaResponse>> listar(Pageable pageable) {
        return ResponseEntity.ok(ventaService.findAll(pageable).map(this::toResponse));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener venta por id")
    public ResponseEntity<VentaResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(ventaService.findById(id)));
    }

    @PostMapping("/facturar")
    @Operation(summary = "Generar factura a partir de una venta")
    public ResponseEntity<FacturaResponse> facturar(@Valid @RequestBody VentaRequest req) {
        Factura f = ventaService.facturar(req.getProductoId(), req.getCantidad(), req.getPrecioUnitario());
        return ResponseEntity.ok(toResponse(f));
    }

    private VentaResponse toResponse(Venta v) {
        VentaResponse r = new VentaResponse();
        r.setId(v.getId());
        r.setFecha(v.getFecha());
        if (v.getProducto() != null) {
            r.setProductoId(v.getProducto().getId());
        }
        r.setCantidad(v.getCantidad());
        r.setPrecioUnitario(v.getPrecioUnitario());
        return r;
    }

    private FacturaResponse toResponse(Factura f) {
        FacturaResponse r = new FacturaResponse();
        r.setId(f.getId());
        if (f.getVenta() != null) r.setVentaId(f.getVenta().getId());
        r.setSubtotal(f.getSubtotal());
        r.setImpuesto(f.getImpuesto());
        r.setTotal(f.getTotal());
        return r;
    }
}
