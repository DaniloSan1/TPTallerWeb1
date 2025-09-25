package com.tallerwebi.dominio;
import java.util.List;

public interface ServicioPartido {
    List<Partido> buscar(Zona zona, Nivel nivel, boolean soloConCupo);
}
