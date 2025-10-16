package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;

import com.tallerwebi.dominio.excepcion.CanchaNoEncontrada;
import com.tallerwebi.dominio.excepcion.NoHayCanchasDisponibles;

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
        if (canchasDisponibles.isEmpty()) {
            throw new NoHayCanchasDisponibles();
        }
        return canchasDisponibles;
    }
    
    @Override
    public Cancha obtenerCanchaPorId(Long id){
        Cancha cancha = repositorioCancha.BuscarCanchaPorId(id);
        if (cancha==null) {
            throw new CanchaNoEncontrada("No se encontró la cancha con ID: " + id);
            
        }
        return cancha;
    }
}