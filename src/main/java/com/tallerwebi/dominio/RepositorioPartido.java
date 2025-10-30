package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioPartido {

    Partido porId(Long id);

    void guardar(Partido partido);

    List<Partido> listar(String busqueda, Zona filtroZona, Nivel filtroNivel);

    void actualizar(Partido partido);
}
