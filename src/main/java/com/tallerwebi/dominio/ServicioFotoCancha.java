package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioFotoCancha {
    List<FotoCancha> obtenerFotosCancha(Long canchaId);
    FotoCancha obtenerPrimeraFotoCancha(Long canchaId);
    List<FotoCancha> insertarFotosAModelCanchas(List<Cancha> canchas);
    List<FotoCancha> insertarFotosAModelPartidos(List<Partido> partidos);
}
