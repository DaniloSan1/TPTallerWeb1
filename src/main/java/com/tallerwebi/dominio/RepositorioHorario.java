package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioHorario {
    Horario obtenerPorId(Long id);
    List<Horario> obtenerPorCancha(Cancha cancha);
    List<Horario> obtenerDisponiblesPorCancha(Cancha cancha);
    void cambiarDisponibilidad(Long id, Boolean disponible);
}