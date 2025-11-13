package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioInscripcion {
    InscripcionTorneo inscribirEquipo(Long torneoId, Long equipoId);
    List<InscripcionTorneo> listarInscripcionesPorTorneo(Long torneoId);
    void cancelarInscripcion(Long inscripcionId);
}
