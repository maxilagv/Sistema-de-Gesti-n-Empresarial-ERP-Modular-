package com.example.erp.service;

import com.example.erp.domain.Cliente;
import com.example.erp.domain.Producto;
import com.example.erp.domain.Venta;
import com.example.erp.domain.VentaDetalle;
import com.example.erp.repository.ClienteRepository;
import com.example.erp.repository.ProductoRepository;
import com.example.erp.repository.VentaRepository;
import com.example.erp.util.MoneyUtil;
import com.example.erp.web.dto.ImpuestoLineaDto;
import com.example.erp.web.dto.VentaItemRequest;
import com.example.erp.web.dto.VentaRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VentaService {
    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;

    public VentaService(VentaRepository ventaRepository,
                        ProductoRepository productoRepository,
                        ClienteRepository clienteRepository) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public Venta crearVenta(VentaRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("La venta debe incluir items");
        }

        Venta venta = new Venta();
        venta.setMoneda(request.getMoneda());
        venta.setTasaCambio(request.getTasaCambio());

        if (request.getClienteId() != null) {
            Optional<Cliente> c = clienteRepository.findById(request.getClienteId());
            c.ifPresent(venta::setCliente);
        }

        BigDecimal subtotalVenta = BigDecimal.ZERO;
        BigDecimal impuestoVenta = BigDecimal.ZERO;
        BigDecimal descuentoTotal = BigDecimal.ZERO;

        for (VentaItemRequest itemReq : request.getItems()) {
            Producto producto = productoRepository.findById(itemReq.getProductoId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + itemReq.getProductoId()));

            VentaDetalle det = new VentaDetalle();
            det.setProducto(producto);
            det.setDescripcionProducto(producto.getNombre());
            det.setCantidad(itemReq.getCantidad());

            BigDecimal precioUnit = itemReq.getPrecioUnitario() != null ? itemReq.getPrecioUnitario() : producto.getPrecio();
            det.setPrecioUnitario(MoneyUtil.round2(precioUnit));
            det.setMoneda(itemReq.getMoneda() != null ? itemReq.getMoneda() : request.getMoneda());
            det.setTasaCambio(itemReq.getTasaCambio() != null ? itemReq.getTasaCambio() : request.getTasaCambio());

            // Descuento
            BigDecimal descuentoValor = itemReq.getDescuentoValor();
            if (descuentoValor == null && itemReq.getDescuentoPct() != null) {
                descuentoValor = precioUnit.multiply(itemReq.getDescuentoPct()).divide(BigDecimal.valueOf(100));
            }
            det.setDescuentoPct(itemReq.getDescuentoPct());
            det.setDescuentoValor(MoneyUtil.round2(descuentoValor));

            BigDecimal qty = itemReq.getCantidad();
            BigDecimal bruto = precioUnit.multiply(qty);
            BigDecimal netoAntesImpuesto = descuentoValor != null ? bruto.subtract(descuentoValor) : bruto;

            // Impuestos por línea
            List<ImpuestoLineaDto> impuestos = itemReq.getImpuestos();
            BigDecimal impuestosLinea = BigDecimal.ZERO;
            BigDecimal subtotalLinea = netoAntesImpuesto;

            if (impuestos != null && !impuestos.isEmpty()) {
                // Separar incluidos y excluidos
                BigDecimal tasaIncluidaAcum = BigDecimal.ZERO;
                BigDecimal tasaExcluidaAcum = BigDecimal.ZERO;
                for (ImpuestoLineaDto t : impuestos) {
                    if (Boolean.TRUE.equals(t.getIncluido())) {
                        if (t.getTasa() != null) tasaIncluidaAcum = tasaIncluidaAcum.add(t.getTasa());
                    } else {
                        if (t.getTasa() != null) tasaExcluidaAcum = tasaExcluidaAcum.add(t.getTasa());
                    }
                }
                if (tasaIncluidaAcum.compareTo(BigDecimal.ZERO) > 0) {
                    // extraer base si estaba incluido
                    BigDecimal divisor = BigDecimal.ONE.add(tasaIncluidaAcum);
                    subtotalLinea = netoAntesImpuesto.divide(divisor, 6, java.math.RoundingMode.HALF_EVEN);
                    BigDecimal impuestosIncluidos = netoAntesImpuesto.subtract(subtotalLinea);
                    impuestosLinea = impuestosLinea.add(impuestosIncluidos);
                }
                if (tasaExcluidaAcum.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal impExcl = subtotalLinea.multiply(tasaExcluidaAcum);
                    impuestosLinea = impuestosLinea.add(impExcl);
                }

                impuestosLinea = MoneyUtil.round2(impuestosLinea);
                subtotalLinea = MoneyUtil.round2(subtotalLinea);

                // Guardar JSON simple con tasas
                det.setImpuestosJson(toJsonSimple(impuestos));
            } else {
                // Sin impuestos explícitos: si producto es exento, no se cobra; si no, 0 por ahora
                subtotalLinea = MoneyUtil.round2(netoAntesImpuesto);
                impuestosLinea = BigDecimal.ZERO;
            }

            det.setSubtotal(subtotalLinea);
            det.setImpuestosTotal(impuestosLinea);
            det.setTotalLinea(MoneyUtil.round2(subtotalLinea.add(impuestosLinea)));

            venta.addItem(det);

            subtotalVenta = subtotalVenta.add(det.getSubtotal());
            impuestoVenta = impuestoVenta.add(det.getImpuestosTotal());
            if (descuentoValor != null) descuentoTotal = descuentoTotal.add(MoneyUtil.round2(descuentoValor));
        }

        venta.setSubtotal(MoneyUtil.round2(subtotalVenta));
        venta.setImpuestoTotal(MoneyUtil.round2(impuestoVenta));
        venta.setDescuentoTotal(MoneyUtil.round2(descuentoTotal));
        venta.setTotal(MoneyUtil.round2(venta.getSubtotal().add(venta.getImpuestoTotal())));
        venta.setEstado("CONFIRMADA");

        return ventaRepository.save(venta);
    }

    private String toJsonSimple(List<ImpuestoLineaDto> impuestos) {
        if (impuestos == null || impuestos.isEmpty()) return null;
        List<String> parts = new ArrayList<>();
        for (ImpuestoLineaDto t : impuestos) {
            String codigo = t.getCodigo() != null ? escape(t.getCodigo()) : "";
            String tasa = t.getTasa() != null ? t.getTasa().toPlainString() : "0";
            String inc = String.valueOf(Boolean.TRUE.equals(t.getIncluido()));
            parts.add("{\"codigo\":\"" + codigo + "\",\"tasa\":" + tasa + ",\"incluido\":" + inc + "}");
        }
        return "[" + String.join(",", parts) + "]";
    }

    private String escape(String s) { return s.replace("\\", "\\\\").replace("\"", "\\\""); }
}

