package com.tallerwebi.dominio;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tallerwebi.dominio.excepcion.NoHayCupoEnPartido;
import com.tallerwebi.dominio.excepcion.PartidoNoEncontrado;
import com.tallerwebi.dominio.excepcion.PermisosInsufficientes;
import com.tallerwebi.dominio.excepcion.YaExisteElParticipante;

@Service
public class ServicioPartidoImpl implements ServicioPartido {
    private final RepositorioPartido repoPartido;
    private final RepositorioReserva repoReserva;
    private final RepositorioUsuario repoUsuario;
    private final ServicioEquipoJugador servicioEquipoJugador;
    private final RepositorioPartidoParticipante repoPartidoParticipante;

    @Autowired
    public ServicioPartidoImpl(RepositorioPartido repoPartido, RepositorioReserva repoReserva,
            RepositorioUsuario repoUsuario, ServicioEquipoJugador servicioEquipoJugador,
            RepositorioPartidoParticipante repoPartidoParticipante) {
        this.repoPartido = repoPartido;
        this.repoReserva = repoReserva;
        this.repoPartidoParticipante = repoPartidoParticipante;
        this.repoUsuario = repoUsuario;
        this.servicioEquipoJugador = servicioEquipoJugador;
    }

    @Override
    public List<Partido> listarTodos(String busqueda, Zona filtroZona, Nivel filtroNivel) {
        return repoPartido.listar(busqueda, filtroZona, filtroNivel);
    }

    @Override
    public Partido crearDesdeReserva(Reserva nuevaReserva, String titulo, String descripcion, Nivel nivel,
            int cupoMaximo, Usuario usuario) {

        Partido partido = new Partido();
        partido.setUsuario(usuario);
        partido.setReserva(nuevaReserva);

        String tituloFinal = (titulo != null && !titulo.isEmpty()) ? titulo
                : "Partido en " + nuevaReserva.getCancha().getNombre();
        partido.setTitulo(tituloFinal);
        partido.setNivel(nivel != null ? nivel : Nivel.INTERMEDIO);
        partido.setDescripcion(descripcion);

        int capacidadCancha = nuevaReserva.getHorario().getCancha().getCapacidad() != null
                ? nuevaReserva.getHorario().getCancha().getCapacidad()
                : 10;
        partido.setCupoMaximo(cupoMaximo > 0 ? cupoMaximo : capacidadCancha);

        repoPartido.guardar(partido);
        return partido;

    }

    @Override
    public Partido obtenerPorId(Long id) {
        Partido partido = repoPartido.porId(id);
        if (partido == null) {
            throw new PartidoNoEncontrado();
        }
        return partido;
    }

    @Override
    @Transactional
    public Partido anotarParticipante(Partido partido, Equipo equipo, Usuario usuario)
            throws YaExisteElParticipante, NoHayCupoEnPartido {

        if (!partido.validarCupo()) {
            throw new NoHayCupoEnPartido();
        }

        EquipoJugador equipoJugador = servicioEquipoJugador.crearEquipoJugador(equipo, usuario);
        partido.agregarParticipante(equipoJugador);
        return partido;
    }

    @Override
    public void abandonarPartido(Long partidoId, Usuario usuario) {
        Partido partido = obtenerPorId(partidoId);
        EquipoJugador equipoJugador = partido.buscarJugador(usuario.getId());
        servicioEquipoJugador.eliminarPorId(equipoJugador.getId());
    }

    @Override
    public void actualizarPartido(Long id, String titulo, String descripcion, Usuario usuario)
            throws PermisosInsufficientes {
        Partido partido = obtenerPorId(id);
        if (!partido.esCreador(usuario.getEmail())) {
            throw new PermisosInsufficientes();
        }
        partido.setTitulo(titulo);
        partido.setDescripcion(descripcion);
        repoPartido.actualizar(partido);
    }
}
