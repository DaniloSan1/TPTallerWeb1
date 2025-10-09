package com.tallerwebi.dominio;

public interface RepositorioReserva {
    Reserva porId(Long id);
    void guardar(Reserva reserva);
}
