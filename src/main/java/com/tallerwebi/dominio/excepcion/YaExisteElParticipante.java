package com.tallerwebi.dominio.excepcion;

public class YaExisteElParticipante extends RuntimeException {
    public YaExisteElParticipante() {
        super("El participante ya está anotado en el partido");
    }
}
