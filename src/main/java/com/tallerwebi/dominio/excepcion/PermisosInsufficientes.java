package com.tallerwebi.dominio.excepcion;

public class PermisosInsufficientes extends RuntimeException {
    public PermisosInsufficientes() {
        super("El usuario no tiene permisos suficientes para realizar esta acción.");
    }

}
