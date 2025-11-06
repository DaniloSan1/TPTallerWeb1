package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioReseniaCancha {
    public void agregarReseniaCancha(ReseniaCancha reseniaCancha);
    public double calcularCalificacionPromedioCancha(Long canchaId);
    public List<ReseniaCancha> obtenerReseniasPorCancha(Long canchaId);
    public List<ReseniaCancha> obtenerReseniasPorUsuario(Long usuarioId);
}
