package com.example.erp.services;

import com.example.erp.entities.Factura;
import com.example.erp.entities.Producto;
import com.example.erp.entities.Venta;
import com.example.erp.exceptions.ResourceNotFoundException;
import com.example.erp.repositories.FacturaRepository;
import com.example.erp.repositories.VentaRepository;
import com.example.erp.repositories.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final FacturaRepository facturaRepository;
    private final ProductoRepository productoRepository;
    private final CalculoImpuestoService calculoImpuestoService;

    public VentaService(VentaRepository ventaRepository,
                        FacturaRepository facturaRepository,
                        ProductoRepository productoRepository,
                        CalculoImpuestoService calculoImpuestoService) {
        this.ventaRepository = ventaRepository;
        this.facturaRepository = facturaRepository;
        this.productoRepository = productoRepository;
        this.calculoImpuestoService = calculoImpuestoService;
    }

    public List<Venta> findAll() { return ventaRepository.findAll(); }

    public Page<Venta> findAll(Pageable pageable) { return ventaRepository.findAll(pageable); }

    public Venta findById(Long id) {
        return ventaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada: " + id));
    }

    public List<Factura> findAllFacturas() { return facturaRepository.findAll(); }

    public Page<Factura> findAllFacturas(Pageable pageable) { return facturaRepository.findAll(pageable); }

    public Factura findFacturaById(Long id) {
        return facturaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada: " + id));
    }

    @Transactional
    public Factura facturar(Long productoId, Integer cantidad, BigDecimal precioUnitario) {
        Producto p = productoRepository.findById(productoId)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + productoId));
        Venta v = new Venta();
        v.setProducto(p);
        v.setCantidad(cantidad != null && cantidad > 0 ? cantidad : 1);
        v.setPrecioUnitario(precioUnitario != null ? precioUnitario : p.getPrecio());
        return facturar(v);
    }

    @Transactional
    public Factura facturar(Venta v) {
        Producto p = v.getProducto();
        if (v.getPrecioUnitario() == null && p != null) {
            v.setPrecioUnitario(p.getPrecio());
        }
        BigDecimal subtotal = v.getPrecioUnitario()
            .multiply(new BigDecimal(v.getCantidad()))
            .setScale(2, RoundingMode.HALF_UP);
        BigDecimal impuesto = calculoImpuestoService
            .calcular(subtotal, p != null && p.isExentoImpuesto())
            .setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(impuesto).setScale(2, RoundingMode.HALF_UP);

        v = ventaRepository.save(v);

        Factura f = new Factura();
        f.setVenta(v);
        f.setSubtotal(subtotal);
        f.setImpuesto(impuesto);
        f.setTotal(total);
        return facturaRepository.save(f);
    }
}
