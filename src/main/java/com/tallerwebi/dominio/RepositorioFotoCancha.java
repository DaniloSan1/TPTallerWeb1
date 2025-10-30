package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioFotoCancha {
    public List<FotoCancha> obtenerFotosCancha(Long canchaId);
    public FotoCancha obtenerPrimeraFotoCancha(Long canchaId);
}
