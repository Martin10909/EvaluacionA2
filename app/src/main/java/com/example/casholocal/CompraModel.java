package com.example.casholocal;

public class CompraModel {
    private long id;
    private String titulo;
    private double monto;
    private String fecha;
    private boolean esRealizada;
    private Long contactoId;
    private String contactoNombre;
    private String categoria;
    private Double latitud;
    private Double longitud;

    public CompraModel(long id, String titulo, double monto, String fecha, boolean esRealizada, Long contactoId, String contactoNombre, String categoria, Double latitud, Double longitud) {
        this.id = id;
        this.titulo = titulo;
        this.monto = monto;
        this.fecha = fecha;
        this.esRealizada = esRealizada;
        this.contactoId = contactoId;
        this.contactoNombre = contactoNombre;
        this.categoria = categoria;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public long getId() { return id; }
    public String getTitulo() { return titulo; }
    public double getMonto() { return monto; }
    public String getFecha() { return fecha; }
    public boolean isEsRealizada() { return esRealizada; }
    public Long getContactoId() { return contactoId; }
    public String getContactoNombre() { return contactoNombre; }
    public String getCategoria() { return categoria; }
    public Double getLatitud() { return latitud; }
    public Double getLongitud() { return longitud; }
}