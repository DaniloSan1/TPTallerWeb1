package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.StatusResultMatchersExtensionsKt.isEqualTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestInfraestructuraConfig.class})
public class RepositorioUsuarioImplTest {
    @Autowired
    private SessionFactory sessionFactory;
    private RepositorioUsuario repositorioUsuario;
    @BeforeEach
    public void setUp() {
        repositorioUsuario = new RepositorioUsuarioImpl(sessionFactory);
    }

  @Test
  @Transactional
  @Rollback
    public void debeGuardarUnUsuario(){
    Usuario usuarioAGuardar = new Usuario();
    usuarioAGuardar.setNombre("nombre");
    usuarioAGuardar.setApellido("apellido");
    usuarioAGuardar.setEmail("email");
    usuarioAGuardar.setUsername("username");

    repositorioUsuario.guardar(usuarioAGuardar);
    sessionFactory.getCurrentSession().flush();

    String hql = "FROM Usuario u WHERE u.email = :email";
    Query query = sessionFactory.getCurrentSession().createQuery(hql);
    query.setParameter("email", "email");
    Usuario usuario = (Usuario) query.getSingleResult();

    assertThat(usuario, equalTo(usuarioAGuardar));
    }

    @Test
    @Transactional
    @Rollback
    public void debeDevolverElUsuarioBuscadoAlBuscarPorEmail(){
        Usuario usuarioGuardado = new Usuario("nombre", "apellido", "email","username");
        repositorioUsuario.guardar(usuarioGuardado);
        sessionFactory.getCurrentSession().flush();

        Usuario usuarioBuscado = repositorioUsuario.buscar("email");
        assertThat(usuarioBuscado, equalTo(usuarioGuardado));
    }

    @Test
    @Transactional
    @Rollback
    public void debeDevolverElUsuarioBuscadoAlBuscarPorEmailYContraseña(){
        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setEmail("email");
        usuarioGuardado.setPassword("password");
        usuarioGuardado.setUsername("username");
        repositorioUsuario.guardar(usuarioGuardado);
        sessionFactory.getCurrentSession().flush();
        Usuario usuarioBuscado = repositorioUsuario.buscarUsuario("email","password");
        assertThat(usuarioBuscado, equalTo(usuarioGuardado));
    }

    @Test
    @Transactional
    @Rollback
    public void debeDevolverElUsuarioBuscadoAlBuscarPorId(){
        Usuario usuarioGuardado = new Usuario("nombre", "apellido", "email","username");
        repositorioUsuario.guardar(usuarioGuardado);
        Long idUsuario = usuarioGuardado.getId();
        sessionFactory.getCurrentSession().flush();

        Usuario usuarioBuscado = repositorioUsuario.buscarPorId(idUsuario);
        assertThat(usuarioBuscado, equalTo(usuarioGuardado));
    }

}
