package com.tallerwebi.dominio.excepcion;

public class GolesDebeSerMayorOIgualACero extends IllegalArgumentException {
    public GolesDebeSerMayorOIgualACero() {
        super("La cantidad de goles debe ser mayor o igual a cero.");
    }

}
