package com.tallerwebi.dominio;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tallerwebi.dominio.excepcion.EquipoNoEncontrado;
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
    private final ServicioPartidoEquipo servicioPartidoEquipo;
    private final RepositorioGoles repositorioGoles;

    @Autowired
    public ServicioPartidoImpl(RepositorioPartido repoPartido, RepositorioReserva repoReserva,
            RepositorioUsuario repoUsuario, ServicioEquipoJugador servicioEquipoJugador, ServicioEquipo servicioEquipo,
            RepositorioPartidoEquipo repoPartidoEquipo, ServicioPartidoEquipo servicioPartidoEquipo,
            RepositorioGoles repoGoles) {
        this.repoPartido = repoPartido;
        this.repoReserva = repoReserva;
        this.repoUsuario = repoUsuario;
        this.servicioEquipoJugador = servicioEquipoJugador;
        this.servicioEquipo = servicioEquipo;
        this.repoPartidoEquipo = repoPartidoEquipo;
        this.servicioPartidoEquipo = servicioPartidoEquipo;
        this.repositorioGoles = repoGoles;

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
        String defaultInsignia = "https://taller-web-1-416711641372-us-east-2.s3.us-east-2.amazonaws.com/insignia-default.png";
        Equipo equipo1 = servicioEquipo.crearEquipo("Equipo 1", "Equipo generado para " + partido.getTitulo(),
                defaultInsignia, usuario, TipoEquipo.PUBLICO);
        Equipo equipo2 = servicioEquipo.crearEquipo("Equipo 2", "Equipo generado para " + partido.getTitulo(),
                defaultInsignia, usuario, TipoEquipo.PUBLICO);

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
    public List<Partido> listarPorParticipante(Usuario usuario) {
        if (usuario == null || usuario.getId() == null) {
            return java.util.Collections.emptyList();
        }
        List<Partido> partidos = repoPartido.listarPorParticipante(usuario.getId());
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
    @Transactional
    public void finalizarPartido(Partido partido, List<Gol> goles, Usuario usuario) {

        Partido partidobdd = repoPartido.porId(partido.getId());
        if (partidobdd == null) {
            throw new RuntimeException("El partido con ID " + partido.getId() + " no fue encontrado.");
        }

        if (goles != null && !goles.isEmpty()) {
            for (Gol g : goles) {
                g.setPartido(partidobdd);
                repositorioGoles.guardar(g);
            }
            servicioPartidoEquipo.actualizarGolesPorEquipo(partidobdd);
        }

        Map<Long, Integer> golesPorEquipo = new HashMap<>();
        Equipo equipoGanador = null;
        int maxGoles = -1;
        int cantidadMaximos = 0;
        Long equipoGanadorId = null;

        if (goles != null) {
            for (Gol g : goles) {
                Equipo equipo = g.getEquipoJugador().getEquipo();
                if (equipo != null) {
                    Long equipoId = equipo.getId();
                    int cantidad = g.getCantidad();
                    golesPorEquipo.merge(equipoId, cantidad, Integer::sum);
                }
            }
        }

        for (Map.Entry<Long, Integer> entry : golesPorEquipo.entrySet()) {
            int totalGoles = entry.getValue();

            if (totalGoles > maxGoles) {
                maxGoles = totalGoles;
                cantidadMaximos = 1;
                equipoGanadorId = entry.getKey();
            } else if (totalGoles == maxGoles && maxGoles > -1) {
                cantidadMaximos++;
            }
        }

        // CORRECCIÓN: Buscamos el objeto Equipo por ID usando el servicio
        if (cantidadMaximos == 1 && equipoGanadorId != null) {
            try {
                equipoGanador = servicioEquipo.buscarPorId(equipoGanadorId);
            } catch (EquipoNoEncontrado ignored) {
                // Si falla la búsqueda (EquipoNoEncontrado), el ganador sigue siendo null
            }
        } else {
            equipoGanador = null;
        }

        partidobdd.setEquipoGanador(equipoGanador);
        partidobdd.setFechaFinalizacion(LocalDateTime.now());

        repoPartido.actualizar(partidobdd);
    }

    @Override
    public List<Partido> listarPorEquipoConInfoCancha(Equipo equipo) {
        return repoPartido.listarPorEquipoConInfoCancha(equipo.getId());
    }

    @Override
    public List<Partido> listarTodos(String busqueda, Zona filtroZona, Nivel filtroNivel,
            java.time.LocalDate fechaFiltro, Long canchaId) {
        return repoPartido.listar(busqueda, filtroZona, filtroNivel, fechaFiltro, canchaId);
    }

    @Override
    public List<Partido> partidosTerminadosDelUsuario(Long usuarioId) {
        return repoPartido.partidosTerminadosDelUsuario(usuarioId);
    }

    @Override
    public List<Partido> partidosGanadosDelUsuario(Long usuarioId) {
        return repoPartido.partidosGanadosDelUsuario(usuarioId);
    }

    @Override
    public List<Partido> listarTodos(String busqueda, Zona filtroZona, Nivel filtroNivel) {
        return repoPartido.listar(busqueda, filtroZona, filtroNivel, null, null);
    }
}
