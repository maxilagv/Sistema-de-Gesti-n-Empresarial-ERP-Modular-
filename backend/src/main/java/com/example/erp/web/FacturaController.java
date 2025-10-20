package com.example.erp.web;

import com.example.erp.domain.Factura;
import com.example.erp.service.FacturaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/facturas")
public class FacturaController {
    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @PostMapping
    public ResponseEntity<Factura> facturar(@RequestParam("ventaId") Long ventaId) {
        Factura f = facturaService.facturarVenta(ventaId);
        return ResponseEntity.ok(f);
    }
}

