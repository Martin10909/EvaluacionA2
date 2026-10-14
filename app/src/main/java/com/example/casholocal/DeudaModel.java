package com.example.casholocal;

public class DeudaModel {
    private long id;
    private String concepto;
    private String contactoNombre;
    private double monto;
    private boolean esPagada;
    private String tipoDeuda; // "me_deben" o "yo_debo"
    private String fecha;

    public DeudaModel(long id, String concepto, String contactoNombre, double monto, boolean esPagada, String tipoDeuda, String fecha) {
        this.id = id;
        this.concepto = concepto;
        this.contactoNombre = contactoNombre;
        this.monto = monto;
        this.esPagada = esPagada;
        this.tipoDeuda = tipoDeuda;
        this.fecha = fecha;
    }

    public long getId() { return id; }
    public String getConcepto() { return concepto; }
    public String getContactoNombre() { return contactoNombre; }
    public double getMonto() { return monto; }
    public boolean isEsPagada() { return esPagada; }
    public String getTipoDeuda() { return tipoDeuda; }
    public String getFecha() { return fecha; }
}