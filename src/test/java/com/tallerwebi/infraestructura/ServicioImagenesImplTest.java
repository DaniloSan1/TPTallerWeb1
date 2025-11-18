package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.AdaptadorAWSS3;
import com.tallerwebi.dominio.ServicioImagenes;
import com.tallerwebi.dominio.excepcion.ErrorSubiendoImagenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ServicioImagenesImplTest {

    @Mock
    private AdaptadorAWSS3 adaptadorAWSS3;

    @Mock
    private MultipartFile multipartFile;

    private ServicioImagenes servicioImagenes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servicioImagenes = new ServicioImagenesImpl(adaptadorAWSS3);
    }

    @Test
    void subirImagen_debeLlamarAlAdaptadorYRetornarLaUrl() throws Exception {
        // Given
        String expectedUrl = "https://s3.amazonaws.com/bucket/image.jpg";
        when(adaptadorAWSS3.subirArchivo(any(MultipartFile.class))).thenReturn(expectedUrl);

        // When
        String result = servicioImagenes.subirImagen(multipartFile);

        // Then
        assertEquals(expectedUrl, result);
        verify(adaptadorAWSS3, times(1)).subirArchivo(multipartFile);
    }

    @Test
    void subirImagen_cuandoAdaptadorLanzaExcepcion_debeLanzarErrorSubiendoImagenException() throws Exception {
        // Given
        Exception expectedException = new Exception("Error al subir archivo");
        when(adaptadorAWSS3.subirArchivo(any(MultipartFile.class))).thenThrow(expectedException);

        // When & Then
        ErrorSubiendoImagenException thrown = assertThrows(ErrorSubiendoImagenException.class, () -> {
            servicioImagenes.subirImagen(multipartFile);
        });
        assertTrue(thrown.getMessage().contains("Error al subir la imagen"));
        verify(adaptadorAWSS3, times(1)).subirArchivo(multipartFile);
    }
}