package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;

@Service
public interface ServicioUsuario {
Usuario buscarPorEmailYPassword(String email, String password);
void registrarUsuario(Usuario usuario);
Usuario buscarPorEmail(String email);
void modificarUsuario(Usuario usuario);
}
