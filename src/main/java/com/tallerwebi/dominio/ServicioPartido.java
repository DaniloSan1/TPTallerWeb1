package com.tallerwebi.dominio;

import java.util.List;

import com.tallerwebi.dominio.excepcion.NoHayCupoEnPartido;
import com.tallerwebi.dominio.excepcion.YaExisteElParticipante;

public interface ServicioPartido {

        Partido obtenerPorId(Long id);

        public void abandonarPartido(Long partidoId, Usuario usuario);

        Partido anotarParticipante(Partido partido, Equipo equipo, Usuario usuario)
                        throws YaExisteElParticipante, NoHayCupoEnPartido;

        Partido crearDesdeReserva(Reserva nuevaReserva, String titulo, String descripcion, Nivel nivel,
                        int cupoMaximo, Usuario usuario);

        List<Partido> listarTodos(String busqueda, Zona filtroZona, Nivel filtroNivel);
        
        public void actualizarPartido(Long id, String titulo, String descripcion, Usuario usuario);
        
        List<Partido> listarPorCreador(Usuario usuario);
    
}
