package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;

@Service
public interface ServicioEquipo {
    Equipo crearEquipo(String name, Usuario creador);

    Equipo buscarPorId(Long id);
}