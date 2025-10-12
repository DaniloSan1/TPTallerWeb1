package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioCancha {
    List<Cancha> MostrarCanchasConHorariosDisponibles();
    Cancha BuscarCanchaPorId(Long id);
}
