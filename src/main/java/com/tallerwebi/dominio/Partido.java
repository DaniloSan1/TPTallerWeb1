package com.tallerwebi.dominio;

import javax.persistence.*;

import com.tallerwebi.dominio.excepcion.NoHayCupoEnPartido;
import com.tallerwebi.presentacion.DetalleParticipante;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    @OneToMany(mappedBy = "partido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<PartidoEquipo> equipos = new HashSet<>();

    @Column(name = "fecha_finalizacion")
    private LocalDateTime fechaFinalizacion;

    @ManyToOne
    @JoinColumn(name = "equipo_ganador_id")
    private Equipo equipoGanador;

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

    public Set<EquipoJugador> getParticipantes() {
        return this.equipos.stream()
                .flatMap(pe -> pe.getJugadores().stream())
                .collect(Collectors.toSet());
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
        return reserva.getCancha().getCapacidad();
    }

    public int getCupoDisponible() {
        return Math.max(0, getCupoMaximo() - getParticipantes().size());
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

    public boolean validarCupo() throws NoHayCupoEnPartido {
        if (getParticipantes().size() >= this.getCupoMaximo()) {
            throw new NoHayCupoEnPartido();
        }
        return true;
    }

    public boolean validarParticipanteExistente(Long usuarioId) {
        return equipos.stream()
                .flatMap(pe -> pe.getJugadores().stream())
                .anyMatch(jugador -> jugador.getUsuario().getId().equals(usuarioId));
    }

    public boolean validarEquipoExistente(Long equipoId) {
        return equipos.stream()
                .anyMatch(pe -> pe.getEquipo().getId().equals(equipoId));
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    @Deprecated
    public void setUsuario(Usuario usuario) {
        // Para mantener compatibilidad con código existente
        this.setCreador(usuario);
    }

    public boolean esCreador(String email) {
        return creador.getEmail().equals(email);
    }

    public boolean partidoActivo() {
        return this.reserva != null && this.reserva.getActiva();
    }

    public Set<PartidoEquipo> getEquipos() {
        return equipos;
    }

    public void setEquipos(Set<PartidoEquipo> equipos) {
        this.equipos = equipos;
    }

    public void agregarParticipante(EquipoJugador equipoJugador) {
        this.equipos.stream()
                .filter(pe -> pe.getEquipo().getId().equals(equipoJugador.getEquipo().getId()))
                .findFirst()
                .ifPresent(pe -> pe.getJugadores().add(equipoJugador));
    }

    public EquipoJugador buscarJugador(Long id2) {
        for (PartidoEquipo pe : this.equipos) {
            for (EquipoJugador ej : pe.getJugadores()) {
                if (ej.getUsuario().getId().equals(id2)) {
                    return ej;
                }
            }
        }
        return null;
    }

    public LocalDateTime getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(LocalDateTime fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public boolean getFinalizado() {
        return fechaFinalizacion != null;
    }

    public Equipo getEquipoGanador() {
    return equipoGanador;
    }

    public void setEquipoGanador(Equipo equipoGanador) {
    this.equipoGanador = equipoGanador;
    }
}
