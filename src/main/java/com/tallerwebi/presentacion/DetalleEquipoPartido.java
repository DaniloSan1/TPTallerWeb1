package com.tallerwebi.presentacion;

import java.util.List;
import java.util.stream.Collectors;

import com.tallerwebi.dominio.PartidoEquipo;

public class DetalleEquipoPartido {
    private Long id;
    private Long idEquipo;
    private String nombre;
    private List<DetalleParticipante> participantes;

    public DetalleEquipoPartido(PartidoEquipo equipoEnPartido) {
        this.id = equipoEnPartido.getId();
        this.idEquipo = equipoEnPartido.getEquipo().getId();
        this.nombre = equipoEnPartido.getEquipo().getNombre();
        this.participantes = equipoEnPartido.getEquipo().getJugadores().stream()
                .map(DetalleParticipante::new)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public List<DetalleParticipante> getParticipantes() {
        return participantes;
    }

    public Long getIdEquipo() {
        return idEquipo;
    }
}
