package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicioPartidoImpl implements ServicioPartido {
    private final RepositorioPartido repo;
    public ServicioPartidoImpl(RepositorioPartido repo) { this.repo = repo; }

    @Override
    public List<Partido> buscar(Zona zona, Nivel nivel, boolean soloConCupo) {
        return repo.todos().stream()
                .filter(p -> zona == null || p.getZona() == zona)
                .filter(p -> nivel == null || p.getNivel() == nivel)
                .filter(p -> !soloConCupo || p.tieneCupo())
                .collect(Collectors.toList());
    }
}
