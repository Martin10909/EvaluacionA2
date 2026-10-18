package com.example.casholocal;

public class CuentaModel {
    private long id;
    private String nombre;
    private String tipo;
    private double saldoInicial;
    private String moneda;
    private String descripcion;
    private String fechaCreacion;

    public CuentaModel(long id, String nombre, String tipo, double saldoInicial, String moneda, String descripcion, String fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.saldoInicial = saldoInicial;
        this.moneda = moneda;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
    }

    public CuentaModel(long id, String nombre, String tipo, double saldoInicial) {
        this(id, nombre, tipo, saldoInicial, "CLP", "Cuenta personal", "Hoy");
    }

    public long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public double getSaldoInicial() { return saldoInicial; }
    public String getMoneda() { return moneda; }
    public String getDescripcion() { return descripcion; }
    public String getFechaCreacion() { return fechaCreacion; }
}