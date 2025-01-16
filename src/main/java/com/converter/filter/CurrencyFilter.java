package com.converter.filter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CurrencyFilter {
    private JsonObject ratesJson;
    private Map<String, Double> filteredCurrencies;

    public CurrencyFilter(JsonObject ratesJson) {
        this.ratesJson = ratesJson;
        this.filteredCurrencies = new HashMap<>();
    }

    // Método para mostrar las monedas disponibles
    public void displayAvailableCurrencies() {
        JsonObject rates = ratesJson.getAsJsonObject("conversion_rates");
        rates.entrySet().forEach(entry -> System.out.println(entry.getKey()));
    }

    // Método para filtrar monedas específicas
    public void filterCurrencies(List<String> currencies) throws IllegalArgumentException {
        JsonObject rates = ratesJson.getAsJsonObject("conversion_rates");

        // Verificar que todas las monedas sean válidas
        boolean allValid = currencies.stream().allMatch(rates::has);

        if (!allValid) {
            throw new IllegalArgumentException("All currencies must be valid. Please check your input.");
        }

        // Filtrar las monedas si son válidas
        filteredCurrencies = currencies.stream()
                .collect(Collectors.toMap(currency -> currency, currency -> rates.get(currency).getAsDouble()));

        System.out.println("Filtered Currencies:");
        filteredCurrencies.forEach((key, value) -> System.out.println(key + ": " + value));
    }

    // Método para guardar los resultados filtrados en un archivo JSON
    public void saveFilteredResults(String filename) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject resultJson = new JsonObject();

        // Crear un nuevo objeto JSON para las tasas
        JsonObject ratesObject = new JsonObject();

        // Agregar las monedas filtradas al objeto de tasas
        filteredCurrencies.forEach(ratesObject::addProperty);

        // Agregar los campos adicionales al JSON final
        resultJson.add("rates", ratesObject);
        resultJson.addProperty("result", "success");
        resultJson.addProperty("base", "USD");

        // Escribir el resultado en un archivo JSON
        try (Writer writer = new FileWriter(filename)) {
            gson.toJson(resultJson, writer);
        }
    }

    // Método para obtener la tasa de cambio entre dos monedas
    public double getExchangeRate(String monedaOrigen, String monedaDestino) {
        JsonObject rates = ratesJson.getAsJsonObject("conversion_rates");

        if (!rates.has(monedaOrigen) || !rates.has(monedaDestino)) {
            throw new IllegalArgumentException("Una o ambas monedas no están disponibles en las tasas de cambio.");
        }

        double tasaOrigen = rates.get(monedaOrigen).getAsDouble();
        double tasaDestino = rates.get(monedaDestino).getAsDouble();

        // Calcular la tasa de cambio directa entre las dos monedas
        return tasaDestino / tasaOrigen;
    }
}

