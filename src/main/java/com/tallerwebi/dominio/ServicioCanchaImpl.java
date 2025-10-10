package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioCanchaImpl implements ServicioCancha {
    private List<Cancha> canchas = new ArrayList<>();

    public ServicioCanchaImpl() {
       
    }

    @Override
    public List<Cancha> obtenerCancha() {
        return canchas;
    }

    @Override
    public boolean reservarCancha(Long id) {
        for (Cancha cancha : canchas) {
            if (cancha.getId().equals(id)) {
                cancha.setDisponible(false);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean cancelarCancha(Long id) {
        for (Cancha cancha : canchas) {
            if (cancha.getId().equals(id)) {
                cancha.setDisponible(true);
                return true;
            }
        }
        return false;
    }
}