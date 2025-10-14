package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioHorario {
    Horario obtenerPorId(Long id);
    List<Horario> obtenerPorCancha(Cancha cancha);
    List<Horario> obtenerDisponiblesPorCancha(Cancha cancha);
}
