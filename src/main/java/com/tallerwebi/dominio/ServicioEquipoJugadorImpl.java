package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicioEquipoJugadorImpl implements ServicioEquipoJugador {
    private final RepositorioEquipoJugador repositorioEquipoJugador;

    @Autowired
    public ServicioEquipoJugadorImpl(RepositorioEquipoJugador repositorioEquipoJugador) {
        this.repositorioEquipoJugador = repositorioEquipoJugador;
    }

    @Override
    public EquipoJugador crearEquipoJugador(Equipo equipo, Usuario jugador) {
        EquipoJugador equipoJugador = new EquipoJugador(equipo, jugador);
        repositorioEquipoJugador.guardar(equipoJugador);
        return equipoJugador;
    }
}