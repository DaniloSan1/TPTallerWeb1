package com.tallerwebi.dominio;

import java.util.List;

import com.tallerwebi.dominio.excepcion.PartidoNoEncontrado;

public interface ServicioPartido {
    List<Partido> buscar(Zona zona, Nivel nivel, boolean soloConCupo);

    Partido obtenerPorId(Long id) throws PartidoNoEncontrado;
}
