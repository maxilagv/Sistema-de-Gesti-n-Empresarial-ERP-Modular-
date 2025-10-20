package com.example.catalogo.web;

import com.example.catalogo.domain.Producto;
import com.example.catalogo.repository.ProductoRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    private final ProductoRepository repo;
    public ProductoController(ProductoRepository repo) { this.repo = repo; }

    @GetMapping
    public Page<Producto> listar(Pageable pageable) { return repo.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtener(@PathVariable @Min(1) Long id) {
        Optional<Producto> p = repo.findById(id);
        return p.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@Valid @RequestBody Producto p) {
        Producto saved = repo.save(p);
        return ResponseEntity.created(URI.create("/productos/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @Valid @RequestBody Producto p) {
        return repo.findById(id)
                .map(db -> {
                    db.setNombre(p.getNombre());
                    db.setPrecio(p.getPrecio());
                    db.setExentoImpuesto(p.isExentoImpuesto());
                    return ResponseEntity.ok(repo.save(db));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

