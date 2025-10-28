package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/perfil")
public class ControladorUsuario {
    private ServicioLogin servicioLogin;

    public ControladorUsuario(ServicioLogin servicioLogin) {
        this.servicioLogin = servicioLogin;
    }

    @GetMapping("/ver/id/{id}")
    public String verPerfilDeOtroJugador(@PathVariable Long id, ModelMap modelo) {
        Usuario usuarioAVer = servicioLogin.buscarPorId(id);

        if (usuarioAVer == null) {
            return "redirect:/home";
        }

        modelo.addAttribute("usuarioAVer", usuarioAVer);
        return "perfilDeOtroJugador";
    }

    @GetMapping("/ver/username/{username}")
    public String verPerfilDeOtroJugador(@PathVariable String username, ModelMap modelo, HttpServletRequest request) {
        Usuario usuarioAVer = servicioLogin.buscarPorUsername(username);

        if (usuarioAVer == null) {
            return "redirect:/home";
        }

        String usernameABuscar = (String) request.getSession().getAttribute("USERNAME");
        if (usuarioAVer.getUsername().equals(usernameABuscar)) {
            return "redirect:/perfil";
        }

        modelo.addAttribute("usuarioAVer", usuarioAVer);
        return "perfilDeOtroJugador";
    }


    @GetMapping
    public String perfil(ModelMap modelo, HttpServletRequest request) {
        String email = (String) request.getSession().getAttribute("EMAIL");
        if (email != null) {
            Usuario usuario = servicioLogin.buscarPorEmail(email);
            modelo.addAttribute("usuario", usuario);
            modelo.put("currentPage", "perfil");
            return "perfil";
        }
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return new ModelAndView("redirect:/login");
    }
}
