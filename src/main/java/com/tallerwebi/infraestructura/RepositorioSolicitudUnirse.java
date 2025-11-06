package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Partido;
import com.tallerwebi.dominio.SolicitudUnirse;
import com.tallerwebi.dominio.EstadoSolicitud;
import java.util.List;
import java.util.Optional;

public interface RepositorioSolicitudUnirse {

    void guardar(SolicitudUnirse solicitud);

    Optional<SolicitudUnirse> buscarPorToken(String token);

    List<SolicitudUnirse> listarPorPartidoYEstado(Partido partido, EstadoSolicitud estado);
}
