package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioPartido {
    List<Partido> todos();

    Partido porId(Long id);

    void guardar(Partido partido);
    
}
