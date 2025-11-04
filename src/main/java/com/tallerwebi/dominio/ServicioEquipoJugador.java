package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;

import com.tallerwebi.dominio.excepcion.YaExisteElParticipante;

@Service
public interface ServicioEquipoJugador {
    EquipoJugador crearEquipoJugador(Equipo equipo, Usuario jugador) throws YaExisteElParticipante;
}