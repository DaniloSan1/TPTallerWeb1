package com.tallerwebi.dominio;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
@Service
public interface ServicioPago {
    
    String crearPago(String titulo, String descripcion, BigDecimal monto, Long reservaId) throws Exception;
    void guardarPago(Reserva reserva, Usuario usuario, String preferenciaId, Double monto);
    Pago obtenerPorPreferencia(String preferenceId);
    void actualizarPago(Pago pago);
}
