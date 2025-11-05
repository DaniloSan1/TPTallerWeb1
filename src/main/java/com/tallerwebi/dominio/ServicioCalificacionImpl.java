package com.tallerwebi.dominio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicioCalificacionImpl implements ServicioCalificacion{
    
    private final RepositorioCalificacion repositorioCalificacion;
    
    @Autowired
    public ServicioCalificacionImpl(RepositorioCalificacion repositorioCalificacion) {
        this.repositorioCalificacion = repositorioCalificacion;
    }
    @Override
    public void calificarJugador(Usuario calificador, Usuario calificado, Partido partido, Integer puntuacion, String comentario) {
        if(repositorioCalificacion.existeCalificacion(calificador.getId(), calificado.getId(), partido.getId())){
            throw new IllegalArgumentException("Ya existe una calificacion para este jugador");
        }
        Calificacion calificacion = new Calificacion(calificador, calificado, partido, puntuacion, comentario);
        repositorioCalificacion.guardarCalificacion(calificacion);
    }
    @Override
    public List<Calificacion> obtenerCalificacionesPorPartido(Long partidoId) {
        return repositorioCalificacion.obtenerPorPartido(partidoId);
    }
}
