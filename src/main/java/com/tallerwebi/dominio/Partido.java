package com.tallerwebi.dominio;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Partido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    @Enumerated(EnumType.STRING)
    private Zona zona;
    @Enumerated(EnumType.STRING)
    private Nivel nivel;
    private int cupoMaximo;
    private String descripcion;
    @ManyToOne
    @JoinColumn(name = "reserva_id")
    private Reserva reserva;
    @ManyToOne
    @JoinColumn(name = "creador_id")
    private Usuario creador;

    @OneToMany(mappedBy = "partido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<PartidoParticipante> participantes = new HashSet<>();

    @OneToMany(mappedBy = "partido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PartidoEquipo> equipos = new HashSet<>();

    // Constructor por defecto para JPA
    public Partido() {
    }

    public Partido(Long id, String titulo, String descripcion, Nivel nivel,
            int cupoMaximo,
            Reserva reserva, Usuario creador) {
        this.titulo = titulo;
        this.nivel = nivel;
        this.cupoMaximo = cupoMaximo;
        this.reserva = reserva;
        this.creador = creador;
        this.descripcion = descripcion;
    }

    // helpers para manejar participantes
    public Set<PartidoParticipante> getParticipantes() {
        return participantes;
    }

    public Set<Long> getParticipantesIds() {
        return participantes.stream()
                .map(pp -> pp.getUsuario().getId())
                .collect(java.util.stream.Collectors.toSet());
    }

    public Reserva getReserva() {
        return reserva;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Zona getZona() {
        return reserva.getCancha().getZona();
    }

    public Nivel getNivel() {
        return nivel;
    }

    public LocalDateTime getFecha() {
        return reserva.getFechaHoraInicio();
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public int getCupoDisponible() {
        return Math.max(0, cupoMaximo - participantes.size());
    }

    public boolean tieneCupo() {
        return getCupoDisponible() > 0;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Cancha getCancha() {
        return reserva.getCancha();
    }

    public Usuario getCreador() {
        return creador;
    }

    public void setCreador(Usuario creador) {
        this.creador = creador;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public void setCupoMaximo(int cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }

    public boolean validarCupo() {
        return participantes.size() < cupoMaximo;
    }

    public boolean validarParticipanteExistente(Long usuarioId) {
        return participantes.stream()
                .anyMatch(pp -> pp.getUsuario().getId().equals(usuarioId));
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public void setUsuario(Usuario usuario) {
        this.creador = usuario;
    }

    public boolean esCreador(String email) {
        return creador.getEmail().equals(email);
    }

    public boolean partidoActivo() {
        return this.reserva != null && this.reserva.getActiva();
    }

    public int cuposDisponibles() {
        return this.cupoMaximo - this.participantes.size();
    }

    public Set<PartidoEquipo> getEquipos() {
        return equipos;
    }

    public void setEquipos(Set<PartidoEquipo> equipos) {
        this.equipos = equipos;
    }
}
