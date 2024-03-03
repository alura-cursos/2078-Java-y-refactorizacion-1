package com.alura.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class MascotaService {

    public void listarMascotasDelRefugio() throws IOException, InterruptedException {
        System.out.println("Escriba el id o nombre del refugio:");
        String idONombre = new Scanner(System.in).nextLine();

        HttpClient client = HttpClient.newHttpClient();
        String uri = "http://localhost:8080/refugios/" + idONombre + "/pets";
        HttpResponse<String> response = dispararRequestGet(client, uri);
        int statusCode = response.statusCode();
        if (statusCode == 404 || statusCode == 500) {
            System.out.println("ID o nombre no registrado!");
        }
        String responseBody = response.body();
        JsonArray jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();
        System.out.println("Mascotas registradas:");
        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            long id = jsonObject.get("id").getAsLong();
            String tipo = jsonObject.get("tipo").getAsString();
            String nombre = jsonObject.get("nombre").getAsString();
            String raza = jsonObject.get("raza").getAsString();
            int edad = jsonObject.get("edad").getAsInt();
            System.out.println(id + " - " + tipo + " - " + nombre + " - " + raza + " - " + edad + " año(s)");
        }
    }

    public void importarMascotasEnElRefugio() throws IOException, InterruptedException {
        System.out.println("Escriba el id o nombre del refugio:");
        String idONombre = new Scanner(System.in).nextLine();

        System.out.println("Escriba el nombre del archivo CSV:");
        String nombreArchivo = new Scanner(System.in).nextLine();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(nombreArchivo));
        } catch (IOException e) {
            System.out.println("Error al cargar el archivo: " + nombreArchivo);
        }
        String line;
        while ((line = reader.readLine()) != null) {
            String[] campos = line.split(",");
            String tipo = campos[0];
            String nombre = campos[1];
            String raza = campos[2];
            int edad = Integer.parseInt(campos[3]);
            String color = campos[4];
            Float peso = Float.parseFloat(campos[5]);

            JsonObject json = new JsonObject();
            json.addProperty("tipo", tipo.toUpperCase());
            json.addProperty("nombre", nombre);
            json.addProperty("raza", raza);
            json.addProperty("edad", edad);
            json.addProperty("color", color);
            json.addProperty("peso", peso);

            HttpClient client = HttpClient.newHttpClient();
            String uri = "http://localhost:8080/refugios/" + idONombre + "/pets";
            HttpResponse<String> response = dispararRequestPost(client, uri, json);
            int statusCode = response.statusCode();
            String responseBody = response.body();
            if (statusCode == 200) {
                System.out.println("Mascota registrada exitosamente: " + nombre);
            } else if (statusCode == 404) {
                System.out.println("Id o nombre del refugio no encontado!");
                break;
            } else if (statusCode == 400 || statusCode == 500) {
                System.out.println("Error al registrar el pet: " + nombre);
                System.out.println(responseBody);
                break;
            }
        }
        reader.close();
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
