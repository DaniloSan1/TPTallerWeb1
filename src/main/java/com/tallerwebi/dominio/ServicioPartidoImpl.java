package com.tallerwebi.dominio;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tallerwebi.dominio.excepcion.NoExisteElUsuario;
import com.tallerwebi.dominio.excepcion.NoHayCupoEnPartido;
import com.tallerwebi.dominio.excepcion.PartidoNoEncontrado;
import com.tallerwebi.dominio.excepcion.YaExisteElParticipante;

@Service
public class ServicioPartidoImpl implements ServicioPartido {
    private final RepositorioPartido repoPartido;
    private final RepositorioReserva repoReserva;
    private final RepositorioUsuario repoUsuario;
    private final RepositorioPartidoParticipante repoPartidoParticipante;

    @Autowired
    public ServicioPartidoImpl(RepositorioPartido repoPartido, RepositorioReserva repoReserva,
            RepositorioUsuario repoUsuario, RepositorioPartidoParticipante repoPartidoParticipante) {
        this.repoPartido = repoPartido;
        this.repoReserva = repoReserva;
        this.repoPartidoParticipante = repoPartidoParticipante;
        this.repoUsuario = repoUsuario;
    }

    @Override
    public List<Partido> listarTodos(String busqueda, Zona filtroZona, Nivel filtroNivel) {
        return repoPartido.listar(busqueda, filtroZona,filtroNivel);
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
    public Partido anotarParticipante(Long partidoId, Usuario usuario)
            throws NoExisteElUsuario, NoHayCupoEnPartido, PartidoNoEncontrado, YaExisteElParticipante {
        Partido partido = repoPartido.porId(partidoId);

        if (partido == null) {
            throw new PartidoNoEncontrado();
        }

        if (!partido.validarCupo()) {
            throw new NoHayCupoEnPartido();
        }

        if (partido.validarParticipanteExistente(usuario.getId())) {
            throw new YaExisteElParticipante();
        }

        PartidoParticipante partidoParticipante = new PartidoParticipante(partido, usuario, Equipo.SIN_EQUIPO);
        repoPartidoParticipante.guardar(partidoParticipante);

        partido.getParticipantes().add(partidoParticipante);

        return partido;
    }

    @Override
    @Transactional
    public void abandonarPartido(Long partidoId, Long usuarioId) {
        Partido partido = repoPartido.porId(partidoId);
        if (partido == null) {
            throw new PartidoNoEncontrado();
        }

        PartidoParticipante partidoParticipante = partido.getParticipantes().stream()
                .filter(pp -> pp.getUsuario().getId().equals(usuarioId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("El usuario no está inscripto en este partido"));

        boolean removed = partido.getParticipantes().remove(partidoParticipante);
        // El @Transactional se encarga del guardado automático

    }

    @Override
    public List<Partido> listarPorCreador(Usuario usuario) {
        if (usuario == null || usuario.getId() == null) {
        return java.util.Collections.emptyList();
    }
    return repoPartido.listarPorCreador(usuario.getId());
    }
}
