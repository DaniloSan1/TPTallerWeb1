package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.EquipoNoEncontrado;
import com.tallerwebi.dominio.excepcion.PermisosInsufficientes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

@Service
public class ServicioEquipoImpl implements ServicioEquipo {
    private final RepositorioEquipo repositorioEquipo;

    @Autowired
    public ServicioEquipoImpl(RepositorioEquipo repositorioEquipo) {
        this.repositorioEquipo = repositorioEquipo;
    }

    @Override
    public Equipo crearEquipo(String nombre, String descripcion, String insigniaUrl, Usuario creador, TipoEquipo tipo) {
        Equipo equipo = new Equipo(nombre, descripcion, creador, LocalDateTime.now(), tipo);
        equipo.setInsigniaUrl(insigniaUrl);
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

    @Override
    public void actualizarEquipo(Equipo equipo, String nombre, String descripcion, String insigniaUrl) {
        equipo.setNombre(nombre);
        equipo.setDescripcion(descripcion);
        equipo.setInsigniaUrl(insigniaUrl);
        repositorioEquipo.modificar(equipo);
    }

    @Override
    public List<Equipo> obtenerEquiposDelUsuarioConFiltro(Usuario usuario, String nombre) {
        return repositorioEquipo.buscarEquiposPorUsuarioYNombre(usuario, nombre);
    }

    @Override
    @Transactional
    public boolean esUsuarioCreador(Long equipoId, Usuario usuario) {
        try {
            Equipo equipo = buscarPorId(equipoId);
            Usuario creador = equipo.getCreadoPor();
            if (creador == null || usuario == null) {
                return false;
            }
            return creador.getId().equals(usuario.getId());
        } catch (EquipoNoEncontrado e) {
            return false;
        }
    }

    @Override
    public boolean esUsuarioCreador(Equipo equipo, Usuario usuario) {
        if (equipo == null || equipo.getCreadoPor() == null || usuario == null) {
            return false;
        }
        return equipo.getCreadoPor().getId().equals(usuario.getId());
    }

    @Override
    public Equipo buscarPorIdYUsuario(Long equipoId, Usuario usuario) throws EquipoNoEncontrado {
        Equipo equipo = repositorioEquipo.buscarPorIdYUsuario(equipoId, usuario);
        if (equipo == null) {
            throw new EquipoNoEncontrado();
        }
        return equipo;
    }

    @Override
    public List<Equipo> listarTodos() {
        return repositorioEquipo.listarTodos();
    }

    public Equipo buscarEquipoDelUsuario(Usuario usuario) {
        return repositorioEquipo.buscarEquipoDelUsuario(usuario);
    }

    public List<Equipo> buscarEquiposPorUsuario(Usuario usuario) {
        return repositorioEquipo.buscarEquiposPorUsuario(usuario);
    }
}