package com.alura.domain;

public class Mascota {

    public Mascota(String tipo, String nombre, String raza, int edad, String color, Float peso) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.raza = raza;
        this.edad = edad;
        this.color = color;
        this.peso = peso;
    }

    private String tipo;
    private String nombre;
    private String raza;
    private int edad;
    private String color;
    private Float peso;
}
