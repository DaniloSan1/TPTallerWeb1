package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioInscripcionTorneo {
    void guardar(InscripcionTorneo inscripcionTorneo);
    InscripcionTorneo buscarPorId(Long id);
    void eliminar(InscripcionTorneo inscripcionTorneo);
    List<InscripcionTorneo> buscarPorTorneo(Long idTorneo);
}
