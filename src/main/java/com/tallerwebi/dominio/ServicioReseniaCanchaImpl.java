package com.tallerwebi.dominio;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ServicioReseniaCanchaImpl implements ServicioReseniaCancha {

    private final RespositorioReseniaCancha repositorioReseniaCancha;

    @Autowired
    public ServicioReseniaCanchaImpl(RespositorioReseniaCancha repositorioReseniaCancha) {
        this.repositorioReseniaCancha = repositorioReseniaCancha;
    }

    @Override
    public void agregarReseniaCancha(ReseniaCancha reseniaCancha) {
        Boolean puedeReseniar = this.verificarSiElUsuarioPuedeReseniarEsaCancha(reseniaCancha.getUsuario(), reseniaCancha.getCancha());
        if(puedeReseniar){
            repositorioReseniaCancha.guardar(reseniaCancha);
        }
         
    }

    @Override
    public List<ReseniaCancha> obtenerReseniasPorCancha(Long canchaId) {
        return repositorioReseniaCancha.obtenerReseniasPorCancha(canchaId);
    }

    @Override
    public List<ReseniaCancha> obtenerReseniasPorUsuario(Long usuarioId) {
        return repositorioReseniaCancha.obtenerReseniasPorUsuario(usuarioId);
    }

    @Override
    public double calcularCalificacionPromedioCancha(Long canchaId) {
        int cantidadResenias = repositorioReseniaCancha.contarReseniasPorCancha(canchaId);
        if (cantidadResenias == 0) {
            return 0.0; 
        }
        double sumaCalificaciones = 0.0;
        for (ReseniaCancha resenia : repositorioReseniaCancha.obtenerReseniasPorCancha(canchaId)) {
            sumaCalificaciones += resenia.getCalificacion();
        }
        return sumaCalificaciones / cantidadResenias;
    }

    @Override
    public Boolean verificarSiElUsuarioPuedeReseniarEsaCancha(Usuario usuario, Cancha cancha){
        Boolean puede=true;
        List<ReseniaCancha> reseniaCancha= this.repositorioReseniaCancha.buscarReseniaPreviaDelUsuarioAUnaCanchaDeterminada(usuario.getId(), cancha.getId());
        if (!reseniaCancha.isEmpty()){
            return puede=false;
        }
        return puede;
    }
}
