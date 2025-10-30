package com.tallerwebi.dominio;

public interface RepositorioPago {
    void guardarPago(Pago pago);
    Pago buscarPagoPorId(Long id);
    Pago obtenerPorPreferencia(String preferenceId);
    void actualizarPago(Pago pago);
}
