package com.tallerwebi.punta_a_punta.vistas;

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

    public boolean estaBotonUnirseVisible() {
        return page.locator("#btn-unirse").isVisible();
    }

    public boolean estaBotonAbandonarVisible() {
        return page.locator("button[name='leave']").isVisible();
    }

    public String obtenerTituloDelPartido() {
        return this.obtenerTextoDelElemento("h1.mb-2");
    }
}