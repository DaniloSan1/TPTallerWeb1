package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioCancha {
    List <Cancha> obtenerCancha();
    boolean reservarCancha(Long id, String horario, String usuario);
    boolean cancelarCancha(Long id, String horario);
} 