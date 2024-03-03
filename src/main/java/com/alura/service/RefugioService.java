package com.alura.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class RefugioService {

    public void listarRefugios() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String uri = "http://localhost:8080/refugios";
        HttpResponse<String> response = dispararRequestGet(client, uri);
        String responseBody = response.body();
        JsonArray jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();
        System.out.println("Refugios registrados:");
        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            long id = jsonObject.get("id").getAsLong();
            String nombre = jsonObject.get("nombre").getAsString();
            System.out.println(id + " - " + nombre);
        }
    }

    public void registrarRefugio() throws IOException, InterruptedException {
        System.out.println("Escriba el nombre del refugio:");
        String nombre = new Scanner(System.in).nextLine();
        System.out.println("Escriba el teléfono del refugio:");
        String telefono = new Scanner(System.in).nextLine();
        System.out.println("Escriba el email del refugio:");
        String email = new Scanner(System.in).nextLine();

        JsonObject json = new JsonObject();
        json.addProperty("nombre", nombre);
        json.addProperty("telefono", telefono);
        json.addProperty("email", email);

        HttpClient client = HttpClient.newHttpClient();
        String uri = "http://localhost:8080/refugios";
        HttpResponse<String> response = dispararRequestPost(client, uri, json);
        int statusCode = response.statusCode();
        String responseBody = response.body();
        if (statusCode == 200) {
            System.out.println("Refugio registrado exitosamente!");
            System.out.println(responseBody);
        } else if (statusCode == 400 || statusCode == 500) {
            System.out.println("Error al registrar el refugio:");
            System.out.println(responseBody);
        }
    }

    private HttpResponse<String> dispararRequestGet(HttpClient client, String uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> dispararRequestPost(HttpClient client, String uri, JsonObject json)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
