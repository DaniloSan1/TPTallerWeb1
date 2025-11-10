package com.tallerwebi.dominio;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ServicioReseniaCanchaImpl implements ServicioReseniaCancha {

    private final RepositorioReseniaCancha repositorioReseniaCancha;

    @Autowired
    public ServicioReseniaCanchaImpl(RepositorioReseniaCancha repositorioMock) {
        this.repositorioReseniaCancha = repositorioMock;
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

    @Override
    public ReseniaCancha obtenerReseniaCanchaPorId(Long reseniaCanchaId) {
        ReseniaCancha resenia = this.repositorioReseniaCancha.obtenerReseniaCanchaPorId(reseniaCanchaId);
        if (resenia == null) {
            throw new IllegalArgumentException("Reseña no encontrada para id: " + reseniaCanchaId);
        }
        return resenia;
    }

    @Override
    public void editarReseniaCancha(ReseniaCancha reseniaCancha){
        repositorioReseniaCancha.actualizar(reseniaCancha);
    }
}
