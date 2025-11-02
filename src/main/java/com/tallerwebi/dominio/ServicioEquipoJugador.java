package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;

@Service
public interface ServicioEquipoJugador {
    EquipoJugador crearEquipoJugador(Equipo equipo, Usuario jugador);
}