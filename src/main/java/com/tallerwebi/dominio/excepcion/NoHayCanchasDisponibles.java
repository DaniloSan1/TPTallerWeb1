package com.tallerwebi.dominio.excepcion;

public class NoHayCanchasDisponibles extends RuntimeException {
    public NoHayCanchasDisponibles() {
        super("No hay canchas disponibles en este momento");
    }
}
