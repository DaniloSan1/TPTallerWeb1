package com.tallerwebi.dominio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.resources.preference.Preference;

@Service
@Transactional
public class ServicioPagoImpl implements ServicioPago {

    private final RepositorioPago repositorioPago;

    @Autowired
    public ServicioPagoImpl(RepositorioPago repositorioPago) {
        this.repositorioPago = repositorioPago;
    }

    @Override
    public String crearPago(String titulo, String descripcion, BigDecimal monto) throws Exception {
        try {
            if (titulo == null || titulo.isEmpty()) {
                throw new IllegalArgumentException("El título es obligatorio");
            }
            if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("El monto debe ser mayor a cero");
            }

            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .title(titulo)
                    .description(descripcion != null ? descripcion : "Pago de reserva de cancha")
                    .unitPrice(monto)
                    .quantity(1)
                    .build();
            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(itemRequest);
            PreferenceBackUrlsRequest backUrlsRequest = PreferenceBackUrlsRequest.builder()
                    .success("http://localhost:8080/spring/pago/exito")
                    .failure("http://localhost:8080/spring/pago/error")
                    .pending("http://localhost:8080/spring/pago/pendiente")
                    .build();
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backUrlsRequest)
                    .build();

            System.err.println("About to create MercadoPago preference with title: " + titulo + ", amount: " + monto);

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            System.err.println("Preferencia de pago creada con ID: " + preference.getId());
            System.err.println(preference);
            if (preference == null || preference.getId() == null) {
                throw new Exception("No se pudo generar la preferencia de pago");
            }
            return preference.getId();

        } catch (MPApiException e) {
            System.err.println("Error de API de MercadoPago: " + e.getMessage());
            System.err.println("Status Code: " + e.getStatusCode());
            System.err.println("API Response: " + e.getApiResponse());
            System.err.println("API Response Content: " + e.getApiResponse().getContent());
            e.printStackTrace();
            throw new Exception("Error de API de MercadoPago: " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Error al crear el pago");
            System.err.println(e.getMessage());
            e.printStackTrace();
            throw new Exception("Error al crear el pago: " + e.getMessage(), e);
        }
    }

    @Override
    public void guardarPago(Reserva reserva, Usuario usuario, String preferenciaId, Double monto) {
        Pago pago = new Pago();
        pago.setReserva(reserva);
        pago.setUsuario(usuario);
        pago.setPreferenciaId(preferenciaId);
        pago.setMonto(monto);
        pago.setEstado("Pendiente");
        pago.setFechaCreacion(LocalDateTime.now());
        repositorioPago.guardarPago(pago);
    }
}