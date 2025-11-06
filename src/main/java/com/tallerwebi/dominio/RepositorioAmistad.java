package com.tallerwebi.dominio;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioAmistad {

    void guardar(Amistad amistad);

    Amistad buscarPorUsuarios(Usuario usuario1, Usuario usuario2);

    Amistad buscarPorId(Long id);

    List<Amistad> buscarPendientes(Usuario usuario);

    List<Amistad> buscarAceptadas(Usuario usuario);

}
