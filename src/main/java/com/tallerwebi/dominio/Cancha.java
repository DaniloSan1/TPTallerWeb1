package com.tallerwebi.dominio;

import java.util.ArrayList;
import java.util.List;


public class Cancha {
    private Long id;
    private String nombre;
    private String tipo;
    private List <Reserva> reservas = new ArrayList<>();
    private List <String> horariosDisponibles = new ArrayList<>();
    public Cancha(Long id, String nombre, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
    public Boolean estaDisponible() {
        return reservas.isEmpty();
    }
    public Boolean estaDisponible(String horario) {
        return reservas.stream().noneMatch(reserva -> reserva.getHorario().equals(horario));
    }
    public Boolean reservar(String horario, String usuario) {
        if (estaDisponible(horario)) {
            reservas.add(new Reserva(horario, usuario));
            return true;
        }
        return false;
    }
    public void cancelar(String horario) {
        reservas.removeIf(reserva -> reserva.getHorario().equals(horario));
    }
    public List<String> getHorariosDisponibles() {
        this.horariosDisponibles.clear();
        for (int i = 8; i <= 22; i++) {
            String horario = String.format("%02d:00", i);
            if (estaDisponible(horario)) {
                this.horariosDisponibles.add(horario);
            }
        }
        return this.horariosDisponibles;
    }
    public void setHorariosDisponibles(List<String> horariosDisponibles) {
        this.horariosDisponibles = horariosDisponibles;
    }
}
