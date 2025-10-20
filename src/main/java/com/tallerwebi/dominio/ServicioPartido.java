package com.tallerwebi.dominio;

import java.util.List;

import com.tallerwebi.dominio.excepcion.NoExisteElUsuario;
import com.tallerwebi.dominio.excepcion.NoHayCupoEnPartido;
import com.tallerwebi.dominio.excepcion.PartidoNoEncontrado;
import com.tallerwebi.dominio.excepcion.YaExisteElParticipante;

public interface ServicioPartido {
    List<Partido> listarTodos();

    List<Partido> buscar(Zona zona, Nivel nivel, boolean soloConCupo);

    Partido obtenerPorId(Long id);

    public void abandonarPartido(Long partidoId, Long usuarioId);

    Partido anotarParticipante(Long partidoId, Usuario usuario)
            throws NoExisteElUsuario, NoHayCupoEnPartido, PartidoNoEncontrado, YaExisteElParticipante;

    Partido crearDesdeReserva(Reserva nuevaReserva, String titulo, String descripcion, Nivel nivel,
            int cupoMaximo, Usuario usuario);
}
