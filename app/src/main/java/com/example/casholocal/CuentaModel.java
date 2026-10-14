package com.example.casholocal;

public class CuentaModel {
    private long id;
    private String nombre;
    private String tipo;
    private double saldoInicial;

    public CuentaModel(long id, String nombre, String tipo, double saldoInicial) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.saldoInicial = saldoInicial;
    }

    public long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public double getSaldoInicial() { return saldoInicial; }
}