package com.alura.domain;

public class Refugio {

    public Refugio(String nombre, String telefono, String email) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
    }

    private Long id;
    private String nombre;
    private String telefono;
    private String email;

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}
