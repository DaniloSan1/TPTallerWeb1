package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioReseniaCancha {
    void agregarReseniaCancha(ReseniaCancha reseniaCancha);
    double calcularCalificacionPromedioCancha(Long canchaId);
    List<ReseniaCancha> obtenerReseniasPorCancha(Long canchaId);
    List<ReseniaCancha> obtenerReseniasPorUsuario(Long usuarioId);
    Boolean verificarSiElUsuarioPuedeReseniarEsaCancha(Usuario usuario, Cancha cancha);
    ReseniaCancha obtenerReseniaCanchaPorId(Long reseniaCanchaId);
    void editarReseniaCancha(ReseniaCancha reseniaCancha);
}
