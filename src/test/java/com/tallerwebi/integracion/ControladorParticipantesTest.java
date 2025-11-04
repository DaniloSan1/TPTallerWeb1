package com.tallerwebi.integracion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.tallerwebi.dominio.Cancha;
import com.tallerwebi.dominio.Equipo;
import com.tallerwebi.dominio.EquipoEnum;
import com.tallerwebi.dominio.EquipoJugador;
import com.tallerwebi.dominio.Horario;
import com.tallerwebi.dominio.Nivel;
import com.tallerwebi.dominio.Partido;
import com.tallerwebi.dominio.PartidoEquipo;
import com.tallerwebi.dominio.PartidoParticipante;
import com.tallerwebi.dominio.Reserva;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.Zona;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class })
public class ControladorParticipantesTest {

        @Autowired
        private WebApplicationContext wac;
        private MockMvc mockMvc;

        @Autowired
        private SessionFactory sessionFactory;

        @BeforeEach
        public void init() {
                this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        }

        @Test
        @Transactional
        @Rollback
        public void deberiaActualizarEquipoSiExisteElParticipante() throws Exception {
                Cancha cancha = new Cancha("Cancha 1", "Direccion 1", 20, "CESPED", Zona.NORTE);
                this.sessionFactory.getCurrentSession().save(cancha);

                Horario horario = new Horario(cancha, DayOfWeek.MONDAY, LocalTime.now(), LocalTime.now().plusHours(1));
                this.sessionFactory.getCurrentSession().save(horario);

                Usuario creador = new Usuario("usuario1", "password", "email@example.com", "usernameCreador");
                creador.setApellido("apellido");
                this.sessionFactory.getCurrentSession().save(creador);

                Reserva reserva = new Reserva(horario, creador, LocalDateTime.now().plusDays(1));
                this.sessionFactory.getCurrentSession().save(reserva);

                Partido nuevoPartido = new Partido(null, "Partido de prueba", "Descripción del partido",
                                Nivel.INTERMEDIO,
                                10,
                                reserva,
                                creador);

                this.sessionFactory.getCurrentSession().save(nuevoPartido);

                // Crear equipos
                Equipo equipo1 = new Equipo("EQUIPO_1", creador, LocalDateTime.now());
                this.sessionFactory.getCurrentSession().save(equipo1);

                Equipo equipo2 = new Equipo("EQUIPO_2", creador, LocalDateTime.now());
                this.sessionFactory.getCurrentSession().save(equipo2);

                // Crear PartidoEquipo
                PartidoEquipo pe1 = new PartidoEquipo(nuevoPartido, equipo1, 0);
                this.sessionFactory.getCurrentSession().save(pe1);

                PartidoEquipo pe2 = new PartidoEquipo(nuevoPartido, equipo2, 0);
                this.sessionFactory.getCurrentSession().save(pe2);

                Usuario participante = new Usuario("usuario2", "password2", "email2@example.com", "username2");
                participante.setNombre("participante1");
                participante.setApellido("apellido1");
                this.sessionFactory.getCurrentSession().save(participante);

                // Crear EquipoJugador para el participante en equipo1
                EquipoJugador ej = new EquipoJugador(equipo1, participante, LocalDateTime.now());
                this.sessionFactory.getCurrentSession().save(ej);

                PartidoParticipante partidoParticipante = new PartidoParticipante(nuevoPartido, participante,
                                EquipoEnum.SIN_EQUIPO);
                this.sessionFactory.getCurrentSession().save(partidoParticipante);
                this.sessionFactory.getCurrentSession().flush();

                ResultActions result = mockMvc
                                .perform(post("/participantes/" + ej.getId() + "/asignacion-equipo")
                                                .param("equipo", String.valueOf(equipo2.getId()))
                                                .sessionAttr("EMAIL", "test@example.com")
                                                .header("referer", "/partido/1"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/partido/1"));

                String FlashErrorMessage = (String) result.andReturn().getFlashMap().get("listaParticipantesError");
                assertNull(FlashErrorMessage);

                String flashSuccessMessage = (String) result.andReturn().getFlashMap().get("listaParticipantesSuccess");
                assertThat(flashSuccessMessage, is("Equipo asignado correctamente"));

                PartidoParticipante partidoParticipanteActualizado = sessionFactory.getCurrentSession()
                                .get(PartidoParticipante.class, partidoParticipante.getId());
                assertNotNull(partidoParticipanteActualizado);
        }

        @Test
        @Transactional
        @Rollback
        public void debeRetornarMensajeDeErrorCuandoNoExisteElParticipante() throws Exception {
                // Crear un usuario para el equipo
                Usuario creador = new Usuario("usuario1", "password", "email@example.com", "usernameCreador");
                this.sessionFactory.getCurrentSession().save(creador);

                // Crear equipos
                Equipo equipo1 = new Equipo("EQUIPO_1", creador, LocalDateTime.now());
                this.sessionFactory.getCurrentSession().save(equipo1);

                this.sessionFactory.getCurrentSession().flush();

                ResultActions result = mockMvc.perform(post("/participantes/1/asignacion-equipo")
                                .param("equipo", String.valueOf(equipo1.getId()))
                                .sessionAttr("EMAIL", "test@example.com")
                                .header("referer", "/partido/1"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/partido/1"));

                String flashErrorMessage = (String) result.andReturn().getFlashMap().get("listaParticipantesError");
                assertThat(flashErrorMessage, is("No se encontró el participante"));

                String flashSuccessMessage = (String) result.andReturn().getFlashMap().get("listaParticipantesSuccess");
                assertNull(flashSuccessMessage);
        }

        @Test
        public void deberiaRedirigirAlLoginSiNoHaySesion() throws Exception {
                mockMvc.perform(post("/participantes/1/asignacion-equipo")
                                .param("equipo", "1")
                                .header("referer", "/partido/1"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/login"));
        }
}