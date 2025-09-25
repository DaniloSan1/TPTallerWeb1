package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicioUsuarioImpl implements ServicioUsuario {
    private final RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioUsuarioImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public Usuario buscarPorEmailYPassword(String email, String password) {
        return repositorioUsuario.buscarUsuario(email, password);
    }

    @Override
    public void registrarUsuario(Usuario usuario) {
        if(repositorioUsuario.buscar(usuario.getEmail()) != null) {
            throw new RuntimeException("Usuario ya existe");
        }
        repositorioUsuario.guardar(usuario);
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        return repositorioUsuario.buscar(email);
    }

    @Override
    public void modificarUsuario(Usuario usuario) {
    repositorioUsuario.modificar(usuario);
    }
}
