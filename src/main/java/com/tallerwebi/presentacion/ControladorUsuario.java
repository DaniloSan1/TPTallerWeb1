package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuario")
public class ControladorUsuario {
private final ServicioUsuario servicioUsuario;
@Autowired
public ControladorUsuario(ServicioUsuario servicioUsuario) {
    this.servicioUsuario = servicioUsuario;
}

@GetMapping("/registro")
public String vistaRegistro(Model modelo){
modelo.addAttribute("usuario", new Usuario());
return "registro";
}

@PostMapping("/registro")
public String registrarUsuario(@ModelAttribute("usuario") Usuario usuario, Model modelo){
servicioUsuario.registrarUsuario(usuario);
modelo.addAttribute("mensaje", "Usuario registrado correctamente");
return "login";
}

@GetMapping("/login")
public String vistaLogin(Model modelo){
    modelo.addAttribute("usuario", new Usuario());
    return "login";
}

@PostMapping("/login")
public String loginUsuario(@RequestParam String email,@RequestParam String password, Model modelo) {
    Usuario usuario = servicioUsuario.buscarPorEmailYPassword(email,password);
    modelo.addAttribute("usuario", usuario);
    return "perfil";
}

    @GetMapping("/perfil/{email}")
    public String vistaPerfil(@PathVariable String email, Model modelo){
    Usuario usuario = servicioUsuario.buscarPorEmail(email);
    modelo.addAttribute("usuario",usuario);
    return "perfil";
    }
}








