package com.example.erp.controllers;

import com.example.erp.dto.factura.FacturaResponse;
import com.example.erp.entities.Factura;
import com.example.erp.services.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    private final VentaService ventaService;

    public FacturaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    @Operation(summary = "Listar facturas paginadas")
    public ResponseEntity<Page<FacturaResponse>> listar(Pageable pageable) {
        return ResponseEntity.ok(ventaService.findAllFacturas(pageable).map(this::toResponse));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener factura por id")
    public ResponseEntity<FacturaResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(ventaService.findFacturaById(id)));
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
