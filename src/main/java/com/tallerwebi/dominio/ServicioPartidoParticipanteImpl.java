package com.tallerwebi.dominio;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tallerwebi.dominio.excepcion.ParticipanteNoEncontrado;

@Service
public class ServicioPartidoParticipanteImpl implements ServicioPartidoParticipante {
    private RepositorioPartidoParticipante repositorioPartidoParticipante;

    @Autowired
    public ServicioPartidoParticipanteImpl(@Autowired RepositorioPartidoParticipante repositorioPartidoParticipante) {
        this.repositorioPartidoParticipante = repositorioPartidoParticipante;
    }

    @Override
    @Transactional
    public PartidoParticipante actualizarEquipo(long partidoParticipanteId, String nuevoEquipo)
            throws ParticipanteNoEncontrado {
        PartidoParticipante participante = repositorioPartidoParticipante.buscarPorId(partidoParticipanteId);
        if (participante == null) {
            throw new ParticipanteNoEncontrado();
        }
        participante.setEquipo(EquipoEnum.valueOf(nuevoEquipo));
        return participante;
    }

    @Override
    public void eliminar(long partidoParticipanteId) throws ParticipanteNoEncontrado {
        PartidoParticipante participante = repositorioPartidoParticipante.buscarPorId(partidoParticipanteId);
        if (participante == null) {
            throw new ParticipanteNoEncontrado();
        }
        repositorioPartidoParticipante.eliminar(participante);
    }
}
