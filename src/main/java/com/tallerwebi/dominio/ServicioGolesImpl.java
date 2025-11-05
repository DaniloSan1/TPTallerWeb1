package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ServicioGolesImpl implements ServicioGoles {

    private RepositorioGoles repositorioGoles;

    private ServicioPartido servicioPartido;

    private ServicioPartidoEquipo servicioPartidoEquipo;

    @Autowired
    public ServicioGolesImpl(RepositorioGoles repositorioGoles, ServicioPartido servicioPartido,
            ServicioPartidoEquipo servicioPartidoEquipo) {
        this.repositorioGoles = repositorioGoles;
        this.servicioPartido = servicioPartido;
        this.servicioPartidoEquipo = servicioPartidoEquipo;
    }

    @Override
    @Transactional
    public void registrarGoles(Partido partido, List<Gol> goles, Usuario usuario) {
        servicioPartido.finalizarPartido(partido, usuario);

        for (Gol gol : goles) {
            repositorioGoles.guardar(gol);
        }
        // Actualizar goles por equipo
        servicioPartidoEquipo.actualizarGolesPorEquipo(partido);
    }
}