package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioGoles {
    void registrarGoles(Partido partido, List<Gol> goles, Usuario usuario);
    int devolverCantidadTotalDeGolesDelUsuario(Long usuarioId);
    Double devolverGolesPromedioPorPartidoDelUsuario(Long usuarioId);
}