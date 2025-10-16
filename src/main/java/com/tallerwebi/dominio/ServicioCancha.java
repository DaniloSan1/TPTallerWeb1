package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioCancha {
    List <Cancha> obtenerCanchasDisponibles();
    Cancha obtenerCanchaPorId(Long id);
} 