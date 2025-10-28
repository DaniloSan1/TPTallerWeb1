package com.tallerwebi.dominio;

public interface RepositorioPartidoParticipante {
    PartidoParticipante buscarPorId(Long id);

    void guardar(PartidoParticipante partidoParticipante);
}
