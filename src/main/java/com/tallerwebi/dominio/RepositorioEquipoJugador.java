package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioEquipoJugador {
    void guardar(EquipoJugador equipoJugador);

    EquipoJugador buscarPorId(Long id);

    EquipoJugador buscarPorEquipoYUsuario(Equipo equipo, Usuario usuario);

    void eliminar(EquipoJugador equipoJugador);

    List<EquipoJugador> buscarPorEquipo(Equipo equipo);
}