package com.tallerwebi.dominio.excepcion;

public class ErrorSubiendoImagenException extends RuntimeException {
    public ErrorSubiendoImagenException() {
        super("Error al subir la imagen");
    }

    public ErrorSubiendoImagenException(String message) {
        super(message);
    }
}