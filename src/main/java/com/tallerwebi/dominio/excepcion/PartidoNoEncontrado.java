package com.tallerwebi.dominio.excepcion;

public class PartidoNoEncontrado extends RuntimeException {
    public PartidoNoEncontrado() {
        super("No se encontró el partido");
    }
}
