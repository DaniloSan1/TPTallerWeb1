package com.tallerwebi.punta_a_punta;

import com.microsoft.playwright.*;
import com.tallerwebi.punta_a_punta.vistas.VistaLogin;
import com.tallerwebi.punta_a_punta.vistas.VistaDetallePartido;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

public class VistaDetallePartidoE2E {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    VistaLogin vistaLogin;
    VistaDetallePartido vistaDetallePartido;

    @BeforeAll
    static void abrirNavegador() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
    }

    @AfterAll
    static void cerrarNavegador() {
        playwright.close();
    }

    @BeforeEach
    void crearContextoYPagina() {
        ReiniciarDB.limpiarBaseDeDatos();

        context = browser.newContext();
        Page page = context.newPage();
        vistaLogin = new VistaLogin(page);
    }

    @AfterEach
    void cerrarContexto() {
        context.close();
    }

    @Test
    void deberiaPermitirAlUsuarioUnirseAlPartido() throws MalformedURLException {
        dadoQueElUsuarioEstaLogueado("test@unlam.edu.ar", "test");
        cuandoElUsuarioNavegaAlDetalleDelPartido(2L);
        entoncesElBotonUnirseDeberiaEstarVisible();
        cuandoElUsuarioHaceClickEnUnirse();
        entoncesDeberiaVerUnMensajeDeExito();
        entoncesElBotonDeberiaCambiarAAbandonar();
    }

    private void dadoQueElUsuarioEstaLogueado(String email, String password) {
        vistaLogin.escribirEMAIL(email);
        vistaLogin.escribirClave(password);
        vistaLogin.darClickEnIniciarSesion();
    }

    private void cuandoElUsuarioNavegaAlDetalleDelPartido(Long partidoId) {
        vistaDetallePartido = new VistaDetallePartido(context.pages().get(0), partidoId);
    }

    private void cuandoElUsuarioHaceClickEnUnirse() {
        vistaDetallePartido.darClickEnUnirse();
        vistaDetallePartido.seleccionarEquipo("3"); // Equipo 1 for partido 1
        vistaDetallePartido.confirmarUnion();
    }

    private void entoncesDeberiaVerUnMensajeDeExito() {
        String mensaje = vistaDetallePartido.obtenerMensajeDeExito();
        assertThat("Te has unido al partido correctamente.", equalToIgnoringCase(mensaje));
    }

    private void entoncesElBotonUnirseDeberiaEstarVisible() {
        assertThat(vistaDetallePartido.estaBotonUnirseVisible(), is(true));
    }

    private void entoncesElBotonDeberiaCambiarAAbandonar() {
        assertThat(vistaDetallePartido.estaBotonAbandonarVisible(), is(true));
        assertThat(vistaDetallePartido.estaBotonUnirseVisible(), is(false));
    }
}
