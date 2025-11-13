package com.tallerwebi.infraestructura;

import java.util.List;

import javax.transaction.Transactional;

import com.tallerwebi.dominio.Cancha;
import com.tallerwebi.dominio.ReseniaCancha;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateTestInfraestructuraConfig.class })
public class RepositorioReseniaCanchaImplTest {

	@Autowired
	private SessionFactory sessionFactory;

	private RepositorioReseniaCanchaImpl repositorio;

	private Usuario usuario1;
	private Usuario usuario2;
	private Cancha cancha1;
	private Cancha cancha2;

	@BeforeEach
	public void init() {
		this.repositorio = new RepositorioReseniaCanchaImpl(this.sessionFactory);

		usuario1 = new Usuario();
		usuario1.setNombre("Usuario1");
		usuario1.setApellido("Uno");
		usuario1.setEmail("u1@example.com");
		usuario1.setUsername("u1");

		usuario2 = new Usuario();
		usuario2.setNombre("Usuario2");
		usuario2.setApellido("Dos");
		usuario2.setEmail("u2@example.com");
		usuario2.setUsername("u2");

		cancha1 = new Cancha();
		cancha1.setNombre("Cancha A");
		cancha1.setDireccion("Calle A");

		cancha2 = new Cancha();
		cancha2.setNombre("Cancha B");
		cancha2.setDireccion("Calle B");

		this.sessionFactory.getCurrentSession().save(usuario1);
		this.sessionFactory.getCurrentSession().save(usuario2);
		this.sessionFactory.getCurrentSession().save(cancha1);
		this.sessionFactory.getCurrentSession().save(cancha2);
		this.sessionFactory.getCurrentSession().flush();
	}

	@Test
	@Transactional
	@Rollback
	public void queCuandoguardeUnaReseniaLapuedaRecuperarDesdeSuCancha() {
		ReseniaCancha r = new ReseniaCancha(5, "Muy buena", usuario1, cancha1);
		repositorio.guardar(r);
		this.sessionFactory.getCurrentSession().flush();

		List<ReseniaCancha> reseniasCancha = repositorio.obtenerReseniasPorCancha(cancha1.getId());
		assertEquals(1, reseniasCancha.size());
		assertTrue(reseniasCancha.get(0).getComentario().equals("Muy buena"));
	}

    @Test
	@Transactional
	@Rollback
	public void queCuandoguardeUnaReseniaLapuedaRecuperarDesdeSuUsuario() {
		ReseniaCancha r = new ReseniaCancha(5, "Muy buena", usuario1, cancha1);
		repositorio.guardar(r);
		this.sessionFactory.getCurrentSession().flush();

		List<ReseniaCancha> reseniasCancha = repositorio.obtenerReseniasPorUsuario(usuario1.getId());
		assertEquals(1, reseniasCancha.size());
		assertTrue(reseniasCancha.get(0).getComentario().equals("Muy buena"));
	}

	@Test
	@Transactional
	@Rollback
	public void queCuandoTengoMasDeUnaReseniaParaUnaCanchaMeDevuelvaElListadoDeLasMismas() {
		ReseniaCancha r1 = new ReseniaCancha(5, "R1", usuario1, cancha1);
		ReseniaCancha r2 = new ReseniaCancha(4, "R2", usuario2, cancha1);
		repositorio.guardar(r1);
		repositorio.guardar(r2);
		this.sessionFactory.getCurrentSession().flush();

		List<ReseniaCancha> res = repositorio.obtenerReseniasPorCancha(cancha1.getId());
		assertEquals(2, res.size());
        assertTrue(res.contains(r1));
        assertTrue(res.contains(r2));
	}


	@Test
	@Transactional
	@Rollback
	public void queCuandoTengoMasDeUnaReseniaDelMismoUsuarioMeDevuelvaElListadoDeLasMismas() {
		ReseniaCancha r1 = new ReseniaCancha(5, "R1", usuario1, cancha1);
		ReseniaCancha r2 = new ReseniaCancha(4, "R2", usuario1, cancha2);
		repositorio.guardar(r1);
		repositorio.guardar(r2);
		this.sessionFactory.getCurrentSession().flush();

		List<ReseniaCancha> res = repositorio.obtenerReseniasPorUsuario(usuario1.getId());
		assertEquals(2, res.size());
         assertTrue(res.contains(r1));
        assertTrue(res.contains(r2));
	}

	@Test
	@Transactional
	@Rollback
	public void queCuandoCuentoLasReseniasDeUnaCanchaMeDevuelvaElValorCorrecto() {
		ReseniaCancha r1 = new ReseniaCancha(5, "R1", usuario1, cancha1);
		ReseniaCancha r2 = new ReseniaCancha(4, "R2", usuario2, cancha1);
		repositorio.guardar(r1);
		repositorio.guardar(r2);
		this.sessionFactory.getCurrentSession().flush();

		int count = repositorio.contarReseniasPorCancha(cancha1.getId());
		assertEquals(2, count);
	}

	@Test
	@Transactional
	@Rollback
	public void testBuscarReseniaPreviaDelUsuarioAUnaCanchaDeterminada() {
		ReseniaCancha r1 = new ReseniaCancha(5, "Prev", usuario1, cancha1);
		repositorio.guardar(r1);
		this.sessionFactory.getCurrentSession().flush();

		List<ReseniaCancha> previa = repositorio.buscarReseniaPreviaDelUsuarioAUnaCanchaDeterminada(usuario1.getId(), cancha1.getId());
		assertNotNull(previa);
		assertEquals(1, previa.size());
		assertEquals("Prev", previa.get(0).getComentario());
	}

	@Test
	@Transactional
	@Rollback
	public void queCuandoActualizoUnaReseniaSeActualiceCorrectamente() {
		ReseniaCancha r1 = new ReseniaCancha(5, "ParaActualizar", usuario1, cancha1);
		repositorio.guardar(r1);
		this.sessionFactory.getCurrentSession().flush();

		ReseniaCancha encontrada = repositorio.obtenerReseniaCanchaPorId(r1.getId());
		assertNotNull(encontrada);
		assertEquals("ParaActualizar", encontrada.getComentario());

		encontrada.setComentario("Actualizada");
		encontrada.setCalificacion(3);
		repositorio.actualizar(encontrada);
		this.sessionFactory.getCurrentSession().flush();

		ReseniaCancha actualizada = repositorio.obtenerReseniaCanchaPorId(encontrada.getId());
		assertEquals("Actualizada", actualizada.getComentario());
		assertEquals(3, actualizada.getCalificacion());
	}

	@Test
	@Transactional
	@Rollback
	public void testObtenerReseniaCanchaPorIdInexistenteDevuelveNull() {
		ReseniaCancha resultado = repositorio.obtenerReseniaCanchaPorId(99999L);
		assertEquals(null, resultado);
	}

}
