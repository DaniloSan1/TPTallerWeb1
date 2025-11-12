package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.UsuarioExistenteException;
import com.tallerwebi.dominio.excepcion.UsuarioNoEncontradoException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ServicioUsuario {
    Usuario buscarPorEmailYPassword(String email, String password) throws UsuarioNoEncontradoException;

    void registrarUsuario(Usuario usuario) throws UsuarioNoEncontradoException, UsuarioExistenteException;

    Usuario buscarPorEmail(String email);

    void modificarUsuario(Usuario usuario);

    Usuario buscarPorId(Long id);

    Usuario buscarPorUsername(String username);

    List<Usuario> filtrarPorUsername(String username);

    List<Usuario> listarAmigosDeUsuario(Usuario usuario);
}
