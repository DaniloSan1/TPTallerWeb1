package com.tallerwebi.dominio;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ServicioReseniaCanchaImplTest {

    private RepositorioReseniaCancha repositorioMock;
    private ServicioReseniaCancha servicio;

    @BeforeEach
    public void init() {
        repositorioMock = Mockito.mock(RepositorioReseniaCancha.class);
        servicio = new ServicioReseniaCanchaImpl(repositorioMock);
    }

    @Test
    public void queCuandoNoHayReseniaPreviaSeGuardaLaResenia() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Cancha cancha = new Cancha();
        cancha.setId(10L);

        ReseniaCancha res = new ReseniaCancha(5, "Buena", usuario, cancha);

        Mockito.when(repositorioMock.buscarReseniaPreviaDelUsuarioAUnaCanchaDeterminada(usuario.getId(), cancha.getId()))
               .thenReturn(Collections.emptyList());

        servicio.agregarReseniaCancha(res);

        Mockito.verify(repositorioMock, times(1)).guardar(res);
    }

    @Test
    public void queCuandoHayReseniaPreviaNoSeGuardaLaResenia() {
        Usuario usuario = new Usuario();
        usuario.setId(2L);
        Cancha cancha = new Cancha();
        cancha.setId(20L);

        ReseniaCancha previa = new ReseniaCancha(4, "Prev", usuario, cancha);
        ReseniaCancha nueva = new ReseniaCancha(3, "Nueva", usuario, cancha);

        Mockito.when(repositorioMock.buscarReseniaPreviaDelUsuarioAUnaCanchaDeterminada(usuario.getId(), cancha.getId()))
               .thenReturn(List.of(previa));

        servicio.agregarReseniaCancha(nueva);

        Mockito.verify(repositorioMock, never()).guardar(nueva);
    }

    @Test
    public void queObtenerReseniasPorCanchaDevuelveListaDelRepositorio() {
        Long canchaId = 5L;
        ReseniaCancha r1 = Mockito.mock(ReseniaCancha.class);
        ReseniaCancha r2 = Mockito.mock(ReseniaCancha.class);
        Mockito.when(repositorioMock.obtenerReseniasPorCancha(canchaId)).thenReturn(List.of(r1, r2));

        List<ReseniaCancha> resultado = servicio.obtenerReseniasPorCancha(canchaId);
        Assertions.assertEquals(2, resultado.size());
    }

    @Test
    public void queObtenerReseniasPorUsuarioDevuelveListaDelRepositorio() {
        Long usuarioId = 7L;
        ReseniaCancha r1 = Mockito.mock(ReseniaCancha.class);
        Mockito.when(repositorioMock.obtenerReseniasPorUsuario(usuarioId)).thenReturn(List.of(r1));

        List<ReseniaCancha> resultado = servicio.obtenerReseniasPorUsuario(usuarioId);
        Assertions.assertEquals(1, resultado.size());
    }

    @Test
    public void calcularCalificacionPromedioDevuelveCeroCuandoNoHayResenias() {
        Long canchaId = 11L;
        Mockito.when(repositorioMock.contarReseniasPorCancha(canchaId)).thenReturn(0);

        double promedio = servicio.calcularCalificacionPromedioCancha(canchaId);
        Assertions.assertEquals(0.0, promedio);
    }

    @Test
    public void calcularCalificacionPromedioCalculaPromedioCorrecto() {
        Long canchaId = 12L;
        ReseniaCancha r1 = new ReseniaCancha(4, "", new Usuario(), new Cancha());
        ReseniaCancha r2 = new ReseniaCancha(5, "", new Usuario(), new Cancha());

        Mockito.when(repositorioMock.contarReseniasPorCancha(canchaId)).thenReturn(2);
        Mockito.when(repositorioMock.obtenerReseniasPorCancha(canchaId)).thenReturn(List.of(r1, r2));

        double promedio = servicio.calcularCalificacionPromedioCancha(canchaId);
        Assertions.assertEquals(4.5, promedio);
    }

    @Test
    public void verificarSiElUsuarioPuedeReseniarDevuelveTrueCuandoNoHayPrevia() {
        Usuario usuario = new Usuario();
        usuario.setId(3L);
        Cancha cancha = new Cancha();
        cancha.setId(30L);

        Mockito.when(repositorioMock.buscarReseniaPreviaDelUsuarioAUnaCanchaDeterminada(usuario.getId(), cancha.getId()))
               .thenReturn(Collections.emptyList());

        Boolean puede = servicio.verificarSiElUsuarioPuedeReseniarEsaCancha(usuario, cancha);
        Assertions.assertTrue(puede);
    }

    @Test
    public void verificarSiElUsuarioPuedeReseniarDevuelveFalseCuandoHayPrevia() {
        Usuario usuario = new Usuario();
        usuario.setId(4L);
        Cancha cancha = new Cancha();
        cancha.setId(40L);

        ReseniaCancha previa = new ReseniaCancha(2, "x", usuario, cancha);
        Mockito.when(repositorioMock.buscarReseniaPreviaDelUsuarioAUnaCanchaDeterminada(usuario.getId(), cancha.getId()))
               .thenReturn(List.of(previa));

        Boolean puede = servicio.verificarSiElUsuarioPuedeReseniarEsaCancha(usuario, cancha);
        Assertions.assertFalse(puede);
    }

    @Test
    public void obtenerReseniaCanchaPorIdLanzaExcepcionCuandoNoExiste() {
        Long id = 999L;
        Mockito.when(repositorioMock.obtenerReseniaCanchaPorId(id)).thenReturn(null);

        Assertions.assertThrows(IllegalArgumentException.class, () -> servicio.obtenerReseniaCanchaPorId(id));
    }

    @Test
    public void obtenerReseniaCanchaPorIdDevuelveReseniaCuandoExiste() {
        ReseniaCancha r = new ReseniaCancha(5, "ok", new Usuario(), new Cancha());
        r.setId(50L);
        Mockito.when(repositorioMock.obtenerReseniaCanchaPorId(50L)).thenReturn(r);

        ReseniaCancha resultado = servicio.obtenerReseniaCanchaPorId(50L);
        Assertions.assertEquals(50L, resultado.getId());
    }

    @Test
    public void editarReseniaCanchaLlamaActualizarEnRepositorio() {
        ReseniaCancha r = new ReseniaCancha(3, "c", new Usuario(), new Cancha());
        servicio.editarReseniaCancha(r);
        Mockito.verify(repositorioMock, times(1)).actualizar(r);
    }

}
