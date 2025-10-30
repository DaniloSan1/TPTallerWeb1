package com.tallerwebi.dominio.excepcion;

public class ParticipanteNoEncontrado extends RuntimeException {
    public ParticipanteNoEncontrado() {
        super("No se encontró el participante");
    }
}
