package com.tallerwebi.dominio.excepcion;

public class UsuarioExistenteException extends RuntimeException {
    public UsuarioExistenteException() {
        super("El usuario ya existe");
    }
    
}
