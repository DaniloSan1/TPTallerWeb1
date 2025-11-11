package com.tallerwebi.dominio;

import java.time.LocalDate;
import java.util.List;

public interface RepositorioTorneo {

    List<Torneo>torneoFuturo(LocalDate fecha);

    Boolean existeCanchaYFecha(Cancha cancha, LocalDate fecha);

    List<Torneo> porCancha(Cancha cancha);
    List<Torneo> porCanchaYFecha(Cancha cancha, LocalDate fecha);
    Torneo porId(Long id);
    void crearTorneo(Torneo torneo);

    List<Torneo> listarTorneos();
}