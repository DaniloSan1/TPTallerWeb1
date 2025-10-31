package com.tallerwebi.infraestructura;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.tallerwebi.dominio.Cancha;
import com.tallerwebi.dominio.FotoCancha;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestInfraestructuraConfig.class})
public class RepositorioFotoCanchaImplTest {
    @Autowired
    private RepositorioFotoCanchaImpl repositorioFotoCancha;
    private SessionFactory sessionFactory;
    private FotoCancha fotoCancha1;
    private FotoCancha fotoCancha2;
    private FotoCancha fotoCancha3;
    private FotoCancha fotoCancha4;
    private Cancha cancha1;
    private Cancha cancha2;

    @BeforeEach
    public void init() {
        this.repositorioFotoCancha = new RepositorioFotoCanchaImpl(this.sessionFactory);
        fotoCancha1 = new FotoCancha();
        fotoCancha2 = new FotoCancha();
        fotoCancha3 = new FotoCancha();
        fotoCancha4 = new FotoCancha();
        cancha1 = new Cancha();
        cancha2 = new Cancha();
        cancha1.setId(1L);
        cancha2.setId(2L);
        fotoCancha1.setId(1L);
        fotoCancha1.setCancha(cancha1);
        fotoCancha2.setId(2L);
        fotoCancha2.setCancha(cancha1);
        fotoCancha3.setId(3L);
        fotoCancha3.setCancha(cancha2);
        fotoCancha4.setId(4L);
        fotoCancha4.setCancha(cancha2);
    }

    @Test
    public void queCuandoQuieroObtenerFotosdeUnaCanchaMeDevuelveLasFotosCorrespondientes() {
        var resultados = repositorioFotoCancha.obtenerFotosCancha(1L);
        assert(resultados.size() >= 2);
        assert(resultados.contains(fotoCancha1));
        assert(resultados.contains(fotoCancha2));
    }

    @Test
    public void queCuandoQuieroObtenerLaPrimeraFotoDeUnaCanchaMeDevuelveSoloLaFotoCorrespondiente() {
        FotoCancha resultado = repositorioFotoCancha.obtenerPrimeraFotoCancha(2L);
        assert(resultado != null);
        assert(resultado.getId().equals(fotoCancha3.getId()));
    }
}
