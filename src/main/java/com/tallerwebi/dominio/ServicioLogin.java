package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.springframework.stereotype.Service;

@Service
public interface ServicioLogin {

    Usuario consultarUsuario(String email, String password);
    void registrar(Usuario usuario) throws UsuarioExistente;
    Usuario buscarPorEmail(String email);

}
