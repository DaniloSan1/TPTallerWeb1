package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioPartido {

    Partido porId(Long id);

    Partido obtenerPorIdConJugadores(Long id);

    void guardar(Partido partido);

    List<Partido> listar(String busqueda, Zona filtroZona, Nivel filtroNivel, java.time.LocalDate fechaFiltro, Long canchaId);

    List<Partido> listarPorCreador(Long idCreador);

    List<Partido> listarPorParticipante(Long usuarioId);

    void actualizar(Partido partido);

    List<Partido> listarPorEquipoConInfoCancha(Long idEquipo);

    List<Partido> partidosTerminadosDelUsuario(Long usuarioId);

     List<Partido> partidosGanadosDelUsuario(Long usuarioId);


}
