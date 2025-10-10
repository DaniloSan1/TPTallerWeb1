package com.tallerwebi.dominio;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
public class Cancha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String direccion;
    private Integer capacidad;
    private String tipoSuelo;
    private Boolean disponible = true;
    @Enumerated(EnumType.STRING)
    private Zona zona;
    @OneToMany(mappedBy = "cancha", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Horario> horarios = new ArrayList<>();
  
    public Cancha() {
    }

    public Cancha(String nombre,String direccion, Boolean disponible, Integer capacidad, String tipoSuelo, Zona zona) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.disponible = disponible;
        this.capacidad = capacidad;
        this.tipoSuelo = tipoSuelo;
        this.zona = zona;
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

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public String getTipoSuelo() {
        return tipoSuelo;
    }

    public void setTipoSuelo(String tipoSuelo) {
        this.tipoSuelo = tipoSuelo;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
    public List<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
        actualizarDisponibilidadSegunHorarios();
    }

    public void addHorario(Horario h) {
        horarios.add(h);
        h.setCancha(this);
        actualizarDisponibilidadSegunHorarios();
    }

    public void removeHorario(Horario h) {
        horarios.remove(h);
        h.setCancha(null);
        actualizarDisponibilidadSegunHorarios();
    }
    
    public void actualizarDisponibilidadSegunHorarios() {
        boolean tieneHorarioDisponible = horarios.stream()
                .anyMatch(h -> Boolean.TRUE.equals(h.getDisponible()));
        this.disponible = tieneHorarioDisponible;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
