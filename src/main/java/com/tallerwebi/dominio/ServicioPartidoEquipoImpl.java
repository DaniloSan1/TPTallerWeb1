package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ServicioPartidoEquipoImpl implements ServicioPartidoEquipo {

    private RepositorioGoles repositorioGoles;
    private RepositorioPartidoEquipo repositorioPartidoEquipo;

    @Autowired
    public ServicioPartidoEquipoImpl(RepositorioGoles repositorioGoles,
            RepositorioPartidoEquipo repositorioPartidoEquipo) {
        this.repositorioGoles = repositorioGoles;
        this.repositorioPartidoEquipo = repositorioPartidoEquipo;
    }

    @Override
    public void actualizarGolesPorEquipo(Partido partido) {
        List<Gol> golesPartido = repositorioGoles.buscarPorPartido(partido);
        Map<Long, Integer> golesPorEquipo = golesPartido.stream()
                .collect(Collectors.groupingBy(
                        gol -> gol.getEquipoJugador().getEquipo().getId(),
                        Collectors.summingInt(Gol::getCantidad)));
        for (PartidoEquipo pe : partido.getEquipos()) {
            Integer totalGoles = golesPorEquipo.getOrDefault(pe.getEquipo().getId(), 0);
            pe.setGoles(totalGoles);
            repositorioPartidoEquipo.guardar(pe);
        }
    }
}