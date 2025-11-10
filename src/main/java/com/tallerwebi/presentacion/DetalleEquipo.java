package com.tallerwebi.presentacion;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.tallerwebi.dominio.Equipo;
import com.tallerwebi.dominio.EquipoJugador;
import com.tallerwebi.dominio.Partido;
import com.tallerwebi.dominio.Usuario;

public class DetalleEquipo {
    private Long id;
    private String titulo;
    private String descripcion;
    private LocalDate fechaCreacion;
    private String insigniaUrl;
    private List<DetalleParticipante> listaJugadores;
    private List<DetallePartidoLista> listaPartidos;

    public DetalleEquipo(Equipo equipo, List<Partido> partidos, List<EquipoJugador> jugadores, Usuario usuario) {
        this.id = equipo.getId();
        this.titulo = equipo.getNombre();
        this.descripcion = equipo.getDescripcion();
        this.fechaCreacion = equipo.getFechaCreacion().toLocalDate();
        this.insigniaUrl = equipo.getInsigniaUrl();
        listaJugadores = jugadores.stream().map(DetalleParticipante::new)
                .collect(Collectors.toList());
        listaPartidos = partidos.stream()
                .map(partido -> new DetallePartidoLista(partido, usuario)).collect(Collectors.toList());

    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public String getInsigniaUrl() {
        return insigniaUrl;
    }

    public List<DetalleParticipante> getListaJugadores() {
        return listaJugadores;
    }

    public List<DetallePartidoLista> getListaPartidos() {
        return listaPartidos;
    }
}
