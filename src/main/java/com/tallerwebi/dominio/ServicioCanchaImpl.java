package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;

import com.tallerwebi.dominio.excepcion.CanchaNoEncontrada;
import com.tallerwebi.dominio.excepcion.NoHayCanchasDisponibles;

import java.util.ArrayList;
import java.util.List;

@Service("servicioCancha")
public class ServicioCanchaImpl implements ServicioCancha {

    @Override
    public List<Cancha> obtenerTodasLasCanchas() {
        return repositorioCancha.obtenerTodasLasCanchas();
    }

    private final RepositorioCancha repositorioCancha;

    public ServicioCanchaImpl(RepositorioCancha repositorioCancha) {
        this.repositorioCancha = repositorioCancha;
    }

    @Override
    public List<Cancha> obtenerCanchasDisponibles(String busqueda, Zona zona, Double precio) {
        Double precioMinimo = null;
        Double precioMaximo = null;
        if (precio==1000.00) {
            precioMinimo = 0.0;
            precioMaximo = 1000.0;
        } else if (precio==5000.00) {
            precioMinimo = 1000.01;
            precioMaximo = 5000.0;
        } else if (precio==5001.00) {
            precioMinimo = 5000.01;
            precioMaximo = null;
        } else {
            precioMinimo = null;
            precioMaximo = null;
        }

        List<Cancha> canchasDisponibles = repositorioCancha.MostrarCanchasConHorariosDisponibles(busqueda, zona, precioMinimo, precioMaximo);
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