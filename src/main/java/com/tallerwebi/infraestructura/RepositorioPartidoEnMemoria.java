package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class RepositorioPartidoEnMemoria implements RepositorioPartido {
    private final Map<Long, Partido> data = new HashMap<>();
    private final AtomicLong sec = new AtomicLong(1);

    public RepositorioPartidoEnMemoria() {
        guardar(new Partido(null, "Mix Norte",  Zona.NORTE,  Nivel.INTERMEDIO,  LocalDateTime.now().plusDays(1), 6));
        guardar(new Partido(null, "Centro Pro", Zona.CENTRO, Nivel.AVANZADO,    LocalDateTime.now().plusHours(6), 4));
        guardar(new Partido(null, "Sur Tranqui",Zona.SUR,    Nivel.PRINCIPIANTE, LocalDateTime.now().plusDays(2), 8));
        guardar(new Partido(null, "Este Inter", Zona.ESTE,   Nivel.INTERMEDIO,   LocalDateTime.now().plusDays(3), 5));
    }

    @Override public List<Partido> todos() {
        return data.values().stream()
                .sorted(Comparator.comparing(Partido::getFecha))
                .collect(Collectors.toList());
    }
    @Override public Partido porId(Long id) { return data.get(id); }

    @Override public void guardar(Partido p) {
        if (p.getId() == null) {
            try {
                var f = Partido.class.getDeclaredField("id");
                f.setAccessible(true);
                f.set(p, sec.getAndIncrement());
            } catch (Exception ignored) {}
        }
        data.put(p.getId(), p);
    }
}
