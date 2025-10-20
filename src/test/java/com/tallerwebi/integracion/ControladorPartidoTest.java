package com.tallerwebi.integracion;

import static org.hamcrest.MatcherAssert.assertThat;
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

		Cancha cancha = new Cancha("Cancha 1", null, null, "Direccion 1", Zona.NORTE);
		this.sessionFactory.getCurrentSession().save(cancha);

		Horario horario = new Horario(cancha, DayOfWeek.MONDAY, LocalTime.now(), LocalTime.now().plusHours(1));
		this.sessionFactory.getCurrentSession().save(horario);

		Usuario creador = new Usuario("usuario1", "password", "email@example.com");
		this.sessionFactory.getCurrentSession().save(creador);

		Reserva reserva = new Reserva(horario, creador, LocalDateTime.now().plusDays(1));
		this.sessionFactory.getCurrentSession().save(reserva);

		Partido nuevoPartido = new Partido(null, "Partido de prueba", "Descripción del partido",
				Nivel.INTERMEDIO,
				10,
				reserva,
				creador);

		this.sessionFactory.getCurrentSession().save(nuevoPartido);

		MvcResult result = this.mockMvc.perform(get("/partidos/{id}", nuevoPartido.getId())
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
					equalToIgnoringCase("usuario1"));
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
