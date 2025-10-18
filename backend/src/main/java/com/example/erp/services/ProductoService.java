package com.example.erp.services;

import com.example.erp.entities.Producto;
import com.example.erp.exceptions.ResourceNotFoundException;
import com.example.erp.repositories.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> findAll() { return productoRepository.findAll(); }

    public Page<Producto> findAll(Pageable pageable) { return productoRepository.findAll(pageable); }

    public Producto findById(Long id) {
        return productoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + id));
    }

    public Producto create(Producto p) { return productoRepository.save(p); }

    public Producto update(Long id, Producto data) {
        Producto p = findById(id);
        if (data.getNombre() != null) p.setNombre(data.getNombre());
        if (data.getPrecio() != null) p.setPrecio(data.getPrecio());
        p.setExentoImpuesto(data.isExentoImpuesto());
        return productoRepository.save(p);
    }

    public void delete(Long id) { productoRepository.delete(findById(id)); }
}
