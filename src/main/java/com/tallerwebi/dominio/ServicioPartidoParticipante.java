package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.ParticipanteNoEncontrado;

public interface ServicioPartidoParticipante {
    PartidoParticipante actualizarEquipo(long partidoParticipanteId, String nuevoEquipo)
            throws ParticipanteNoEncontrado;

    void eliminar(long partidoParticipanteId) throws ParticipanteNoEncontrado;
}
