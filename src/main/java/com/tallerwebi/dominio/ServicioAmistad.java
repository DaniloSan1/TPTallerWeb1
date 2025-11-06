package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ServicioAmistad {
    public void enviarSolicitud(Long idRemitente, Long idReceptor);

    public void aceptarSolicitud(Long idAmistad);

    public void rechazarSolicitud(Long idAmistad);

    public List<Amistad> verSolicitudesPendientes(Long idUsuario);

    public List<Amistad> verAmigos(Long idUsuario);

    public Amistad buscarRelacionEntreUsuarios(Long id1, Long id2);

    public void eliminarAmistad(Long idAmistad);

}
