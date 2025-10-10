package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioCancha {
    List<Cancha> MostrarCanchasDisponibles();
    Cancha BuscarCanchaPorId(Long id);
}
