package com.tallerwebi.dominio;

import java.util.List;

import org.springframework.stereotype.Service;
@Service
public interface ServicioCalificacion {
    public void calificarJugador(Usuario calificador, Usuario calificado, Partido partido, Integer puntuacion, String comentario);
    public List<Calificacion> obtenerCalificacionesPorPartido(Long partidoId);
    double calcularCalificacionPromedioUsuario(Long usuarioId);
    List<Calificacion>obtenerCalificacionesPorCalificador(Long usuarioId);
}
