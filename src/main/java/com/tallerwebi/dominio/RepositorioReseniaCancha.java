package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioReseniaCancha {
    void guardar(ReseniaCancha reseniaCancha);
    List<ReseniaCancha> obtenerReseniasPorCancha(Long canchaId);
    List<ReseniaCancha> obtenerReseniasPorUsuario(Long usuarioId);
    int contarReseniasPorCancha(Long canchaId);
    List<ReseniaCancha> buscarReseniaPreviaDelUsuarioAUnaCanchaDeterminada(Long usuarioId,Long canchaId);
    ReseniaCancha obtenerReseniaCanchaPorId(Long reseniaCanchaId);
    void actualizar(ReseniaCancha reseniaCancha);
    
} 
