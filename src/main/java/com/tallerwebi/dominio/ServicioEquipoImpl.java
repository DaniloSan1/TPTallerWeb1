package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ServicioEquipoImpl implements ServicioEquipo {
    private final RepositorioEquipo repositorioEquipo;

    @Autowired
    public ServicioEquipoImpl(RepositorioEquipo repositorioEquipo) {
        this.repositorioEquipo = repositorioEquipo;
    }

    @Override
    public Equipo crearEquipo(String name, Usuario creador) {
        Equipo equipo = new Equipo(name, creador, LocalDateTime.now());
        repositorioEquipo.guardar(equipo);
        return equipo;
    }

    @Override
    public Equipo buscarPorId(Long id) {
        return repositorioEquipo.buscarPorId(id);
    }
}