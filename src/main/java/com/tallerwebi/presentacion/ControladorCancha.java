package com.tallerwebi.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tallerwebi.dominio.ServicioCancha;
@Controller
@RequestMapping("/canchas")
public class ControladorCancha {
    private final ServicioCancha servicioCancha;
    @Autowired
    public ControladorCancha(ServicioCancha servicioCancha) {
        this.servicioCancha = servicioCancha;
    }
    @GetMapping
    public String listarCanchas(ModelMap model) {
        model.put("canchas", servicioCancha.obtenerCancha());
        return "canchas";
    }
    @PostMapping("/reservar/{id}")
    public String reservarCancha(@PathVariable Long id, 
                                 @RequestParam String horario, 
                                 @RequestParam String usuario) {
        servicioCancha.reservarCancha(id, horario, usuario);
        return "redirect:/canchas";
    }
    @PostMapping("/cancelar/{id}")
    public String cancelarCancha(@PathVariable Long id, 
                                 @RequestParam String horario) {
        servicioCancha.cancelarCancha(id, horario);
        return "redirect:/canchas";
    }
}