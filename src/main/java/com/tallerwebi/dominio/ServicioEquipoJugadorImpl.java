package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}