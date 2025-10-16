package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.UsuarioExistenteException;
import com.tallerwebi.dominio.excepcion.UsuarioNoEncontradoException;
import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioUsuarioImplTest {
    private ServicioUsuario servicioUsuarioImpl;
    private RepositorioUsuario repositorioUsuario;

    @BeforeEach
    public void init(){
        repositorioUsuario = mock(RepositorioUsuarioImpl.class);
        servicioUsuarioImpl = new ServicioUsuarioImpl(repositorioUsuario);

    }

    @Test
    public void alBuscarUnUsuarioRegistradoPorEmailYPasswordMeDeberiaDevolverElUsuarioCorrecto() throws UsuarioNoEncontradoException {

        Usuario usuarioABuscar = new Usuario();
        usuarioABuscar.setEmail("email@email");
        usuarioABuscar.setPassword("password");

        when(repositorioUsuario.buscarUsuario("email@email","password")).thenReturn(usuarioABuscar);
        Usuario usuarioRetornado = servicioUsuarioImpl.buscarPorEmailYPassword("email@email", "password");

        assertThat(usuarioABuscar, equalTo(usuarioRetornado));
    }

    @Test
    public void queAlBuscarUnUsuarioQueNoEstaRegistradoDevuelvaUnError() throws UsuarioNoEncontradoException {
        Usuario usuarioABuscar = new Usuario();
        usuarioABuscar.setEmail("email@email");
        usuarioABuscar.setPassword("password");

        when(repositorioUsuario.buscarUsuario("email@email","password")).thenReturn(null);
        assertThrows(UsuarioNoEncontradoException.class, () -> {
            servicioUsuarioImpl.buscarPorEmailYPassword("email@email", "password");
        } );
    }

    @Test
    public void queSePuedaRegistrarUnUsuarioNuevo() throws UsuarioNoEncontradoException, UsuarioExistenteException {
        Usuario usuarioARegistrar = new Usuario();
        usuarioARegistrar.setEmail("email@email");
        usuarioARegistrar.setPassword("password");
        when(repositorioUsuario.buscarUsuario("email@email","password")).thenReturn(null);
        servicioUsuarioImpl.registrarUsuario(usuarioARegistrar);
        when(repositorioUsuario.buscarUsuario("email@email","password")).thenReturn(usuarioARegistrar);
        Usuario usuarioEncontrado = servicioUsuarioImpl.buscarPorEmailYPassword("email@email", "password");
        assertThat(usuarioARegistrar, equalTo(usuarioEncontrado));
    }


    @Test
    public void queDevuelvaErrorCuandoUnUsuarioExistenteSeQuiereRegistrar() throws UsuarioExistenteException {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setEmail("email@email");
        usuarioExistente.setPassword("password");

        when(repositorioUsuario.buscar("email@email"))
                .thenReturn(usuarioExistente);

        assertThrows(UsuarioExistenteException.class, () -> {
            servicioUsuarioImpl.registrarUsuario(usuarioExistente);
        });
    }


    //Test de modificar usuario pendiente hasta que implemente la parte de editar perfil

}
