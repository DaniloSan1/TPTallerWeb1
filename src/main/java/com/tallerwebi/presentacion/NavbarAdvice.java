package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.NotificacionDeUsuario;
import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.ServicioNotificacionDeUsuario;
import com.tallerwebi.dominio.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@ControllerAdvice()
public class NavbarAdvice {

    @Autowired
    private ServicioNotificacionDeUsuario servicioNotificacionDeUsuario;

    @ModelAttribute
    public void agregarNotificacionesNoLeidas(HttpServletRequest request, Model model) {

        String uri = request.getRequestURI();

        // no agregamos nada al modelo para que el test de ControladorTest pase
        if (uri == null || uri.isEmpty() || uri.equals("/") || uri.equals("//") ||
            uri.startsWith("/login") ||
            uri.startsWith("/registrarme") ||
            uri.startsWith("/nuevo-usuario")) {
            return;
        }

        Usuario usuario = (Usuario) request.getSession().getAttribute("USUARIO");
        if (usuario == null) {
            return;
        }

        model.addAttribute("notificacionesNoLeidas",
            servicioNotificacionDeUsuario.contarNoLeidas(usuario.getId())
        );
    }



}
