package com.tallerwebi.dominio;

public interface RepositorioEquipo {
    void guardar(Equipo equipo);

    Equipo buscarPorId(Long id);

    void modificar(Equipo equipo);
}