package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioPartido {

    Partido porId(Long id);

    void guardar(Partido partido);

    java.util.List<Partido> listar(String busqueda, Zona filtroZona, Nivel filtroNivel, java.time.LocalDate fechaFiltro, Long canchaId);

    List<Partido> listarPorCreador(Long idCreador);

    List<Partido> listarPorParticipante(Long usuarioId);

    void actualizar(Partido partido);
}
