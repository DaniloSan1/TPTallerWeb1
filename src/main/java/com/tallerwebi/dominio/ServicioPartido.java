package com.tallerwebi.dominio;

import java.util.List;

import com.tallerwebi.dominio.excepcion.NoExisteElUsuario;
import com.tallerwebi.dominio.excepcion.NoHayCupoEnPartido;
import com.tallerwebi.dominio.excepcion.PartidoNoEncontrado;
import com.tallerwebi.dominio.excepcion.YaExisteElParticipante;

public interface ServicioPartido {
    List<Partido> listarTodos();

    List<Partido> buscar(Zona zona, Nivel nivel, boolean soloConCupo);

    Partido crearDesdeReserva(Long reservaId, String titulo, String descripcion,
            Zona zona, Nivel nivel, int cupoMaximo, String username);

    Partido obtenerPorId(Long id);

    void anotarParticipante(Long partidoId, String username)
            throws NoExisteElUsuario, NoHayCupoEnPartido, PartidoNoEncontrado, YaExisteElParticipante;
}
