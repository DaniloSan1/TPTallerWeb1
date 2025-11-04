package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.UsernameExistenteException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping("/perfil")
public class ControladorUsuario {
    private final ServicioUsuario servicioUsuario;
    private ServicioLogin servicioLogin;

    public ControladorUsuario(ServicioLogin servicioLogin, ServicioUsuario servicioUsuario) {
        this.servicioLogin = servicioLogin;
        this.servicioUsuario = servicioUsuario;
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

    @GetMapping("/editar")
    public String editar(ModelMap modelo, HttpServletRequest request) {
        String email = (String) request.getSession().getAttribute("EMAIL");
        if (email != null) {
            Usuario usuario = servicioLogin.buscarPorEmail(email);
            modelo.addAttribute("usuario", usuario);
            return "editarPerfil";
        }

        return "redirect:/login";
    }

    @PostMapping("/editar")
    public String guardarCambios(@ModelAttribute("usuario") Usuario usuarioEditado, HttpServletRequest request,
            ModelMap modelo) {
        String email = (String) request.getSession().getAttribute("EMAIL");
        if (email != null) {
            try {
                Usuario usuario = servicioLogin.buscarPorEmail(email);
                Usuario usuarioExistente = servicioUsuario.buscarPorUsername(usuarioEditado.getUsername());
                if (usuarioExistente != null && !usuarioExistente.getId().equals(usuarioEditado.getId())) {
                    throw new UsernameExistenteException();
                }

                usuario.setUsername(usuarioEditado.getUsername());
                usuario.setNombre(usuarioEditado.getNombre());
                usuario.setApellido(usuarioEditado.getApellido());
                usuario.setPosicionFavorita(usuarioEditado.getPosicionFavorita());

                servicioUsuario.modificarUsuario(usuario);

                return "redirect:/perfil";

            } catch (UsernameExistenteException e) {
                modelo.addAttribute("usuario", usuarioEditado);
                modelo.addAttribute("error", e.getMessage());
                return "editarPerfil";
            }

        }
        return "redirect:/login";
    }
}
