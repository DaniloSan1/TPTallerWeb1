package com.tallerwebi.dominio.excepcion;

public class UsernameExistenteException extends RuntimeException {
    public UsernameExistenteException() {
        super("El username ya existe");
    }
}
