package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.EquipoNoEncontrado;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ServicioEquipo {
    Equipo crearEquipo(String nombre, String descripcion, Usuario creador);

    Equipo buscarPorId(Long id) throws EquipoNoEncontrado;

    void actualizarNombre(Equipo equipo, String nuevoNombre);

    List<Equipo> obtenerEquiposDelUsuario(Usuario usuario);

    List<Equipo> obtenerEquiposDelUsuarioConFiltro(Usuario usuario, String nombre);
}