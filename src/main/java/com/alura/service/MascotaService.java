package com.alura.service;

import com.alura.client.ClientHttpConfiguration;
import com.alura.domain.Mascota;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class MascotaService {

    private ClientHttpConfiguration client;

    public MascotaService(ClientHttpConfiguration client) {
        this.client = client;
    }

    public void listarMascotasDelRefugio() throws IOException, InterruptedException {
        System.out.println("Escriba el id o nombre del refugio:");
        String idONombre = new Scanner(System.in).nextLine();

        String uri = "http://localhost:8080/refugios/" + idONombre + "/pets";
        HttpResponse<String> response = client.dispararRequestGet(uri);
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

            Mascota mascota = new Mascota(tipo, nombre, raza, edad, color, peso);

            String uri = "http://localhost:8080/refugios/" + idONombre + "/pets";
            HttpResponse<String> response = client.dispararRequestPost(uri, mascota);
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
}
