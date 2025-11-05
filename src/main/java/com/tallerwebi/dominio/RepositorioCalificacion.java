package com.tallerwebi.dominio;

import java.util.List;

import org.springframework.stereotype.Repository;
@Repository
public interface RepositorioCalificacion {
    public void guardarCalificacion(Calificacion calificacion);
    public List <Calificacion> obtenerPorPartido(Long idPartido);
    public Boolean existeCalificacion(Long calificadorId, Long calificadoId, Long partidoId);
}
