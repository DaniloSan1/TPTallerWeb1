package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioCancha {
    Cancha obtenerCanchaPorId(Long id);
    List<Cancha> obtenerCanchasDisponibles(String busqueda, Zona zona, Double precio);
    List<Cancha> obtenerTodasLasCanchas();
    void eliminarCancha(Long id);
    void crearCancha(Cancha cancha);
    Cancha obtenerCanchaConHorariosPorId(Long id);
    void guardar(Cancha cancha);
} 