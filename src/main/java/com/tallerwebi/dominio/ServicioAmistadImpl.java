package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ServicioAmistadImpl implements ServicioAmistad {

    @Autowired
    private RepositorioAmistad repositorioAmistad;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Override
    public void enviarSolicitud(Long idRemitente, Long idReceptor) {
        Usuario remitente = repositorioUsuario.buscarPorId(idRemitente);
        Usuario receptor = repositorioUsuario.buscarPorId(idReceptor);
        // Buscar relación en ambas direcciones
        Amistad existenteDirecta = repositorioAmistad.buscarPorUsuarios(remitente, receptor);
        if (existenteDirecta != null) {
            // Si existe y fue rechazada, permitir reenviar
            if (existenteDirecta.getEstadoDeAmistad() == EstadoDeAmistad.RECHAZADA) {
                existenteDirecta.setEstadoDeAmistad(EstadoDeAmistad.PENDIENTE);
                existenteDirecta.setFechaSolicitud(java.time.LocalDate.now());
            }
            return;
        }

        // Revisar si existe una solicitud en sentido inverso (el receptor ya envió una solicitud al remitente)
        Amistad existenteInversa = repositorioAmistad.buscarPorUsuarios(receptor, remitente);
        if (existenteInversa != null) {
            if (existenteInversa.getEstadoDeAmistad() == EstadoDeAmistad.PENDIENTE) {
                // Aceptar la solicitud pendiente del otro usuario
                existenteInversa.setEstadoDeAmistad(EstadoDeAmistad.ACEPTADA);
            } else if (existenteInversa.getEstadoDeAmistad() == EstadoDeAmistad.RECHAZADA) {
                // Si el otro la rechazó previamente, volver a ponerla como pendiente
                existenteInversa.setEstadoDeAmistad(EstadoDeAmistad.PENDIENTE);
                existenteInversa.setFechaSolicitud(java.time.LocalDate.now());
            }
            return;
        }

        // No existe relación previa: crear nueva solicitud
        repositorioAmistad.guardar(new Amistad(remitente, receptor));
    }

    @Override
    public void aceptarSolicitud(Long idAmistad) {
        Amistad amistad = repositorioAmistad.buscarPorId(idAmistad);
        amistad.setEstadoDeAmistad(EstadoDeAmistad.ACEPTADA);
    }

    @Override
    public void rechazarSolicitud(Long idAmistad) {
        Amistad amistad = repositorioAmistad.buscarPorId(idAmistad);
        amistad.setEstadoDeAmistad(EstadoDeAmistad.RECHAZADA);
    }

    @Override
    public List<Amistad> verSolicitudesPendientes(Long idUsuario) {
        Usuario usuario = repositorioUsuario.buscarPorId(idUsuario);
        return repositorioAmistad.buscarPendientes(usuario);
    }

    @Override
    public List<Amistad> verAmigos(Long idUsuario) {
        Usuario usuario = repositorioUsuario.buscarPorId(idUsuario);
        return repositorioAmistad.buscarAceptadas(usuario);
    }

    @Override
    public Amistad buscarRelacionEntreUsuarios(Long id1, Long id2) {
        Usuario usuario1 = repositorioUsuario.buscarPorId(id1);
        Usuario usuario2 = repositorioUsuario.buscarPorId(id2);

        Amistad amistadDirecta = repositorioAmistad.buscarPorUsuarios(usuario1, usuario2);
        Amistad amistadInversa = repositorioAmistad.buscarPorUsuarios(usuario2, usuario1);

        if (amistadDirecta != null) {
            return amistadDirecta;
        }
        return amistadInversa;
    }

    @Override
    public void eliminarAmistad(Long idAmistad) {
        Amistad amistad = repositorioAmistad.buscarPorId(idAmistad);
        if (amistad != null) {
            repositorioAmistad.eliminar(amistad);
        }
    }

}
