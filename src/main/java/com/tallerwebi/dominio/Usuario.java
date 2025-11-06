package com.tallerwebi.dominio;

import java.util.List;

import javax.persistence.*;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;

    @Column(unique = true)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;
    private String rol;
    private String posicionFavorita;
    private Boolean activo = false;
    private Double calificacionPromedio = 0.0;
    private Integer totalCalificaciones = 0;

    @Column(nullable = true)
    private String fotoPerfil;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReseniaCancha> reseñasCanchas;

    public Usuario() {
    }

    public Usuario(String nombre, String password, String email, String username) {
        this.nombre = nombre;
        this.password = password;
        this.email = email;
        this.rol = "ROLE_USER";
        this.activo = true;
        this.username = username;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPosicionFavorita() {
        return posicionFavorita;
    }

    public void setPosicionFavorita(String posicionFavorita) {
        this.posicionFavorita = posicionFavorita;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getNombreCompleto() {
        return this.nombre.trim() + " " + this.apellido.trim();
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public boolean activo() {
        return activo;
    }

    public void activar() {
        activo = true;
    }

    public Double getCalificacionPromedio() {
        return calificacionPromedio;
    }

    public void setCalificacionPromedio(Double calificacionPromedio) {
        this.calificacionPromedio = calificacionPromedio;
    }

    public Integer getTotalCalificaciones() {
        return totalCalificaciones;
    }

    public void setTotalCalificaciones(Integer totalCalificaciones) {
        this.totalCalificaciones = totalCalificaciones;
    }
    
    public List<ReseniaCancha> getReseñasCanchas() {
        return reseñasCanchas;
    }
    public void setReseñasCanchas(List<ReseniaCancha> reseñasCanchas) {
        this.reseñasCanchas = reseñasCanchas;
    }
}
