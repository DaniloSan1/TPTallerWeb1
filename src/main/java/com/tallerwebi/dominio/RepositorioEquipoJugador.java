package com.tallerwebi.dominio;

public interface RepositorioEquipoJugador {
    void guardar(EquipoJugador equipoJugador);

    EquipoJugador buscarPorId(Long id);
}