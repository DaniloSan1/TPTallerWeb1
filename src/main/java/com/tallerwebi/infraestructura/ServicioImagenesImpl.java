package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.AdaptadorAWSS3;
import com.tallerwebi.dominio.ServicioImagenes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ServicioImagenesImpl implements ServicioImagenes {

    private final AdaptadorAWSS3 adaptadorAWSS3;

    @Autowired
    public ServicioImagenesImpl(AdaptadorAWSS3 adaptadorAWSS3) {
        this.adaptadorAWSS3 = adaptadorAWSS3;
    }

    @Override
    public String subirImagen(MultipartFile insignia) throws Exception {
        return adaptadorAWSS3.subirArchivo(insignia);
    }

}