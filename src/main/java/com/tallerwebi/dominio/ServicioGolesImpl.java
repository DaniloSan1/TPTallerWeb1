package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ServicioGolesImpl implements ServicioGoles {

    private RepositorioGoles repositorioGoles;

    private ServicioPartido servicioPartido;

    private ServicioPartidoEquipo servicioPartidoEquipo;
    private ServicioUsuario servicioUsuario;

    @Autowired
    public ServicioGolesImpl(RepositorioGoles repositorioGoles, ServicioPartido servicioPartido,
            ServicioPartidoEquipo servicioPartidoEquipo,ServicioUsuario servicioUsuario) {
        this.repositorioGoles = repositorioGoles;
        this.servicioPartido = servicioPartido;
        this.servicioPartidoEquipo = servicioPartidoEquipo;
        this.servicioUsuario=servicioUsuario;
    }

    @Override
    @Transactional
    public void registrarGoles(Partido partido, List<Gol> goles, Usuario usuario) {

        for (Gol gol : goles) {
            repositorioGoles.guardar(gol);
        }
        // Actualizar goles por equipo
        servicioPartidoEquipo.actualizarGolesPorEquipo(partido);
    }

    @Override
    public int devolverCantidadTotalDeGolesDelUsuario(Long usuarioId){
        int cantidad=0;
        List<Gol>golesDelUsuario=repositorioGoles.buscarPorUsuario(usuarioId);
        if(golesDelUsuario.isEmpty()){
            return cantidad;
        }
        for(Gol gol : golesDelUsuario){
            cantidad+=gol.getCantidad();
        }
        return cantidad;
    }
    
    @Override
    public Double devolverGolesPromedioPorPartidoDelUsuario(Long usuarioId) {
    Double cantidadPromedio = 0.0;

    Usuario usuario = servicioUsuario.buscarPorId(usuarioId);
    List<Gol> golesDelUsuario = repositorioGoles.buscarPorUsuario(usuarioId);
    List<Partido> partidos = servicioPartido.partidosTerminadosDelUsuario(usuarioId);

    int partidosJugados = partidos.size();

    if (golesDelUsuario.isEmpty() || partidosJugados == 0) {
        return cantidadPromedio;
    }

    int cantidadGolesTotales = 0;
    for (Gol gol : golesDelUsuario) {
        cantidadGolesTotales += gol.getCantidad();
    }

   
    cantidadPromedio = (double) cantidadGolesTotales / partidosJugados;

    return cantidadPromedio;
    }
}