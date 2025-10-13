package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;



import java.util.ArrayList;
import java.util.List;

@Service("servicioCancha")
public class ServicioCanchaImpl implements ServicioCancha {

    private final RepositorioCancha repositorioCancha;

    public ServicioCanchaImpl(RepositorioCancha repositorioCancha) {
        this.repositorioCancha = repositorioCancha;
    }

    @Override
    public List<Cancha> obtenerCanchasDisponibles() {
        List<Cancha> canchasDisponibles = repositorioCancha.MostrarCanchasConHorariosDisponibles();
        return canchasDisponibles;
    }
    
    @Override
    public Cancha obtenerCanchaPorId(Long id){
        Cancha cancha = repositorioCancha.BuscarCanchaPorId(id);
        return cancha;
    }
}