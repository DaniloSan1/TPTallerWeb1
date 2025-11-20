package com.tallerwebi.dominio;

import java.util.List;

import com.tallerwebi.dominio.excepcion.NoHayCupoEnPartido;
import com.tallerwebi.dominio.excepcion.PermisosInsufficientes;
import com.tallerwebi.dominio.excepcion.YaExisteElParticipante;

public interface ServicioPartido {

        Partido obtenerPorId(Long id);

        void abandonarPartido(Long partidoId, Usuario usuario);

        Partido anotarParticipante(Partido partido, Equipo equipo, Usuario usuario)
                        throws YaExisteElParticipante, NoHayCupoEnPartido;

        Partido crearDesdeReserva(Reserva nuevaReserva, String titulo, String descripcion, Nivel nivel,
                        int cupoMaximo, Usuario usuario);

        List<Partido> listarTodos(String busqueda, Zona filtroZona, Nivel filtroNivel, java.time.LocalDate fechaFiltro, Long canchaId);
        
        void actualizarPartido(Long id, String titulo, String descripcion, Usuario usuario);

        List<Partido> listarPorParticipante(Usuario usuario);

        void finalizarPartido(Partido partido, List<Gol> goles, Usuario usuario);

        List<Partido> listarTodos(String busqueda, Zona filtroZona, Nivel filtroNivel);

        List<Partido> listarPorCreador(Usuario usuario);

        List<Partido> listarPorEquipoConInfoCancha(Equipo equipo);

        Partido obtenerPorIdConJugadores(Long id);
        
        List<Partido> partidosTerminadosDelUsuario(Long usuarioId);

        List<Partido>partidosGanadosDelUsuario(Long usuarioId);


}
