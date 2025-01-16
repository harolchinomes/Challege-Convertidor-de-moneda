package com.converter;

import com.converter.api.CurrencyAPI;
import com.converter.filter.CurrencyFilter;
import com.google.gson.JsonObject;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            // Obtener tasas de cambio
            String apiUrl = "https://v6.exchangerate-api.com/v6/cee8ae258436ef9ca4c2b4a9/latest/USD";
            CurrencyAPI apiMoneda = new CurrencyAPI();
            JsonObject tasasJson = apiMoneda.fetchRates(apiUrl);

            // Filtrar y mostrar monedas disponibles
            CurrencyFilter filtro = new CurrencyFilter(tasasJson);

            Scanner escaner = new Scanner(System.in);
            int opcion;

            do {
                // Mostrar el menú
                System.out.println("************************************");
                System.out.println("  Sea bienvenido/a al Conversor de Moneda =] ");
                System.out.println("************************************");
                System.out.println("1) Dólar => Peso argentino");
                System.out.println("2) Peso argentino => Dólar");
                System.out.println("3) Dólar => Real brasileño");
                System.out.println("4) Real brasileño => Dólar");
                System.out.println("5) Dólar => Peso colombiano");
                System.out.println("6) Peso colombiano => Dólar");
                System.out.println("7) Salir");
                System.out.println("Elija una opción válida:");
                System.out.println("************************************");


                opcion = escaner.nextInt();
                escaner.nextLine(); // Consumir el salto de línea

                String monedaOrigen = "", monedaDestino = "";

                switch (opcion) {
                    case 1:
                        monedaOrigen = "USD";
                        monedaDestino = "ARS";
                        break;
                    case 2:
                        monedaOrigen = "ARS";
                        monedaDestino = "USD";
                        break;
                    case 3:
                        monedaOrigen = "USD";
                        monedaDestino = "BRL";
                        break;
                    case 4:
                        monedaOrigen = "BRL";
                        monedaDestino = "USD";
                        break;
                    case 5:
                        monedaOrigen = "USD";
                        monedaDestino = "COP";
                        break;
                    case 6:
                        monedaOrigen = "COP";
                        monedaDestino = "USD";
                        break;
                    case 7:
                        System.out.println("Saliendo del programa. ¡Gracias por usar el conversor de moneda!");
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, intente nuevamente.");
                }

                if (opcion >= 1 && opcion <= 6) {
                    try {
                        double tasaCambio = filtro.getExchangeRate(monedaOrigen, monedaDestino);
                        System.out.println("Ingrese la cantidad en " + monedaOrigen + " a convertir:");
                        double cantidad = escaner.nextDouble();
                        escaner.nextLine(); // Consumir el salto de línea

                        double resultado = cantidad * tasaCambio;
                        System.out.printf("%.2f %s equivalen a %.2f %s%n", cantidad, monedaOrigen, resultado, monedaDestino);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }

            } while (opcion != 7);

        } catch (Exception e) {
            System.out.println("Ocurrió un error: " + e.getMessage());
        }
    }
}
