package com.tallerwebi.dominio;

import java.util.List;

import com.tallerwebi.dominio.excepcion.PartidoNoEncontrado;

public interface ServicioPartido {
    List<Partido> listarTodos();
    List<Partido> buscar(Zona zona, Nivel nivel, boolean soloConCupo);

    Partido crearDesdeReserva(Long reservaId, String titulo, String descripcion, 
                              Zona zona, Nivel nivel, int cupoMaximo);
    Partido obtenerPorId(Long id);

}
