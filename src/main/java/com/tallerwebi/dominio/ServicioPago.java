package com.tallerwebi.dominio;

import java.math.BigDecimal;

public interface ServicioPago {
    
    String crearPago(String titulo, String descripcion, BigDecimal monto) throws Exception;
}
