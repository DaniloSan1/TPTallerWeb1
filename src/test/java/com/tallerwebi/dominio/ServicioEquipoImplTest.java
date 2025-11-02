package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.RepositorioEquipoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class ServicioEquipoImplTest {
    private ServicioEquipo servicioEquipoImpl;
    private RepositorioEquipo repositorioEquipo;

    @BeforeEach
    public void init() {
        repositorioEquipo = mock(RepositorioEquipoImpl.class);
        servicioEquipoImpl = new ServicioEquipoImpl(repositorioEquipo);
    }

    @Test
    public void queSePuedaCrearUnEquipo() {
        Usuario creador = new Usuario("nombre", "password", "email@test.com", "username");
        String nombreEquipo = "Equipo Test";

        Equipo equipoCreado = servicioEquipoImpl.crearEquipo(nombreEquipo, creador);

        verify(repositorioEquipo).guardar(any(Equipo.class));
        assertThat(equipoCreado.getName(), equalTo(nombreEquipo));
        assertThat(equipoCreado.getCreadoPor(), equalTo(creador));
    }

    @Test
    public void queSePuedaBuscarUnEquipoPorId() {
        Long id = 1L;
        Equipo equipoEsperado = new Equipo("Equipo Test", new Usuario(), java.time.LocalDateTime.now());
        equipoEsperado.setId(id);

        when(repositorioEquipo.buscarPorId(id)).thenReturn(equipoEsperado);

        Equipo equipoEncontrado = servicioEquipoImpl.buscarPorId(id);

        assertThat(equipoEncontrado, equalTo(equipoEsperado));
    }
}