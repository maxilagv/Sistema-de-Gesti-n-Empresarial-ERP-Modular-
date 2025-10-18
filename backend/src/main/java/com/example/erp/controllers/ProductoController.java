package com.example.erp.controllers;

import com.example.erp.dto.producto.CreateProductoRequest;
import com.example.erp.dto.producto.ProductoResponse;
import com.example.erp.dto.producto.UpdateProductoRequest;
import com.example.erp.entities.Producto;
import com.example.erp.services.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/productos")
@Validated
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    @Operation(summary = "Listar productos paginados")
    public ResponseEntity<Page<ProductoResponse>> listar(Pageable pageable) {
        Page<Producto> page = productoService.findAll(pageable);
        return ResponseEntity.ok(page.map(this::toResponse));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por id")
    public ResponseEntity<ProductoResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(productoService.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Crear producto")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody CreateProductoRequest req) {
        Producto p = new Producto();
        p.setNombre(req.getNombre());
        p.setPrecio(req.getPrecio());
        p.setExentoImpuesto(req.isExentoImpuesto());
        return ResponseEntity.ok(toResponse(productoService.create(p)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoResponse> actualizar(@PathVariable Long id, @Valid @RequestBody UpdateProductoRequest req) {
        Producto data = new Producto();
        data.setNombre(req.getNombre());
        data.setPrecio(req.getPrecio());
        if (req.getExentoImpuesto() != null) data.setExentoImpuesto(req.getExentoImpuesto());
        return ResponseEntity.ok(toResponse(productoService.update(id, data)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private ProductoResponse toResponse(Producto p) {
        ProductoResponse r = new ProductoResponse();
        r.setId(p.getId());
        r.setNombre(p.getNombre());
        r.setPrecio(p.getPrecio());
        r.setExentoImpuesto(p.isExentoImpuesto());
        return r;
    }
}
