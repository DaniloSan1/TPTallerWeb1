package com.tallerwebi.dominio.excepcion;

public class NoExisteElUsuario extends RuntimeException {
    public NoExisteElUsuario() {
        super("No existe el usuario");
    }
}
