package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioUsuario {

    List<Usuario> filtrarPorUsername(String username);
    Usuario buscarUsuario(String email, String password);
    void guardar(Usuario usuario);
    Usuario buscar(String email);
    void modificar(Usuario usuario);
    Usuario buscarPorId(Long id);
    Usuario buscarPorUsername(String username);
}

