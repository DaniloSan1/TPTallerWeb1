package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioCancha {
    List<Cancha> MostrarCanchasConHorariosDisponibles(String busqueda, Zona zona, Double precioMinimo, Double precioMaximo);
    Cancha BuscarCanchaPorId(Long id);
    
}
