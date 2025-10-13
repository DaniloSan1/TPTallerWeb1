package com.tallerwebi.dominio.excepcion;

public class NoHayCupoEnPartido extends RuntimeException {
    public NoHayCupoEnPartido() {
        super("No hay cupo en el partido");
    }
}
