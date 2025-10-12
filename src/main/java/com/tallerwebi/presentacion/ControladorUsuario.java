package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/perfil")
public class ControladorUsuario {
    private ServicioLogin serivicioLogin;
    public ControladorUsuario(ServicioLogin serivicioLogin) {
        this.serivicioLogin = serivicioLogin;
    }
    @GetMapping
    public String perfil(ModelMap modelo, HttpServletRequest request) {
        String email = (String) request.getSession().getAttribute("EMAIL");
        if(email != null){
            Usuario usuario = serivicioLogin.buscarPorEmail(email);
            modelo.addAttribute("usuario", usuario);
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







