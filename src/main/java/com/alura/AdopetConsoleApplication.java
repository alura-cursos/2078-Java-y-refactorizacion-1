package com.alura;

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

public class AdopetConsoleApplication {

    public static void main(String[] args) {
        System.out.println("##### BIENVENIDOS AL SISTEMA ADOPET-CONSOLE #####");
        try {
            int opcionElegida = 0;
            while (opcionElegida != 5) {
                System.out.println("\nESCRIBA EL NÚMERO DE LA OPERACIÓN DESEADA:");
                System.out.println("1 -> Listar refugios registrados");
                System.out.println("2 -> Registrar nuevo refugio");
                System.out.println("3 -> Listar mascotas del refugio");
                System.out.println("4 -> Importar mascotas del refugio");
                System.out.println("5 -> Salir");

                String textoEscrito = new Scanner(System.in).nextLine();
                opcionElegida = Integer.parseInt(textoEscrito);

                if (opcionElegida == 1) {
                    listarRefugios();
                } else if (opcionElegida == 2) {
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
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(uri))
                            .header("Content-Type", "application/json")
                            .method("POST", HttpRequest.BodyPublishers.ofString(json.toString()))
                            .build();

                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    int statusCode = response.statusCode();
                    String responseBody = response.body();
                    if (statusCode == 200) {
                        System.out.println("Refugio registrado exitosamente!");
                        System.out.println(responseBody);
                    } else if (statusCode == 400 || statusCode == 500) {
                        System.out.println("Error al registrar el refugio:");
                        System.out.println(responseBody);
                    }
                } else if (opcionElegida == 3) {
                    System.out.println("Escriba el id o nombre del refugio:");
                    String idONombre = new Scanner(System.in).nextLine();

                    HttpClient client = HttpClient.newHttpClient();
                    String uri = "http://localhost:8080/refugios/" + idONombre + "/pets";
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(uri))
                            .method("GET", HttpRequest.BodyPublishers.noBody())
                            .build();
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    int statusCode = response.statusCode();
                    if (statusCode == 404 || statusCode == 500) {
                        System.out.println("ID o nombre no registrado!");
                        continue;
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
                } else if (opcionElegida == 4) {
                    System.out.println("Escriba el id o nombre del refugio:");
                    String idONombre = new Scanner(System.in).nextLine();

                    System.out.println("Escriba el nombre del archivo CSV:");
                    String nombreArchivo = new Scanner(System.in).nextLine();

                    BufferedReader reader;
                    try {
                        reader = new BufferedReader(new FileReader(nombreArchivo));
                    } catch (IOException e) {
                        System.out.println("Error al cargar el archivo: " + nombreArchivo);
                        continue;
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
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(uri))
                                .header("Content-Type", "application/json")
                                .method("POST", HttpRequest.BodyPublishers.ofString(json.toString()))
                                .build();

                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
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
                } else if (opcionElegida == 5) {
                    break;
                } else {
                    System.out.println("NÚMERO INVÁLIDO!");
                    opcionElegida = 0;
                }
            }
            System.out.println("Finalizando el programa...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void listarRefugios() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String uri = "http://localhost:8080/refugios";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
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
}
