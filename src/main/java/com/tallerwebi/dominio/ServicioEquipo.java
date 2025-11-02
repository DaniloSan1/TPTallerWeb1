package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.EquipoNoEncontrado;
import org.springframework.stereotype.Service;

@Service
public interface ServicioEquipo {
    Equipo crearEquipo(String nombre, Usuario creador);

    Equipo buscarPorId(Long id) throws EquipoNoEncontrado;

    void actualizarNombre(Equipo equipo, String nuevoNombre);
}