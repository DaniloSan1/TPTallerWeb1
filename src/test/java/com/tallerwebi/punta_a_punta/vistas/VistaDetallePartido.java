package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class VistaDetallePartido extends VistaWeb {

    private Long partidoId;

    public VistaDetallePartido(Page page, Long partidoId) {
        super(page);
        this.partidoId = partidoId;
        page.navigate("localhost:8080/spring/partidos/" + partidoId);
    }

    public String obtenerMensajeDeExito() {
        return this.obtenerTextoDelElemento("#alertSuccess");
    }

    public String obtenerMensajeDeError() {
        return this.obtenerTextoDelElemento("#alertError");
    }

    public void darClickEnUnirse() {
        this.darClickEnElElemento("#btn-unirse");
    }

    public void seleccionarEquipo(String equipoId) {
        page.selectOption("#equipoSelect", equipoId);
    }

    public void confirmarUnion() {
        this.darClickEnElElemento("#confirmJoin");
    }

    public boolean estaBotonUnirseVisible() {
        return page.locator("#btn-unirse").isVisible();
    }

    public boolean estaBotonAbandonarVisible() {
        return page.locator("button").filter(new Locator.FilterOptions().setHasText("Abandonar partido")).isVisible();
    }

    public String obtenerTituloDelPartido() {
        return this.obtenerTextoDelElemento("h1.mb-2");
    }
}