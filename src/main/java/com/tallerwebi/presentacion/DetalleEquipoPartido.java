package com.tallerwebi.presentacion;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.tallerwebi.dominio.PartidoEquipo;

public class DetalleEquipoPartido {
    private Long id;
    private Long idEquipo;
    private String nombre;
    private Integer goles;
    private List<DetalleParticipante> participantes;
    private String insigniaUrl;

    public DetalleEquipoPartido(PartidoEquipo equipoEnPartido) {
        this.id = equipoEnPartido.getId();
        this.idEquipo = equipoEnPartido.getEquipo().getId();
        this.nombre = equipoEnPartido.getEquipo().getNombre();
        this.insigniaUrl = equipoEnPartido.getEquipo().getInsigniaUrl();
        this.participantes = equipoEnPartido.getEquipo().getJugadores().stream()
                .map(DetalleParticipante::new)
                .sorted(Comparator.comparing(DetalleParticipante::isEsCapitan).reversed()
                        .thenComparing(DetalleParticipante::getFechaUnion))
                .collect(Collectors.toList());
        this.goles = equipoEnPartido.getGoles() != null ? equipoEnPartido.getGoles() : 0;
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

    public Integer getGoles() {
        return goles;
    }

    public String getInsigniaUrl() {
        return insigniaUrl;
    }
}
