package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioEquipo {
    void guardar(Equipo equipo);

    Equipo buscarPorId(Long id);

    void modificar(Equipo equipo);

    List<Equipo> buscarEquiposPorUsuario(Usuario usuario);

    List<Equipo> buscarEquiposPorUsuarioYNombre(Usuario usuario, String nombre);
}