package com.example.casholocal;

public class ContactoModel {
    private long id;
    private String nombre;
    private String telefono;
    private String email;
    private String notas;

    public ContactoModel(long id, String nombre, String telefono, String email, String notas) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.notas = notas;
    }

    public long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
    public String getNotas() { return notas; }
}