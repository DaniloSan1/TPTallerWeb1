package com.tallerwebi.dominio;

import com.tallerwebi.dominio.Equipo;
import com.tallerwebi.dominio.InscripcionTorneo;
import com.tallerwebi.dominio.RepositorioInscripcionTorneo;
import com.tallerwebi.dominio.Torneo;
import com.tallerwebi.dominio.RepositorioEquipo;
import com.tallerwebi.dominio.RepositorioTorneo;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ServicioInscripcionImpl implements ServicioInscripcion {

    private final RepositorioInscripcionTorneo inscripcionRepo;
    private final RepositorioTorneo torneoRepo;
    private final RepositorioEquipo equipoRepo;

    @Autowired
    public ServicioInscripcionImpl(RepositorioInscripcionTorneo inscripcionRepo,
                                        RepositorioTorneo torneoRepo,
                                        RepositorioEquipo equipoRepo) {
        this.inscripcionRepo = inscripcionRepo;
        this.torneoRepo = torneoRepo;
        this.equipoRepo = equipoRepo;
    }

    @Override
    public InscripcionTorneo inscribirEquipo(Long torneoId, Long equipoId) {
        Torneo torneo = torneoRepo.porId(torneoId);
        if (torneo == null) {
            throw new RuntimeException("Torneo no encontrado");
        }

        Equipo equipo = equipoRepo.buscarPorId(equipoId);
        if (equipo == null) {
            throw new RuntimeException("Equipo no encontrado");
        }

        // Validar que no esté ya inscripto
        List<InscripcionTorneo> inscripciones = inscripcionRepo.buscarPorTorneo(torneo.getId());
        boolean yaInscripto = inscripciones.stream()
                .anyMatch(i -> i.getEquipo().getId().equals(equipoId));

        if (yaInscripto) {
            throw new RuntimeException("El equipo ya está inscripto en este torneo");
        }

        InscripcionTorneo inscripcion = new InscripcionTorneo();
        inscripcion.setTorneo(torneo);
        inscripcion.setEquipo(equipo);
        inscripcion.setFechaInscripcion(LocalDateTime.now());

        inscripcionRepo.guardar(inscripcion);
        return inscripcion;
    }

    @Override
    public List<InscripcionTorneo> listarInscripcionesPorTorneo(Long torneoId) {
        Torneo torneo = torneoRepo.porId(torneoId);
        if (torneo == null) {
            throw new RuntimeException("Torneo no encontrado");
        }

        List<InscripcionTorneo> inscripciones = inscripcionRepo.buscarPorTorneo(torneo.getId());

        //Evita LazyInitializationException
        for (InscripcionTorneo inscripcion : inscripciones) {
            Hibernate.initialize(inscripcion.getEquipo());
            Hibernate.initialize(inscripcion.getEquipo().getJugadores());
            inscripcion.getEquipo().getJugadores()
                    .forEach(ej -> Hibernate.initialize(ej.getUsuario()));
        }

        return inscripciones;
    }


    @Override
    public void cancelarInscripcion(Long inscripcionId) {
        InscripcionTorneo inscripcion = inscripcionRepo.buscarPorId(inscripcionId);
        if (inscripcion == null) {
            throw new RuntimeException("Inscripción no encontrada");
        }
        inscripcionRepo.eliminar(inscripcion);
    }

}
