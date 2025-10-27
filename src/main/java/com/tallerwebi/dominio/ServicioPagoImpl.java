package com.tallerwebi.dominio;

import java.math.BigDecimal;

import java.util.List;

import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;

public class ServicioPagoImpl implements ServicioPago {
    
    @Override
    public String crearPago(String titulo, String descripcion, BigDecimal monto) throws Exception {
        if (titulo == null || titulo.isEmpty()) {
            throw new Exception("El titulo es obligatorio");
        }
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("El monto debe ser mayor a cero");
        }
        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .title(titulo)
                .description(descripcion != null ? descripcion : "Pago de reserva de cancha")
                .currencyId("ARS")
                .unitPrice(monto)
                .quantity(1)
                .build();
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(List.of(itemRequest))
                .build();
        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);
        return preference.getInitPoint();
    }
}
