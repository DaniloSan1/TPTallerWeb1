package com.tallerwebi.dominio;

import org.hibernate.Session;
import org.springframework.stereotype.Service;

import com.tallerwebi.dominio.excepcion.NoExisteElUsuario;
import com.tallerwebi.dominio.excepcion.NoHayCupoEnPartido;
import com.tallerwebi.dominio.excepcion.PartidoNoEncontrado;
import com.tallerwebi.dominio.excepcion.YaExisteElParticipante;  
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ArrayList;
import java.util.HashSet;

@Service
public class ServicioPartidoImpl implements ServicioPartido {
    private final RepositorioPartido repoPartido;
    private final RepositorioReserva repoReserva;
    private final RepositorioUsuario repoUsuario;
    private final RepositorioPartidoParticipante repoPartidoParticipante;

    public ServicioPartidoImpl(RepositorioPartido repoPartido, RepositorioReserva repoReserva,
            RepositorioUsuario repoUsuario, RepositorioPartidoParticipante repoPartidoParticipante) {
        this.repoPartido = repoPartido;
        this.repoReserva = repoReserva;
        this.repoPartidoParticipante = repoPartidoParticipante;
        this.repoUsuario = repoUsuario;
    }

    @Override
    public List<Partido> listarTodos() {
        return repoPartido.todos();
    }

    @Override
    public List<Partido> buscar(Zona zona, Nivel nivel, boolean soloConCupo) {
        return repoPartido.todos().stream()
                .filter(p -> (zona == null || p.getZona() == zona) &&
                        (nivel == null || p.getNivel() == nivel) &&
                        (!soloConCupo || p.tieneCupo()))
                .collect(Collectors.toList());
    }

    @Override
    public Partido crearDesdeReserva(Reserva nuevaReserva, String titulo, String descripcion,
            Zona zona, Nivel nivel, int cupoMaximo, Usuario usuario) {


        Partido partido = new Partido();
        partido.setUsuario(usuario);
        partido.setReserva(nuevaReserva);

        String tituloFinal = (titulo != null && !titulo.isEmpty()) ? titulo : "Partido en " + nuevaReserva.getCancha().getNombre();
        partido.setTitulo(tituloFinal);
        partido.setZona(zona != null ? zona : Zona.CENTRO);
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
    public void anotarParticipante(Long partidoId, String username)
            throws NoExisteElUsuario, NoHayCupoEnPartido, PartidoNoEncontrado, YaExisteElParticipante {
        Usuario usuario = repoUsuario.buscar(username);
        if (usuario == null) {
            throw new NoExisteElUsuario();
        }

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

        PartidoParticipante partidoParticipante = new PartidoParticipante(partido, usuario);
        repoPartidoParticipante.guardar(partidoParticipante);
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
}
