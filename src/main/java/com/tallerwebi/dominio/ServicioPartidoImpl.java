package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;

import com.tallerwebi.dominio.excepcion.PartidoNoEncontrado;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
public class ServicioPartidoImpl implements ServicioPartido {
    private final RepositorioPartido repoPartido;
    private final RepositorioReserva repoReserva;
    private final RepositorioUsuario repoUsuario;

    public ServicioPartidoImpl(RepositorioPartido repoPartido, RepositorioReserva repoReserva,
            RepositorioUsuario repoUsuario) {
        this.repoPartido = repoPartido;
        this.repoReserva = repoReserva;
        this.repoUsuario = repoUsuario;
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
    public Partido crearDesdeReserva(Long reservaId, String titulo, String descripcion,
            Zona zona, Nivel nivel, int cupoMaximo, String username) {
        Reserva reserva = (reservaId != null) ? repoReserva.porId(reservaId) : null;

        if (reserva == null) {
            return null;
        }

        LocalDateTime fecha = reserva.getFechaHoraInicio();
        if (fecha == null) {
            return null;
        }

        Usuario usuario = repoUsuario.buscar(username);

        Partido p = new Partido(null, titulo, descripcion, zona, nivel, cupoMaximo, reserva, usuario);
        repoPartido.guardar(p);
        return p;
    }

    @Override
    public Partido obtenerPorId(Long id) {
        Partido partido = repoPartido.porId(id);
        if (partido == null) {
            throw new PartidoNoEncontrado();
        }
        return partido;
    }
}
