package com.tallerwebi.presentacion;

public class DetalleAmigoLista {
    private Long id;
    private String nombre;
    private String apellido;
    private String username;

    public DetalleAmigoLista() {
    }

    public DetalleAmigoLista(Long id, String nombre, String apellido, String username) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.username = username;
    }

    // Getters and setters
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}