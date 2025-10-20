package com.example.erp.web;

import com.example.erp.domain.Venta;
import com.example.erp.domain.VentaDetalle;
import com.example.erp.service.VentaService;
import com.example.erp.repository.VentaRepository;
import com.example.erp.web.dto.VentaItemResponse;
import com.example.erp.web.dto.VentaRequest;
import com.example.erp.web.dto.VentaResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/ventas")
public class VentaController {
    private final VentaService ventaService;
    private final VentaRepository ventaRepository;

    public VentaController(VentaService ventaService, VentaRepository ventaRepository) {
        this.ventaService = ventaService;
        this.ventaRepository = ventaRepository;
    }

    @PostMapping
    public ResponseEntity<VentaResponse> crear(@Valid @RequestBody VentaRequest request) {
        Venta venta = ventaService.crearVenta(request);
        return ResponseEntity.ok(toResponse(venta));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponse> obtener(@PathVariable Long id) {
        Optional<Venta> v = ventaRepository.findById(id);
        return v.map(venta -> ResponseEntity.ok(toResponse(venta)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private VentaResponse toResponse(Venta v) {
        VentaResponse r = new VentaResponse();
        r.setId(v.getId());
        r.setEstado(v.getEstado());
        r.setMoneda(v.getMoneda());
        r.setTasaCambio(v.getTasaCambio());
        r.setSubtotal(v.getSubtotal());
        r.setDescuentoTotal(v.getDescuentoTotal());
        r.setImpuestoTotal(v.getImpuestoTotal());
        r.setTotal(v.getTotal());
        List<VentaItemResponse> items = v.getItems().stream().map(this::toItem).collect(Collectors.toList());
        r.setItems(items);
        return r;
    }

    private VentaItemResponse toItem(VentaDetalle d) {
        VentaItemResponse r = new VentaItemResponse();
        r.setProductoId(d.getProducto() != null ? d.getProducto().getId() : null);
        r.setDescripcionProducto(d.getDescripcionProducto());
        r.setCantidad(d.getCantidad());
        r.setPrecioUnitario(d.getPrecioUnitario());
        r.setDescuentoPct(d.getDescuentoPct());
        r.setDescuentoValor(d.getDescuentoValor());
        r.setSubtotal(d.getSubtotal());
        r.setImpuestosTotal(d.getImpuestosTotal());
        r.setTotalLinea(d.getTotalLinea());
        return r;
    }
}

