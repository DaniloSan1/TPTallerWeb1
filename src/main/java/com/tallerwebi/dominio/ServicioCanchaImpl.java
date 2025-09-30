package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioCanchaImpl implements ServicioCancha {
    private List<Cancha> canchas = new ArrayList<>();

public ServicioCanchaImpl() {
    canchas.add(new Cancha(1L, "Cancha 1", "5x5"));
    canchas.add(new Cancha(2L, "Cancha 2", "7x7"));
}

@Override
public List<Cancha> obtenerCancha() {
    return canchas;
}
@Override
public boolean reservarCancha(Long id, String horario, String usuario) {
    for (Cancha cancha : canchas) {
        if (cancha.getId().equals(id)) {
            cancha.reservar(horario, usuario);
            return true;
        }
    }
    return false;
}
@Override
public boolean cancelarCancha(Long id, String horario) {
    for (Cancha cancha : canchas) {
        if (cancha.getId().equals(id)) {
            cancha.cancelar(horario);
            return true;
        }
    }
    return false;
}
}