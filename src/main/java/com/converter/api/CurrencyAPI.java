package com.converter.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CurrencyAPI {
    public JsonObject fetchRates(String apiUrl) throws IOException, InterruptedException {
        // Crear un cliente HTTP y una solicitud HTTP
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        // Enviar la solicitud y recibir la respuesta
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("Error al obtener las tasas de cambio. Código de estado: " + response.statusCode());
        }

        // Analizar la respuesta JSON utilizando Gson
        JsonParser parser = new JsonParser();
        return parser.parse(response.body()).getAsJsonObject();
    }
}
