package com.tallerwebi.dominio.excepcion;

public class CanchaNoEncontrada   extends RuntimeException {
    public CanchaNoEncontrada(String mensaje) {
        super(mensaje);
    }
    public CanchaNoEncontrada() {
        super("No se encontró la cancha");
    }
    
}
