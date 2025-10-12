package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioCanchaImpl implements ServicioCancha {
    private List<Cancha> canchas = new ArrayList<>();

    public ServicioCanchaImpl() {
        Cancha c1 = new Cancha("Cancha 1","Direc Cancha 1", 5, "Césped", Zona.OESTE);
        c1.setId(1L);
        Cancha c2 = new Cancha("Cancha 2", "Direc Cancha 2" , 7, "Césped", Zona.NORTE);
        c2.setId(2L);
        canchas.add(c1);
        canchas.add(c2);

    }

    @Override
    public List<Cancha> obtenerCancha() {
        return canchas;
    }

    @Override
    public boolean reservarCancha(Long id) {
        for (Cancha cancha : canchas) {
            if (cancha.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean cancelarCancha(Long id) {
        for (Cancha cancha : canchas) {
            if (cancha.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}