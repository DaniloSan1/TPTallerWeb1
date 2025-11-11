package com.tallerwebi.dominio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public String crearPago(String titulo, String descripcion, BigDecimal monto, Long reservaId) throws Exception {
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
                    .currencyId("ARS")
                    .build();

            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(itemRequest);

            PreferenceBackUrlsRequest backUrlsRequest = PreferenceBackUrlsRequest.builder()
                    .success("https://noninternationally-variolitic-diedra.ngrok-free.dev/spring/pago/exito/")
                    .failure("https://noninternationally-variolitic-diedra.ngrok-free.dev/spring/pago/error/")
                    .pending("https://noninternationally-variolitic-diedra.ngrok-free.dev/spring/pago/pendiente/")
                    .build();

            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backUrlsRequest)
                    //.autoReturn("all")
                    .build();

            System.out.println("Creando preferencia MercadoPago: " + titulo + " | Monto: " + monto);

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            if (preference == null || preference.getId() == null) {
                throw new Exception("No se pudo generar la preferencia de pago");
            }

            System.out.println(" Preferencia creada con ID: " + preference.getId());
            return preference.getId();

        } catch (MPApiException e) {
            System.err.println(" Error de API MercadoPago:");
            System.err.println("Status: " + e.getStatusCode());
            System.err.println("Response: " + e.getApiResponse().getContent());
            throw new Exception("Error API MercadoPago: " + e.getMessage(), e);
        } catch (Exception e) {
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

    @Override
    public void guardarPagoTorneo(Torneo torneo, Usuario usuario, String preferenciaId, Double monto) {
        Pago pago = new Pago();
        pago.setTorneo(torneo);
        pago.setUsuario(usuario);
        pago.setPreferenciaId(preferenciaId);
        pago.setMonto(monto);
        pago.setEstado("Pendiente");
        pago.setFechaCreacion(LocalDateTime.now());
        repositorioPago.guardarPago(pago);
    }

    @Override
    public Pago obtenerPorPreferencia(String preferenceId) {
        return repositorioPago.obtenerPorPreferencia(preferenceId);
    }

    @Override
    public void actualizarPago(Pago pago) {
        repositorioPago.actualizarPago(pago);
    }
}
