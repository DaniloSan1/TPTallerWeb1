package com.tallerwebi.dominio;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tallerwebi.dominio.excepcion.ParticipanteNoEncontrado;
import com.tallerwebi.dominio.excepcion.YaExisteElParticipante;

@Service
public class ServicioEquipoJugadorImpl implements ServicioEquipoJugador {
    private final RepositorioEquipoJugador repositorioEquipoJugador;

    @Autowired
    public ServicioEquipoJugadorImpl(RepositorioEquipoJugador repositorioEquipoJugador) {
        this.repositorioEquipoJugador = repositorioEquipoJugador;
    }

    @Override
    public EquipoJugador crearEquipoJugador(Equipo equipo, Usuario jugador) throws YaExisteElParticipante {
        if (equipo.yaExisteJugador(jugador.getId())) {
            throw new YaExisteElParticipante(); // TODO: Agregar test unitario para esta excepcion
        }

        EquipoJugador equipoJugador = new EquipoJugador(equipo, jugador);
        repositorioEquipoJugador.guardar(equipoJugador);

        return equipoJugador;
    }

    @Override
    public void eliminarPorId(Long id) {
        EquipoJugador equipoJugador = repositorioEquipoJugador.buscarPorId(id);
        repositorioEquipoJugador.eliminar(equipoJugador);
    }

    @Override
    public EquipoJugador buscarPorEquipoYUsuario(Equipo equipo, Usuario usuario) {
        return repositorioEquipoJugador.buscarPorEquipoYUsuario(equipo, usuario);
    }

    @Override
    public EquipoJugador buscarPorId(Long id) {
        return repositorioEquipoJugador.buscarPorId(id);
    }

    @Override
    @Transactional
    public EquipoJugador actualizarEquipo(long partidoParticipanteId, Equipo nuevoEquipo)
            throws ParticipanteNoEncontrado {
        EquipoJugador participante = repositorioEquipoJugador.buscarPorId(partidoParticipanteId);
        if (participante == null) {
            throw new ParticipanteNoEncontrado();
        }
        participante.setEquipo(nuevoEquipo);
        return participante;
    }

    @Override
    @Transactional
    public void promoverCapitan(Long equipoJugadorId) throws ParticipanteNoEncontrado {
        EquipoJugador nuevoCapitan = repositorioEquipoJugador.buscarPorId(equipoJugadorId);
        if (nuevoCapitan == null) {
            throw new ParticipanteNoEncontrado();
        }

        Equipo equipo = nuevoCapitan.getEquipo();
        List<EquipoJugador> jugadores = repositorioEquipoJugador.buscarPorEquipo(equipo);

        // Unset all captains in the equipo
        for (EquipoJugador jugador : jugadores) {
            if (jugador.isEsCapitan()) {
                jugador.setEsCapitan(false);
            }
        }

        // Set the new captain
        nuevoCapitan.setEsCapitan(true);
    }

    @Override
    public List<EquipoJugador> buscarPorEquipo(Equipo equipo) {
        return repositorioEquipoJugador.buscarPorEquipo(equipo);
    }
}