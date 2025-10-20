package com.example.erp.service;

import com.example.erp.domain.*;
import com.example.erp.repository.FacturaRepository;
import com.example.erp.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class FacturaService {
    private final FacturaRepository facturaRepository;
    private final VentaRepository ventaRepository;

    public FacturaService(FacturaRepository facturaRepository, VentaRepository ventaRepository) {
        this.facturaRepository = facturaRepository;
        this.ventaRepository = ventaRepository;
    }

    @Transactional
    public Factura facturarVenta(Long ventaId) {
        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada: " + ventaId));

        Factura f = new Factura();
        f.setVenta(venta);

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal impuestos = BigDecimal.ZERO;

        for (VentaDetalle vd : venta.getItems()) {
            FacturaDetalle fd = new FacturaDetalle();
            fd.setProducto(vd.getProducto());
            fd.setDescripcionProducto(vd.getDescripcionProducto());
            fd.setCantidad(vd.getCantidad());
            fd.setPrecioUnitario(vd.getPrecioUnitario());
            fd.setDescuentoPct(vd.getDescuentoPct());
            fd.setDescuentoValor(vd.getDescuentoValor());
            fd.setImpuestosJson(vd.getImpuestosJson());
            fd.setSubtotal(vd.getSubtotal());
            fd.setImpuestosTotal(vd.getImpuestosTotal());
            fd.setTotalLinea(vd.getTotalLinea());
            f.addItem(fd);

            subtotal = subtotal.add(fd.getSubtotal());
            impuestos = impuestos.add(fd.getImpuestosTotal());
        }

        f.setSubtotal(subtotal);
        f.setImpuestoTotal(impuestos);
        f.setTotal(subtotal.add(impuestos));
        return facturaRepository.save(f);
    }
}

