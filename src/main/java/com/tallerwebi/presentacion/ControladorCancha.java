package com.tallerwebi.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String reservarCancha(@PathVariable Long id) {
        servicioCancha.reservarCancha(id);
        return "redirect:/canchas";
    }
    @PostMapping("/cancelar/{id}")
    public String cancelarCancha(@PathVariable Long id) {
        servicioCancha.cancelarCancha(id);
        return "redirect:/canchas";
    }
}