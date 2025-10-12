package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioPartido {
    List<Partido> listarTodos();
    List<Partido> buscar(Zona zona, Nivel nivel, boolean soloConCupo);

    Partido crearDesdeReserva(Long reservaId, String titulo, String descripcion,
            Zona zona, Nivel nivel, int cupoMaximo, String username);

    Partido obtenerPorId(Long id);

}
