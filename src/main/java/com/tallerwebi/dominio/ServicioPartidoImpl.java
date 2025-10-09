package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
public class ServicioPartidoImpl implements ServicioPartido {
    private final RepositorioPartido repoPartido;
    private final RepositorioReserva repoReserva;

    public ServicioPartidoImpl(RepositorioPartido repoPartido, RepositorioReserva repoReserva) {
        this.repoPartido = repoPartido;
        this.repoReserva = repoReserva;
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
                                     Zona zona, Nivel nivel, int cupoMaximo) {
         Reserva r = (reservaId != null) ? repoReserva.porId(reservaId) : null;
        if (r == null) {
            return null;
        }
        LocalDateTime fecha = r.getFechaHoraInicio();
        if (fecha == null) {
            return null;
        }

        Partido p = new Partido(null, titulo, zona, nivel, fecha, Math.max(0, cupoMaximo));
        p.setDescripcion(descripcion);
        repoPartido.guardar(p);
        return p;
    }
}
