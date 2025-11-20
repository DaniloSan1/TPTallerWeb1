package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service("servicioHorario")
@Transactional
public class ServicioHorarioImpl implements ServicioHorario {

    private final RepositorioHorario repositorioHorario;

    @Autowired
    public ServicioHorarioImpl(RepositorioHorario repositorioHorario) {
        this.repositorioHorario = repositorioHorario;
    }

    @Override
    public Horario obtenerPorId(Long id) {
        if (id == null) {
            throw new RuntimeException("El ID del horario no puede ser nulo");
        }
        Horario horario = repositorioHorario.obtenerPorId(id);
        if (horario == null) {
            throw new RuntimeException("No se encontró un horario con el ID especificado");
        }
        return horario;
    }

    @Override
    public List<Horario> obtenerPorCancha(Cancha cancha) {
        if (cancha == null) {
            throw new RuntimeException("La cancha no puede ser nula");
        }
        return repositorioHorario.obtenerPorCancha(cancha);
    }

    @Override
    public List<Horario> obtenerDisponiblesPorCancha(Cancha cancha) {
        if (cancha == null) {
            throw new RuntimeException("La cancha no puede ser nula");
        }
        return repositorioHorario.obtenerDisponiblesPorCancha(cancha);
    }

    @Override
    public void cambiarDisponibilidad(Long id, Boolean disponible) {
        if (id == null) {
            throw new IllegalArgumentException("El id del horario no puede ser nulo");
        }
        repositorioHorario.cambiarDisponibilidad(id, disponible);
    }

    @Override
    @Transactional
    public void guardar(Horario horario) {
        if (horario == null) {
            throw new IllegalArgumentException("El horario no puede ser nulo");
        }
        repositorioHorario.guardar(horario);
    }

    @Override
    @Transactional
    public void eliminarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id del horario no puede ser nulo");
        }
        repositorioHorario.eliminarPorId(id);
    }
    @Override
    @Transactional
    public void actualizarHorarios(Horario horario) {
        if (horario == null) {
            throw new IllegalArgumentException("El horario no puede ser nulo");
        }
        repositorioHorario.actualizarHorarios(horario);
        }
    }