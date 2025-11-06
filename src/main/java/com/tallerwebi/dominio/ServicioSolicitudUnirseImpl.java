package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tallerwebi.infraestructura.RepositorioSolicitudUnirse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ServicioSolicitudUnirseImpl implements ServicioSolicitudUnirse {

    private final RepositorioSolicitudUnirse repo;
    private final ServicioPartido servicioPartido;

    public ServicioSolicitudUnirseImpl(RepositorioSolicitudUnirse repo, ServicioPartido servicioPartido) {
        this.repo = repo;
        this.servicioPartido = servicioPartido;
    }

    @Override
    public String crearInvitacion(Long partidoId, Usuario creador, String emailDestino) {
        // verifica que el creador sea el creador del partido
        var partido = servicioPartido.obtenerPorId(partidoId);
        // Permitimos enviar invitaciones si eres el creador o si ya te uniste al partido
        boolean esCreador = partido.getCreador() != null && partido.getCreador().getId().equals(creador.getId());
        boolean esParticipante = partido.validarParticipanteExistente(creador.getId());
        if (!esCreador && !esParticipante) {
            throw new RuntimeException("Solo el creador o un participante del partido puede enviar invitaciones.");
        }

        // crea la solicitud
        SolicitudUnirse solicitud = new SolicitudUnirse();
        solicitud.setPartido(partido);
    // quien genera la invitación
    solicitud.setCreador(creador);
        solicitud.setEmailDestino(emailDestino);
        solicitud.setToken(UUID.randomUUID().toString());
        solicitud.setCreada(LocalDateTime.now());
        solicitud.setVence(LocalDateTime.now().plusDays(7)); // vence en 7 días
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);

        repo.guardar(solicitud);

        // Construimos el link público hacia la página del partido con token como query param
        String link = System.getProperty("app.baseurl", "http://localhost:8080/spring") + "/partidos/" + partido.getId() + "?token=" + solicitud.getToken();

        // Intentamos enviar el email si hay configuración SMTP disponible (propiedades del sistema)
        try {
            String host = System.getProperty("mail.smtp.host");
            if (host != null && !host.isEmpty()) {
                String port = System.getProperty("mail.smtp.port", "25");
                String user = System.getProperty("mail.smtp.user");
                String pass = System.getProperty("mail.smtp.password");

                java.util.Properties props = new java.util.Properties();
                props.put("mail.smtp.host", host);
                props.put("mail.smtp.port", port);
                props.put("mail.smtp.auth", (user != null && pass != null) ? "true" : "false");
                props.put("mail.smtp.starttls.enable", System.getProperty("mail.smtp.starttls.enable", "false"));

                javax.mail.Session session;
                if (user != null && pass != null) {
                    session = javax.mail.Session.getInstance(props, new javax.mail.Authenticator() {
                        protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                            return new javax.mail.PasswordAuthentication(user, pass);
                        }
                    });
                } else {
                    session = javax.mail.Session.getInstance(props);
                }

                javax.mail.Message message = new javax.mail.internet.MimeMessage(session);
                message.setFrom(new javax.mail.internet.InternetAddress(user != null ? user : "no-reply@example.com"));
                message.setRecipients(javax.mail.Message.RecipientType.TO, javax.mail.internet.InternetAddress.parse(emailDestino));
                message.setSubject("Invitación para unirte a un partido");
                message.setText("Has sido invitado a un partido. Haz clic en el siguiente link para ver el detalle y aceptar: " + link);

                javax.mail.Transport.send(message);
            }
        } catch (Exception e) {
            // No queremos fallar la creación por un error de envío de correo. Solo lo logueamos.
            System.err.println("No se pudo enviar el email de invitación: " + e.getMessage());
            e.printStackTrace();
        }

        // Devolvemos el link público (no sólo el token) para que el controlador pueda mostrarlo al usuario
        return link;
    }

    @Override
    public MensajeResultado aceptarPorToken(String token, Usuario quienAcepta) {
        var optSolicitud = repo.buscarPorToken(token);
        if (optSolicitud.isEmpty()) {
            return MensajeResultado.error("Token inválido.");
        }
        var solicitud = optSolicitud.get();

        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
            return MensajeResultado.error("La solicitud ya fue procesada.");
        }

        if (solicitud.getVence().isBefore(LocalDateTime.now())) {
            solicitud.setEstado(EstadoSolicitud.EXPIRO);
            repo.guardar(solicitud);
            return MensajeResultado.error("La invitación ha expirado.");
        }

        // Aquí podrías agregar lógica para añadir al usuario al partido

        solicitud.setEstado(EstadoSolicitud.ACEPTADA);
        repo.guardar(solicitud);

        return MensajeResultado.ok("Has aceptado la invitación al partido.");
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudUnirse> listarPorPartido(Long partidoId) {
        var partido = servicioPartido.obtenerPorId(partidoId);
        return repo.listarPorPartidoYEstado(partido, EstadoSolicitud.PENDIENTE);
    }
}
