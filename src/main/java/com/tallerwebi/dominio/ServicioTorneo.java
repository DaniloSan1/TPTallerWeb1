package com.tallerwebi.dominio;

import java.util.List;
import java.time.LocalDate;

public interface ServicioTorneo {
    Torneo crearTorneo(Torneo torneo);

    List<Torneo> listarTorneos();

    List<Torneo> listarTorneosDisponibles();

    boolean existeTorneoEnFecha(Cancha cancha, LocalDate fecha);
    void actualizarTorneo(Torneo torneo);
    Torneo obtenerPorId(Long id);

    void cancelarTorneo(Long id);

}
