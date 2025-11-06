package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.UsuarioExistenteException;
import com.tallerwebi.dominio.excepcion.UsuarioNoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioUsuarioImpl implements ServicioUsuario {
    private final RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioUsuarioImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public Usuario buscarPorEmailYPassword(String email, String password) throws UsuarioNoEncontradoException {
        if(repositorioUsuario.buscarUsuario(email, password) == null) {
            throw new UsuarioNoEncontradoException();
        }
        return repositorioUsuario.buscarUsuario(email, password);
    }

    @Override
    public void registrarUsuario(Usuario usuario) throws UsuarioExistenteException {
        if(repositorioUsuario.buscar(usuario.getEmail()) != null) {
            throw new UsuarioExistenteException();
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
    @Override
    public Usuario buscarPorId(Long id) {
        return repositorioUsuario.buscarPorId(id);
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        return repositorioUsuario.buscarPorUsername(username);
    }

    @Override
    public List<Usuario> filtrarPorUsername(String username) {
        return repositorioUsuario.filtrarPorUsername(username);
    }
}
