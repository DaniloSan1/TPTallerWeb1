package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioSolicitudUnirse {
    String crearInvitacion(Long partidoId, Usuario creador, String emailDestino);

    MensajeResultado aceptarPorToken(String token, Usuario quienAcepta);

    List<SolicitudUnirse> listarPorPartido(Long partidoId);
    
    class MensajeResultado {
        public final boolean ok;
        public final String mensaje;
        public MensajeResultado(boolean ok, String mensaje) { this.ok = ok; this.mensaje = mensaje; }
        public static MensajeResultado ok(String m){ return new MensajeResultado(true, m); }
        public static MensajeResultado error(String m){ return new MensajeResultado(false, m); }
    }
}
