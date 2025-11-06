package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.PartidoEquipo;

public class EquipoSimple {
    private Long id;
    private String nombre;

    public EquipoSimple(PartidoEquipo partidoEquipo) {
        this.id = partidoEquipo.getEquipo().getId();
        this.nombre = partidoEquipo.getEquipo().getNombre();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}