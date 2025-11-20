
package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tallerwebi.dominio.excepcion.CanchaNoEncontrada;
import com.tallerwebi.dominio.excepcion.NoHayCanchasDisponibles;

import java.util.List;

@Service("servicioCancha")
public class ServicioCanchaImpl implements ServicioCancha {

    @Override
    public Cancha obtenerCanchaConHorariosPorId(Long id) {
        return repositorioCancha.BuscarCanchaPorId(id);
    }

    @Override
    public List<Cancha> obtenerTodasLasCanchas() {
        return repositorioCancha.obtenerTodasLasCanchas();
    }

    private final RepositorioCancha repositorioCancha;
    private final RepositorioReserva repositorioReserva;


    public ServicioCanchaImpl(RepositorioCancha repositorioCancha, RepositorioReserva repositorioReserva) {
        this.repositorioCancha = repositorioCancha;
        this.repositorioReserva = repositorioReserva;
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

    @Override
    public void crearCancha(Cancha cancha) {
        if (cancha == null) {
            throw new IllegalArgumentException("La cancha no puede ser nula");
        }
        repositorioCancha.guardar(cancha);
    }

    @Override
    @Transactional
    public void eliminarCancha(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id de la cancha no puede ser nulo");
        }
        Cancha cancha = repositorioCancha.BuscarCanchaPorId(id);
        if (cancha == null) {
            throw new IllegalArgumentException("No se encontró la cancha con id: " + id);
        }
        var reservas = repositorioReserva.porCancha(cancha);
        if (reservas != null && !((List<Reserva>) reservas).isEmpty()) {
            throw new IllegalStateException("No se puede eliminar la cancha porque tiene reservas asociadas.");
        }
        repositorioCancha.eliminarPorId(id);
    }

    @Override
    public void guardar(Cancha cancha) {
        if (cancha == null) {
            throw new IllegalArgumentException("La cancha no puede ser nula");
        }
        repositorioCancha.guardar(cancha);
    }

}