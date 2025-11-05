package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;

import com.tallerwebi.dominio.excepcion.ParticipanteNoEncontrado;
import com.tallerwebi.dominio.excepcion.YaExisteElParticipante;

@Service
public interface ServicioEquipoJugador {
    EquipoJugador crearEquipoJugador(Equipo equipo, Usuario jugador) throws YaExisteElParticipante;

    EquipoJugador buscarPorEquipoYUsuario(Equipo equipo, Usuario usuario);

    void eliminarPorId(Long id);

    EquipoJugador actualizarEquipo(long partidoParticipanteId, Equipo nuevoEquipo) throws ParticipanteNoEncontrado;

    void promoverCapitan(Long equipoJugadorId) throws ParticipanteNoEncontrado;
}