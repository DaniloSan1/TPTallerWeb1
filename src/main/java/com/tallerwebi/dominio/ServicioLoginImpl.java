package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.UsuarioNoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("servicioLogin")
@Transactional
public class ServicioLoginImpl implements ServicioLogin {

    private RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioLoginImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public Usuario consultarUsuario(String email, String password) {
        return repositorioUsuario.buscarUsuario(email, password);
    }

    @Override
    public void registrar(Usuario usuario) throws UsuarioNoEncontradoException {
        Usuario usuarioEncontrado = repositorioUsuario.buscarUsuario(usuario.getEmail(), usuario.getPassword());
        if (usuarioEncontrado != null) {
            throw new UsuarioNoEncontradoException();
        }
        usuario.setFotoPerfil(
                "https://taller-web-1-416711641372-us-east-2.s3.us-east-2.amazonaws.com/ejemplos/avatar.png");
        repositorioUsuario.guardar(usuario);
    }

    @Override
    public Usuario buscarPorEmail(String email) throws UsuarioNoEncontradoException {
        Usuario usuarioEncontrado = repositorioUsuario.buscar(email);
        if (usuarioEncontrado == null) {
            throw new UsuarioNoEncontradoException();
        }
        return usuarioEncontrado;
    }

    @Override
    public Usuario buscarPorId(Long id) throws UsuarioNoEncontradoException {
        Usuario usuario = repositorioUsuario.buscarPorId(id);
        if (usuario == null) {
            throw new UsuarioNoEncontradoException();
        }
        return usuario;
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        Usuario usuarioABuscar = repositorioUsuario.buscarPorUsername(username);
        return usuarioABuscar;
    }

}
