package com.example.casholocal;

public class MovimientoModel {
    private long id;
    private long cuentaId;
    private String tipo; // "ingreso" o "egreso"
    private double monto;
    private String descripcion;
    private String fecha;
    private Double latitud;
    private Double longitud;
    private String categoria;

    public MovimientoModel(long id, long cuentaId, String tipo, double monto, String descripcion, String fecha, Double latitud, Double longitud, String categoria) {
        this.id = id;
        this.cuentaId = cuentaId;
        this.tipo = tipo;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.latitud = latitud;
        this.longitud = longitud;
        this.categoria = categoria;
    }

    public long getId() { return id; }
    public long getCuentaId() { return cuentaId; }
    public String getTipo() { return tipo; }
    public double getMonto() { return monto; }
    public String getDescripcion() { return descripcion; }
    public String getFecha() { return fecha; }
    public Double getLatitud() { return latitud; }
    public Double getLongitud() { return longitud; }
    public String getCategoria() { return categoria; }
}