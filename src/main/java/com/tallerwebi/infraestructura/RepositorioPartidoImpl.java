package com.tallerwebi.infraestructura;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tallerwebi.dominio.Nivel;
import com.tallerwebi.dominio.Partido;
import com.tallerwebi.dominio.RepositorioPartido;
import com.tallerwebi.dominio.Zona;

@Repository("repositorioPartido")
@Transactional
public class RepositorioPartidoImpl implements RepositorioPartido {
    private SessionFactory sessionFactory;
    private final AtomicLong sec = new AtomicLong(1);

    @Autowired
    public RepositorioPartidoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Partido porId(Long id) {
        return this.sessionFactory.getCurrentSession().get(Partido.class, id);
    }

    @Override
    public List<Partido> todos() {
        Map<Long, Partido> data = new HashMap<>();
        guardar(new Partido(null,"Mix Norte", "Descripción Mix Norte", Zona.NORTE, Nivel.INTERMEDIO,
                LocalDateTime.now().plusDays(1), 6, null, null));
        guardar(new Partido(null,"Centro Pro", "Descripción Centro Pro", Zona.CENTRO, Nivel.AVANZADO,
                LocalDateTime.now().plusHours(6), 4, null, null));
        guardar(new Partido(null,"Sur Tranqui", "Descripción Sur Tranqui", Zona.SUR, Nivel.PRINCIPIANTE,
                LocalDateTime.now().plusDays(2), 8, null, null));
        guardar(new Partido(null,"Este Inter", "Descripción Este Inter", Zona.ESTE, Nivel.INTERMEDIO,
                LocalDateTime.now().plusDays(3), 5, null, null));

        return data.values().stream()
                .sorted(Comparator.comparing(Partido::getFecha))
                .collect(Collectors.toList());
    }

    @Override
    public void guardar(Partido p) {
        Map<Long, Partido> data = new HashMap<>();
        if (p.getId() == null) {
            try {
                var f = Partido.class.getDeclaredField("id");
                f.setAccessible(true);
                f.set(p, sec.getAndIncrement());
            } catch (Exception ignored) {
            }
        }
        data.put(p.getId(), p);
    }
}