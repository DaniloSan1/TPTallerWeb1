package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
@Controller
public class ControladorAmigos {

    @Autowired
    private ServicioUsuario servicioUsuario;

    public ControladorAmigos() {
    }

    public ControladorAmigos(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    @GetMapping("/amigos")
    @ResponseBody
    public List<DetalleAmigoLista> listarAmigos(HttpServletRequest request) {
        String email = (String) request.getSession().getAttribute("EMAIL");
        if (email == null) {
            return List.of(); // return empty list
        }
        Usuario usuario = servicioUsuario.buscarPorEmail(email);
        if (usuario == null) {
            return List.of();
        }
        List<Usuario> amigos = servicioUsuario.listarAmigosDeUsuario(usuario);
        return amigos.stream()
                .filter(amigo -> amigo != null)
                .map(amigo -> new DetalleAmigoLista(amigo.getId(), amigo.getNombre(), amigo.getApellido(),
                        amigo.getUsername()))
                .collect(Collectors.toList());
    }
}
