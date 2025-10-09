package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioCancha {
    List <Cancha> obtenerCancha();
    boolean reservarCancha(Long id);
    boolean cancelarCancha(Long id);
} 