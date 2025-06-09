package com.juanjojmnz.conversormonedas.Service;

import com.juanjojmnz.conversormonedas.DTO.ApiResponse;
import com.juanjojmnz.conversormonedas.DTO.MonedaInfoDTO;
import com.juanjojmnz.conversormonedas.Entities.Conversor;
import com.juanjojmnz.conversormonedas.Repository.ConversorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ConversorServicio {

    private final ConversorRepository conversionRepo;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_KEY = "4a3609c3b25f7ddea8d607f6";
    private static final String API_BASE_URL = "https://v6.exchangerate-api.com/v6/";
    
    private final Map<String, String> MONEDAS_NOMBRES = new HashMap<>() {{
        put("USD", "Dólar Estadounidense");
        put("EUR", "Euro");
        put("GBP", "Libra Esterlina");
        put("JPY", "Yen Japonés");
        put("AUD", "Dólar Australiano");
        put("CAD", "Dólar Canadiense");
        put("CHF", "Franco Suizo");
        put("CNY", "Yuan Chino");
        put("MXN", "Peso Mexicano");
        put("BRL", "Real Brasileño");
        put("ARS", "Peso Argentino");
        put("CLP", "Peso Chileno");
        put("PEN", "Sol Peruano");
        put("COP", "Peso Colombiano");
        put("VES", "Bolívar Venezolano");
    }};

    public List<MonedaInfoDTO> obtenerMonedasDisponibles() {
        try {
            String url = API_BASE_URL + API_KEY + "/latest/USD";
            ApiResponse response = restTemplate.getForObject(url, ApiResponse.class);
            
            if (response != null && response.getConversion_rates() != null) {
                List<MonedaInfoDTO> monedas = new ArrayList<>();
                Set<String> codigosMonedas = response.getConversion_rates().keySet();
                
                for (String codigo : codigosMonedas) {
                    String nombre = MONEDAS_NOMBRES.getOrDefault(codigo, codigo);
                    monedas.add(new MonedaInfoDTO(codigo, nombre));
                }
                
                // Ordenar por nombre de moneda
                monedas.sort(Comparator.comparing(MonedaInfoDTO::getNombre));
                return monedas;
            }
            throw new RuntimeException("No se pudieron obtener las monedas disponibles");
        } catch (RestClientException e) {
            throw new RuntimeException("Error al obtener las monedas disponibles: " + e.getMessage(), e);
        }
    }

public Conversor convertir(String origen, String destino, double cantidad) {
    try {
        // Primero obtener las tasas disponibles
        String url = API_BASE_URL + API_KEY + "/latest/" + origen;
        ApiResponse response = restTemplate.getForObject(url, ApiResponse.class);

        if (response != null && response.getConversion_rates() != null) {
            // Verificar si las monedas existen en la respuesta de la API
            if (!response.getConversion_rates().containsKey(destino)) {
                throw new IllegalArgumentException("Código de moneda destino no válido: " + destino);
            }

            Double tasaDestino = response.getConversion_rates().get(destino);
            double resultado = cantidad * tasaDestino;
            
            Conversor conversion = Conversor.builder()
                    .monedaOrigen(origen)
                    .monedaDestino(destino)
                    .cantidad(cantidad)
                    .resultado(resultado)
                    .fechaHora(LocalDateTime.now())
                    .build();

            return conversionRepo.save(conversion);
        }
        throw new RuntimeException("No se pudo obtener una respuesta válida de la API");
        
    } catch (RestClientException e) {
        throw new RuntimeException("Error en la conversión de moneda: " + e.getMessage(), e);
    }
}
}