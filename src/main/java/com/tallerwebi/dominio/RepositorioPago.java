package com.tallerwebi.dominio;

public interface RepositorioPago {
    void guardarPago(Pago pago);
    Pago buscarPagoPorId(Long id);
}
