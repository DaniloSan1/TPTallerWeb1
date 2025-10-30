package com.tallerwebi.dominio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("servicioFotoCancha")
public class ServicioFotoCanchaImpl implements ServicioFotoCancha {
  private RepositorioFotoCancha repositorioFotoCancha;
    @Autowired
    public ServicioFotoCanchaImpl(RepositorioFotoCancha repositorioFotoCancha) {
        this.repositorioFotoCancha = repositorioFotoCancha;
    }  
  
    @Override
    public List<FotoCancha> obtenerFotosCancha(Long canchaId) {
        return repositorioFotoCancha.obtenerFotosCancha(canchaId);
    }
    @Override
    public FotoCancha obtenerPrimeraFotoCancha(Long canchaId) {
        return repositorioFotoCancha.obtenerPrimeraFotoCancha(canchaId);
    }
    @Override
    public List<FotoCancha> insertarFotosAModelCanchas(List<Cancha> canchas) {
        List<FotoCancha> fotosCancha = new java.util.ArrayList<>();
       for (Cancha cancha : canchas) {
                FotoCancha fotoCancha = obtenerPrimeraFotoCancha(cancha.getId());
                if (fotoCancha != null) {
                    fotosCancha.add(fotoCancha);
                }
                else {
                    fotosCancha.add(new FotoCancha("assets/cancha.webp", cancha));
                }
            }
        return fotosCancha;
    }
    @Override
    public List<FotoCancha> insertarFotosAModelPartidos(List<Partido> partidos) {
        List<FotoCancha> fotosCancha = new java.util.ArrayList<>();
       for (Partido partido : partidos) {
                FotoCancha fotoCancha = obtenerPrimeraFotoCancha(partido.getCancha().getId());
                if (fotoCancha != null) {
                    fotosCancha.add(fotoCancha);
                }
                else {
                    fotosCancha.add(new FotoCancha("assets/cancha.webp", partido.getCancha()));
                }
            }
        return fotosCancha;
    
}
}
