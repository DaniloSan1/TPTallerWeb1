package com.tallerwebi.dominio;

import java.util.List;

public interface RespositorioReseniaCancha {
    void guardar(ReseniaCancha reseniaCancha);
    List<ReseniaCancha> obtenerReseniasPorCancha(Long canchaId);
    List<ReseniaCancha> obtenerReseniasPorUsuario(Long usuarioId);
    int contarReseniasPorCancha(Long canchaId);
    
} 
