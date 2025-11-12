package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ControladorAmigosTest {

    private ServicioUsuario servicioUsuarioMock;
    private ControladorAmigos controladorAmigos;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;

    @BeforeEach
    public void init() {
        servicioUsuarioMock = mock(ServicioUsuario.class);
        controladorAmigos = new ControladorAmigos(servicioUsuarioMock);
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        when(requestMock.getSession()).thenReturn(sessionMock);
    }

    @Test
    public void queCuandoNoHayEmailEnSesionRetorneListaVacia() {
        // Given
        when(sessionMock.getAttribute("EMAIL")).thenReturn(null);

        // When
        List<DetalleAmigoLista> amigos = controladorAmigos.listarAmigos(requestMock);

        // Then
        assertTrue(amigos.isEmpty());
        verify(sessionMock).getAttribute("EMAIL");
        verifyNoInteractions(servicioUsuarioMock);
    }

    @Test
    public void queCuandoUsuarioNoExisteRetorneListaVacia() {
        // Given
        String email = "test@example.com";
        when(sessionMock.getAttribute("EMAIL")).thenReturn(email);
        when(servicioUsuarioMock.buscarPorEmail(email)).thenReturn(null);

        // When
        List<DetalleAmigoLista> amigos = controladorAmigos.listarAmigos(requestMock);

        // Then
        assertTrue(amigos.isEmpty());
        verify(sessionMock).getAttribute("EMAIL");
        verify(servicioUsuarioMock).buscarPorEmail(email);
        verifyNoMoreInteractions(servicioUsuarioMock);
    }

    @Test
    public void queCuandoUsuarioTieneAmigosRetorneListaDeAmigos() {
        // Given
        String email = "test@example.com";
        Usuario usuario = mock(Usuario.class);
        Usuario amigo1 = mock(Usuario.class);
        Usuario amigo2 = mock(Usuario.class);
        when(amigo1.getId()).thenReturn(1L);
        when(amigo1.getNombre()).thenReturn("Juan");
        when(amigo1.getApellido()).thenReturn("Perez");
        when(amigo1.getUsername()).thenReturn("juanp");
        when(amigo2.getId()).thenReturn(2L);
        when(amigo2.getNombre()).thenReturn("Maria");
        when(amigo2.getApellido()).thenReturn("Gomez");
        when(amigo2.getUsername()).thenReturn("mariag");
        List<Usuario> amigosList = Arrays.asList(amigo1, amigo2);

        when(sessionMock.getAttribute("EMAIL")).thenReturn(email);
        when(servicioUsuarioMock.buscarPorEmail(email)).thenReturn(usuario);
        when(servicioUsuarioMock.listarAmigosDeUsuario(usuario)).thenReturn(amigosList);

        // When
        List<DetalleAmigoLista> amigos = controladorAmigos.listarAmigos(requestMock);

        // Then
        assertEquals(2, amigos.size());
        assertEquals(1L, amigos.get(0).getId());
        assertEquals("Juan", amigos.get(0).getNombre());
        assertEquals("Perez", amigos.get(0).getApellido());
        assertEquals("juanp", amigos.get(0).getUsername());
        assertEquals(2L, amigos.get(1).getId());
        assertEquals("Maria", amigos.get(1).getNombre());
        assertEquals("Gomez", amigos.get(1).getApellido());
        assertEquals("mariag", amigos.get(1).getUsername());
        verify(sessionMock).getAttribute("EMAIL");
        verify(servicioUsuarioMock).buscarPorEmail(email);
        verify(servicioUsuarioMock).listarAmigosDeUsuario(usuario);
    }

    @Test
    public void queFiltreAmigosNulos() {
        // Given
        String email = "test@example.com";
        Usuario usuario = mock(Usuario.class);
        Usuario amigo1 = mock(Usuario.class);
        when(amigo1.getId()).thenReturn(1L);
        when(amigo1.getNombre()).thenReturn("Juan");
        when(amigo1.getApellido()).thenReturn("Perez");
        when(amigo1.getUsername()).thenReturn("juanp");
        List<Usuario> amigosList = Arrays.asList(amigo1, null);

        when(sessionMock.getAttribute("EMAIL")).thenReturn(email);
        when(servicioUsuarioMock.buscarPorEmail(email)).thenReturn(usuario);
        when(servicioUsuarioMock.listarAmigosDeUsuario(usuario)).thenReturn(amigosList);

        // When
        List<DetalleAmigoLista> amigos = controladorAmigos.listarAmigos(requestMock);

        // Then
        assertEquals(1, amigos.size());
        assertEquals(1L, amigos.get(0).getId());
        verify(sessionMock).getAttribute("EMAIL");
        verify(servicioUsuarioMock).buscarPorEmail(email);
        verify(servicioUsuarioMock).listarAmigosDeUsuario(usuario);
    }
}