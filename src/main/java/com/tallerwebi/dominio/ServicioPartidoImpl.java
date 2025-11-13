package com.tallerwebi.dominio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ServicioEquipo servicioEquipo;
    private final RepositorioPartidoEquipo repoPartidoEquipo;

    @Autowired
    public ServicioPartidoImpl(RepositorioPartido repoPartido, RepositorioReserva repoReserva,
            RepositorioUsuario repoUsuario, ServicioEquipoJugador servicioEquipoJugador, ServicioEquipo servicioEquipo,
            RepositorioPartidoEquipo repoPartidoEquipo) {
        this.repoPartido = repoPartido;
        this.repoReserva = repoReserva;
        this.repoUsuario = repoUsuario;
        this.servicioEquipoJugador = servicioEquipoJugador;
        this.servicioEquipo = servicioEquipo;
        this.repoPartidoEquipo = repoPartidoEquipo;
    }

    @Override
    public List<Partido> listarTodos(String busqueda, Zona filtroZona, Nivel filtroNivel) {
        return repoPartido.listar(busqueda, filtroZona, filtroNivel);
    }

    @Override
    public Partido crearDesdeReserva(Reserva nuevaReserva, String titulo, String descripcion, Nivel nivel,
            int cupoMaximo, Usuario usuario) {

        Partido partido = new Partido();
        partido.setCreador(usuario);
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

        // Crear dos equipos por defecto
        String defaultInsignia = "https://www.ligaprofesional.ar/wp-content/uploads/2024/06/BOC-escudo.png";
        Equipo equipo1 = servicioEquipo.crearEquipo("Equipo 1", "Equipo generado para " + partido.getTitulo(),
                defaultInsignia, usuario);
        Equipo equipo2 = servicioEquipo.crearEquipo("Equipo 2", "Equipo generado para " + partido.getTitulo(),
                defaultInsignia, usuario);

        // Crear las relaciones PartidoEquipo
        PartidoEquipo partidoEquipo1 = new PartidoEquipo(partido, equipo1);
        PartidoEquipo partidoEquipo2 = new PartidoEquipo(partido, equipo2);

        repoPartidoEquipo.guardar(partidoEquipo1);
        repoPartidoEquipo.guardar(partidoEquipo2);

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
    public Partido obtenerPorIdConJugadores(Long id) {
        Partido partido = repoPartido.obtenerPorIdConJugadores(id);
        if (partido == null) {
            throw new PartidoNoEncontrado();
        }
        return partido;
    }

    @Override
    @Transactional
    public Partido anotarParticipante(Partido partido, Equipo equipo, Usuario usuario)
            throws YaExisteElParticipante, NoHayCupoEnPartido {

        partido.validarCupo();

        EquipoJugador equipoJugador = servicioEquipoJugador.crearEquipoJugador(equipo, usuario);
        partido.agregarParticipante(equipoJugador);

        return partido;
    }

    @Override
    public void abandonarPartido(Long partidoId, Usuario usuario) {
        Partido partido = obtenerPorIdConJugadores(partidoId);
        EquipoJugador equipoJugador = partido.buscarJugador(usuario.getId());
        servicioEquipoJugador.eliminarPorId(equipoJugador.getId());
    }

    @Override
    public List<Partido> listarPorCreador(Usuario usuario) {
        if (usuario == null || usuario.getId() == null) {
            System.out.println("Usuario nulo o sin ID");
            return java.util.Collections.emptyList();
        }
        System.out.println("Buscando partidos para usuario ID: " + usuario.getId());
        List<Partido> partidos = repoPartido.listarPorCreador(usuario.getId());
        System.out.println("Partidos encontrados: " + (partidos != null ? partidos.size() : "null"));
        return partidos != null ? partidos : java.util.Collections.emptyList();
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

    @Override
    public void finalizarPartido(Partido partido, Usuario usuario) throws PermisosInsufficientes {
        if (!partido.esCreador(usuario.getEmail())) {
            throw new PermisosInsufficientes();
        }
        partido.setFechaFinalizacion(java.time.LocalDateTime.now());
        repoPartido.actualizar(partido);
    }

    @Override
    public List<Partido> listarPorEquipoConInfoCancha(Equipo equipo) {
        return repoPartido.listarPorEquipoConInfoCancha(equipo.getId());
    }
}
