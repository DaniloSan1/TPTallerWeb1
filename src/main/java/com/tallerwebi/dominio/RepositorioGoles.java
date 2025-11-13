package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioGoles {
    void guardar(Gol gol);

    List<Gol> buscarPorPartido(Partido partido);
    
}