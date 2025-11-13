package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.EquipoNoEncontrado;
import com.tallerwebi.dominio.excepcion.PermisosInsufficientes;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ServicioEquipo {
    Equipo crearEquipo(String nombre, String descripcion, String insigniaUrl, Usuario creador);

    Equipo buscarPorId(Long id) throws EquipoNoEncontrado;

    void actualizarNombre(Equipo equipo, String nuevoNombre);

    void actualizarEquipo(Equipo equipo, String nombre, String descripcion, String insigniaUrl);

    List<Equipo> obtenerEquiposDelUsuarioConFiltro(Usuario usuario, String nombre);

    boolean esUsuarioCreador(Long equipoId, Usuario usuario);

    boolean esUsuarioCreador(Equipo equipo, Usuario usuario);

    Equipo buscarPorIdYUsuario(Long equipoId, Usuario usuario) throws EquipoNoEncontrado;

    List<Equipo> listarTodos();

    Equipo buscarEquipoDelUsuario(Usuario usuario);

    List<Equipo> buscarEquiposPorUsuario(Usuario usuario);
}