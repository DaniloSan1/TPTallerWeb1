package com.tallerwebi.dominio;

public interface RepositorioPartidoEquipo {
    void guardar(PartidoEquipo partidoEquipo);

    PartidoEquipo buscarPorId(Long id);
}