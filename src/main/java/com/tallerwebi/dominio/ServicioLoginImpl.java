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
    public ServicioLoginImpl(RepositorioUsuario repositorioUsuario){
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public Usuario consultarUsuario (String email, String password) {
        return repositorioUsuario.buscarUsuario(email, password);
    }

    @Override
    public void registrar(Usuario usuario) throws UsuarioNoEncontradoException {
        Usuario usuarioEncontrado = repositorioUsuario.buscarUsuario(usuario.getEmail(), usuario.getPassword());
        if(usuarioEncontrado != null){
            throw new UsuarioNoEncontradoException();
        }
        repositorioUsuario.guardar(usuario);
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        Usuario usuarioEncontrado = repositorioUsuario.buscar(email);
        return usuarioEncontrado;
    }

    @Override
    public Usuario buscarPorId(Long id) {
        Usuario usuario = repositorioUsuario.buscarPorId(id);
        return usuario;
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        Usuario usuarioABuscar = repositorioUsuario.buscarPorUsername(username);
        return usuarioABuscar;
    }

}
