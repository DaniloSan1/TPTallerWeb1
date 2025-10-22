package com.tallerwebi.integracion;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.tallerwebi.dominio.Cancha;
import com.tallerwebi.dominio.Horario;
import com.tallerwebi.dominio.Nivel;
import com.tallerwebi.dominio.Partido;
import com.tallerwebi.dominio.PartidoParticipante;
import com.tallerwebi.dominio.Reserva;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.Zona;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import com.tallerwebi.presentacion.DetallePartido;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class })
public class ControladorPartidoTest {

	private Usuario usuarioMock;

	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;

	@Autowired
	private SessionFactory sessionFactory;

	@BeforeEach
	public void init() {
		usuarioMock = mock(Usuario.class);
		when(usuarioMock.getEmail()).thenReturn("dami@unlam.com");
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	@Transactional
	@Rollback
	public void debeRetornarDetallePartidoCuandoSeNavegaADetallePartidoConUnIdExistente() throws Exception {

		Cancha cancha = new Cancha("Cancha 1", "Direccion 1", 20, "CESPED", Zona.NORTE);
		this.sessionFactory.getCurrentSession().save(cancha);
		this.sessionFactory.getCurrentSession().flush();

		Horario horario = new Horario(cancha, DayOfWeek.MONDAY, LocalTime.now(), LocalTime.now().plusHours(1));
		this.sessionFactory.getCurrentSession().save(horario);
		this.sessionFactory.getCurrentSession().flush();

		Usuario creador = new Usuario("usuario1", "password", "email@example.com");
		creador.setApellido("apellido");
		this.sessionFactory.getCurrentSession().save(creador);
		this.sessionFactory.getCurrentSession().flush();

		Reserva reserva = new Reserva(horario, creador, LocalDateTime.now().plusDays(1));
		this.sessionFactory.getCurrentSession().save(reserva);
		this.sessionFactory.getCurrentSession().flush();

		Partido nuevoPartido = new Partido(null, "Partido de prueba", "Descripción del partido",
				Nivel.INTERMEDIO,
				10,
				reserva,
				creador);

		this.sessionFactory.getCurrentSession().save(nuevoPartido);
		this.sessionFactory.getCurrentSession().flush();

		// agrega participantes
		Usuario participante1 = new Usuario("usuario2", "password2", "email2@example.com");
		participante1.setNombre("participante1");
		participante1.setApellido("apellido1");
		Usuario participante2 = new Usuario("usuario3", "password3", "email3@example.com");
		participante2.setNombre("participante2");
		participante2.setApellido("apellido2");
		Usuario participante3 = new Usuario("usuario4", "password4", "email4@example.com");
		participante3.setNombre("participante3");
		participante3.setApellido("apellido3");
		Usuario participante4 = new Usuario("usuario5", "password5", "email5@example.com");
		participante4.setNombre("participante4");
		participante4.setApellido("apellido4");
		this.sessionFactory.getCurrentSession().save(participante1);
		this.sessionFactory.getCurrentSession().save(participante2);
		this.sessionFactory.getCurrentSession().save(participante3);
		this.sessionFactory.getCurrentSession().save(participante4);

		PartidoParticipante pp1 = new PartidoParticipante(nuevoPartido, participante1);
		PartidoParticipante pp2 = new PartidoParticipante(nuevoPartido, participante2);
		PartidoParticipante pp3 = new PartidoParticipante(nuevoPartido, participante3);
		PartidoParticipante pp4 = new PartidoParticipante(nuevoPartido, participante4);

		nuevoPartido.getParticipantes().add(pp1);
		nuevoPartido.getParticipantes().add(pp2);
		nuevoPartido.getParticipantes().add(pp3);
		nuevoPartido.getParticipantes().add(pp4);

		this.sessionFactory.getCurrentSession().save(pp1);
		this.sessionFactory.getCurrentSession().save(pp2);
		this.sessionFactory.getCurrentSession().save(pp3);
		this.sessionFactory.getCurrentSession().save(pp4);

		Long partidoId = nuevoPartido.getId();
		MvcResult result = this.mockMvc.perform(get("/partidos/{id}", partidoId)
				.sessionAttr("EMAIL", "email@example.com"))
				.andExpect(status().isOk())
				.andReturn();

		ModelAndView modelAndView = result.getModelAndView();
		assertThat("ModelAndView should not be null", modelAndView != null);
		if (modelAndView != null) {
			assertThat(modelAndView.getViewName(), equalToIgnoringCase("detalle-partido"));

			// Verificar que el partido está en el modelo como DetallePartido
			Object partidoEnModelo = modelAndView.getModel().get("partido");
			assertNotNull(partidoEnModelo, "El modelo debe contener un partido");

			// El partido en el modelo es de tipo DetallePartido
			DetallePartido detallePartido = (DetallePartido) partidoEnModelo;

			// Verificar las propiedades del DetallePartido
			assertThat("El título del partido debe coincidir", detallePartido.getTitulo(),
					equalToIgnoringCase("Partido de prueba"));
			assertThat("La descripción del partido debe coincidir", detallePartido.getDescripcion(),
					equalToIgnoringCase("Descripción del partido"));
			assertThat("La zona del partido debe coincidir", detallePartido.getZona().toString(),
					equalToIgnoringCase(Zona.NORTE.toString()));
			assertThat("El nivel del partido debe coincidir", detallePartido.getNivel().toString(),
					equalToIgnoringCase(Nivel.INTERMEDIO.toString()));
			assertThat("La cancha del partido debe coincidir", detallePartido.getCancha(),
					equalToIgnoringCase("Cancha 1"));
			assertThat("El creador del partido debe coincidir", detallePartido.getCreador(),
					equalToIgnoringCase("usuario1 apellido"));
			assertThat("El cupo máximo del partido debe coincidir", detallePartido.getCupoMaximo(),
					equalTo(10));
			assertThat("El número de participantes del partido debe coincidir",
					detallePartido.getParticipantes().size(),
					equalTo(4));
		}
	}

	@Test
	@Transactional
	@Rollback
	public void debeRetornarLaPaginaDetallePartidoConErrorCuandoElPartidoNoExiste() throws Exception {
		MvcResult result = this.mockMvc.perform(get("/partidos/{id}", 999L)
				.sessionAttr("EMAIL", "email@example.com"))
				.andExpect(status().isOk())
				.andReturn();

		ModelAndView modelAndView = result.getModelAndView();
		assertThat("ModelAndView should not be null", modelAndView != null);
		if (modelAndView != null) {
			assertThat(modelAndView.getViewName(), equalToIgnoringCase("detalle-partido"));

			// Verificar que el partido está en el modelo como DetallePartido
			Object errorEnModelo = modelAndView.getModel().get("error");
			assertNotNull(errorEnModelo, "El modelo debe contener un error");

			// El partido en el modelo es de tipo DetallePartido
			String errorDetalle = (String) errorEnModelo;
			assertThat("El mensaje de error debe coincidir", errorDetalle,
					equalToIgnoringCase("No se encontró el partido"));
		}
	}
}
