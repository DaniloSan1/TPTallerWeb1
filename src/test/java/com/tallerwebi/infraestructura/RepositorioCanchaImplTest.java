package com.tallerwebi.infraestructura;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.Query;

import com.tallerwebi.dominio.Cancha;
import com.tallerwebi.dominio.Horario;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;
import com.tallerwebi.dominio.RepositorioCancha;
import com.tallerwebi.infraestructura.RepositorioCanchaIpl;

import org.hibernate.SessionFactory;
import org.hsqldb.Session;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestInfraestructuraConfig.class})
public class RepositorioCanchaImplTest {
    private SessionFactory sessionFactory;

    @Test
    public void DadoQueTengo3CanchasPeroSolo2TienenHorariosDisponiblesMostrarSoloDisponibles() {
        Cancha cancha1 = new Cancha();
        cancha1.setId(1L);
        cancha1.setNombre("Cancha 1");
        cancha1.setDireccion("Direccion 1");
        cancha1.setCapacidad(10);

        Cancha cancha2 = new Cancha();
        cancha2.setId(2L);
        cancha2.setNombre("Cancha 2");
        cancha2.setDireccion("Direccion 2");
        cancha2.setCapacidad(20);

        Cancha cancha3 = new Cancha();
        cancha3.setId(3L);
        cancha3.setNombre("Cancha 3");
        cancha3.setDireccion("Direccion 3");
        cancha3.setCapacidad(30);
        
        Horario horario1 = new Horario();
        horario1.setId(1L);
        horario1.setCancha(cancha1);
        horario1.setDiaSemana(DayOfWeek.MONDAY);
        horario1.setHoraInicio(LocalTime.now());
        horario1.setHoraFin(LocalTime.now().plusHours(1));


        Horario horario2 = new Horario();
        horario2.setId(2L);
        horario2.setCancha(cancha3);
        horario2.setDiaSemana(DayOfWeek.MONDAY);
        horario2.setHoraInicio(LocalTime.now());
        horario2.setHoraFin(LocalTime.now().plusHours(1));

        RepositorioCancha repositorioCancha = new RepositorioCanchaIpl(null);
        // Guardar las canchas y horarios en el repositorio
        String hql="INSERT INTO Cancha (id, nombre, direccion, capacidad) VALUES (:id, :nombre, :direccion, :capacidad)";
        String hql2="INSERT INTO Horario (id, cancha_id, diaSemana, horaInicio, horaFin, disponible) VALUES (:id, :cancha_id, :diaSemana, :horaInicio, :horaFin, :disponible)";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", cancha1.getId());
        query.setParameter("nombre", cancha1.getNombre());
        query.setParameter("direccion", cancha1.getDireccion());
        query.setParameter("capacidad", cancha1.getCapacidad());
        query.executeUpdate();
        List<Cancha> canchasDisponibles = repositorioCancha.MostrarCanchasConHorariosDisponibles();

        // Verificar que solo se devuelven las canchas con horarios disponibles
        assertEquals(2, canchasDisponibles.size());
        assertTrue(canchasDisponibles.contains(cancha1));
        assertTrue(canchasDisponibles.contains(cancha2));
        assertFalse(canchasDisponibles.contains(cancha3));
    }
}