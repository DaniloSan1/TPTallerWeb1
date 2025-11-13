package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

@Service
@Transactional
public class ServicioTorneoImpl implements ServicioTorneo {

    private final RepositorioTorneo repositorioTorneo;
    private final RepositorioReserva repositorioReserva;

    @Autowired
    public ServicioTorneoImpl(RepositorioTorneo repositorioTorneo,
                              RepositorioReserva repositorioReserva) {
        this.repositorioTorneo = repositorioTorneo;
        this.repositorioReserva = repositorioReserva;
    }

    @Override
    public Torneo crearTorneo(Torneo torneo) {
        if (torneo == null || torneo.getCancha() == null || torneo.getOrganizador() == null) {
            throw new RuntimeException("Datos del torneo incompletos");
        }
        if (torneo.getFecha() == null || torneo.getFecha().isBefore(LocalDate.now())) {
            throw new RuntimeException("La fecha del torneo no puede ser nula ni pasada");
        }

        // Validar disponibilidad
        if (repositorioTorneo.existeCanchaYFecha(torneo.getCancha(), torneo.getFecha())) {
            throw new IllegalStateException("Ya existe un torneo en esa cancha y fecha.");
        }
        if (!repositorioReserva.porCanchaYFecha(torneo.getCancha(), torneo.getFecha()).isEmpty()) {
            throw new IllegalStateException("La cancha ya tiene reservas en esa fecha.");
        }

        // Calcular precio 
        BigDecimal precioTotal = BigDecimal.valueOf(torneo.getCancha().getPrecio() * 12);
        torneo.setPrecio(precioTotal);

        // Estado inicial: pendiente de pago
        torneo.setEstado("PENDIENTE");

        repositorioTorneo.crearTorneo(torneo);
        return torneo;
    }
    public void actualizarTorneo(Torneo torneo) {
        repositorioTorneo.actualizarTorneo(torneo);
    }

    @Override
    public List<Torneo> listarTorneos() {
        return repositorioTorneo.listarTorneos();
    }

    @Override
    public List<Torneo> listarTorneosDisponibles() {
        return repositorioTorneo.torneoFuturo(LocalDate.now());
    }

    @Override
    public Torneo obtenerPorId(Long id) {
        return repositorioTorneo.porId(id);
    }

    @Override
    public void cancelarTorneo(Long id) {
        Torneo torneo = obtenerPorId(id);
        if (torneo == null) {
            throw new RuntimeException("No se encontró el torneo con el id especificado");
        }
        torneo.setEstado("CANCELADO");
        repositorioTorneo.crearTorneo(torneo);
    }

    @Override
    public boolean existeTorneoEnFecha(Cancha cancha, LocalDate fecha) {
        return repositorioTorneo.existeCanchaYFecha(cancha, fecha);
    }
}