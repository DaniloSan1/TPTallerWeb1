package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.UsuarioNoEncontradoException;
import org.springframework.stereotype.Service;

@Service
public interface ServicioLogin {

    Usuario consultarUsuario(String email, String password);
    void registrar(Usuario usuario) throws UsuarioNoEncontradoException;
    Usuario buscarPorEmail(String email);

}
