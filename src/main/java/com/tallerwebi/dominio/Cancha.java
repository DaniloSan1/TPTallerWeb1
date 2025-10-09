package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
public class Cancha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Integer capacidad;
    private String tipoSuelo;
    private Boolean disponible = true;
    @Enumerated(EnumType.STRING)
    private Zona zona;

    // Constructor por defecto para JPA
    public Cancha() {
    }

    public Cancha(String nombre, Boolean disponible, Integer capacidad, String tipoSuelo, Zona zona) {
        this.nombre = nombre;
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
}