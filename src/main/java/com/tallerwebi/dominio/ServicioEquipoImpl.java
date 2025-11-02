package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.EquipoNoEncontrado;
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
    public Equipo crearEquipo(String nombre, Usuario creador) {
        Equipo equipo = new Equipo(nombre, creador, LocalDateTime.now());
        repositorioEquipo.guardar(equipo);
        return equipo;
    }

    @Override
    public Equipo buscarPorId(Long id) throws EquipoNoEncontrado {
        Equipo equipo = repositorioEquipo.buscarPorId(id);
        if (equipo == null) {
            throw new EquipoNoEncontrado();
        }
        return equipo;
    }

    @Override
    public void actualizarNombre(Equipo equipo, String nuevoNombre) {
        equipo.setNombre(nuevoNombre);
        repositorioEquipo.modificar(equipo);
    }
}