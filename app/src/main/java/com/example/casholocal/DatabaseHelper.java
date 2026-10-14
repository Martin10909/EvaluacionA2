package com.example.casholocal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "casholocal.db";
    private static final int DATABASE_VERSION = 3;

    // Tabla Cuentas
    public static final String TABLA_CUENTAS = "cuentas";
    public static final String COL_CUENTA_ID = "id";
    public static final String COL_CUENTA_NOMBRE = "nombre";
    public static final String COL_CUENTA_TIPO = "tipo";
    public static final String COL_CUENTA_SALDO_INICIAL = "saldo_inicial";

    // Tabla Movimientos
    public static final String TABLA_MOVIMIENTOS = "movimientos";
    public static final String COL_MOV_ID = "id";
    public static final String COL_MOV_CUENTA_ID = "cuenta_id";
    public static final String COL_MOV_TIPO = "tipo"; // "ingreso" o "egreso"
    public static final String COL_MOV_MONTO = "monto";
    public static final String COL_MOV_DESCRIPCION = "descripcion";
    public static final String COL_MOV_FECHA = "fecha";
    public static final String COL_MOV_LAT = "latitud";
    public static final String COL_MOV_LNG = "longitud";
    public static final String COL_MOV_CATEGORIA = "categoria";

    // Tabla Contactos
    public static final String TABLA_CONTACTOS = "contactos";
    public static final String COL_CONTACTO_ID = "id";
    public static final String COL_CONTACTO_NOMBRE = "nombre";
    public static final String COL_CONTACTO_TELEFONO = "telefono";
    public static final String COL_CONTACTO_EMAIL = "email";
    public static final String COL_CONTACTO_NOTAS = "notas";

    // Tabla Compras
    public static final String TABLA_COMPRAS = "compras";
    public static final String COL_COMPRA_ID = "id";
    public static final String COL_COMPRA_TITULO = "titulo";
    public static final String COL_COMPRA_MONTO = "monto";
    public static final String COL_COMPRA_FECHA = "fecha";
    public static final String COL_COMPRA_ES_REALIZADA = "es_realizada";
    public static final String COL_COMPRA_CONTACTO_ID = "contacto_id";
    public static final String COL_COMPRA_CATEGORIA = "categoria";

    // Tabla Gastos Compartidos
    public static final String TABLA_GASTOS_COMPARTIDOS = "gastos_compartidos";
    public static final String COL_GASTO_ID = "id";
    public static final String COL_GASTO_CONCEPTO = "concepto";
    public static final String COL_GASTO_MONTO_TOTAL = "monto_total";
    public static final String COL_GASTO_TIPO_DIVISION = "tipo_division";
    public static final String COL_GASTO_PAGADOR = "pagador";
    public static final String COL_GASTO_FECHA = "fecha";

    // Tabla Gasto Items
    public static final String TABLA_GASTO_ITEMS = "gasto_items";
    public static final String COL_ITEM_ID = "id";
    public static final String COL_ITEM_GASTO_ID = "gasto_id";
    public static final String COL_ITEM_NOMBRE = "nombre_item";
    public static final String COL_ITEM_PRECIO = "precio_item";

    // Tabla Deudas
    public static final String TABLA_DEUDAS = "deudas";
    public static final String COL_DEUDA_ID = "id";
    public static final String COL_DEUDA_GASTO_ID = "gasto_id";
    public static final String COL_DEUDA_CONTACTO = "contacto_nombre";
    public static final String COL_DEUDA_MONTO = "monto_deuda";
    public static final String COL_DEUDA_ES_PAGADA = "es_pagada";
    public static final String COL_DEUDA_TIPO = "tipo_deuda";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Cuentas
        db.execSQL("CREATE TABLE " + TABLA_CUENTAS + " (" +
                COL_CUENTA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CUENTA_NOMBRE + " TEXT, " +
                COL_CUENTA_TIPO + " TEXT, " +
                COL_CUENTA_SALDO_INICIAL + " REAL)");

        // Movimientos
        db.execSQL("CREATE TABLE " + TABLA_MOVIMIENTOS + " (" +
                COL_MOV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MOV_CUENTA_ID + " INTEGER, " +
                COL_MOV_TIPO + " TEXT, " +
                COL_MOV_MONTO + " REAL, " +
                COL_MOV_DESCRIPCION + " TEXT, " +
                COL_MOV_FECHA + " TEXT, " +
                COL_MOV_LAT + " REAL, " +
                COL_MOV_LNG + " REAL, " +
                COL_MOV_CATEGORIA + " TEXT, " +
                "FOREIGN KEY(" + COL_MOV_CUENTA_ID + ") REFERENCES " + TABLA_CUENTAS + "(" + COL_CUENTA_ID + ") ON DELETE CASCADE)");

        // Contactos
        db.execSQL("CREATE TABLE " + TABLA_CONTACTOS + " (" +
                COL_CONTACTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CONTACTO_NOMBRE + " TEXT, " +
                COL_CONTACTO_TELEFONO + " TEXT, " +
                COL_CONTACTO_EMAIL + " TEXT, " +
                COL_CONTACTO_NOTAS + " TEXT)");

        // Compras
        db.execSQL("CREATE TABLE " + TABLA_COMPRAS + " (" +
                COL_COMPRA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_COMPRA_TITULO + " TEXT, " +
                COL_COMPRA_MONTO + " REAL, " +
                COL_COMPRA_FECHA + " TEXT, " +
                COL_COMPRA_ES_REALIZADA + " INTEGER, " +
                COL_COMPRA_CONTACTO_ID + " INTEGER, " +
                COL_COMPRA_CATEGORIA + " TEXT, " +
                "FOREIGN KEY(" + COL_COMPRA_CONTACTO_ID + ") REFERENCES " + TABLA_CONTACTOS + "(" + COL_CONTACTO_ID + ") ON DELETE SET NULL)");

        // Gastos Compartidos
        db.execSQL("CREATE TABLE " + TABLA_GASTOS_COMPARTIDOS + " (" +
                COL_GASTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_GASTO_CONCEPTO + " TEXT, " +
                COL_GASTO_MONTO_TOTAL + " REAL, " +
                COL_GASTO_TIPO_DIVISION + " TEXT, " +
                COL_GASTO_PAGADOR + " TEXT, " +
                COL_GASTO_FECHA + " TEXT)");

        // Gasto Items
        db.execSQL("CREATE TABLE " + TABLA_GASTO_ITEMS + " (" +
                COL_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ITEM_GASTO_ID + " INTEGER, " +
                COL_ITEM_NOMBRE + " TEXT, " +
                COL_ITEM_PRECIO + " REAL, " +
                "FOREIGN KEY(" + COL_ITEM_GASTO_ID + ") REFERENCES " + TABLA_GASTOS_COMPARTIDOS + "(" + COL_GASTO_ID + ") ON DELETE CASCADE)");

        // Deudas
        db.execSQL("CREATE TABLE " + TABLA_DEUDAS + " (" +
                COL_DEUDA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DEUDA_GASTO_ID + " INTEGER, " +
                COL_DEUDA_CONTACTO + " TEXT, " +
                COL_DEUDA_MONTO + " REAL, " +
                COL_DEUDA_ES_PAGADA + " INTEGER, " +
                COL_DEUDA_TIPO + " TEXT, " +
                "FOREIGN KEY(" + COL_DEUDA_GASTO_ID + ") REFERENCES " + TABLA_GASTOS_COMPARTIDOS + "(" + COL_GASTO_ID + ") ON DELETE CASCADE)");

        // Insertar Cuentas por defecto
        db.execSQL("INSERT INTO " + TABLA_CUENTAS + " (" + COL_CUENTA_NOMBRE + ", " + COL_CUENTA_TIPO + ", " + COL_CUENTA_SALDO_INICIAL + ") VALUES ('Cuenta Principal', 'Bancaria', 1000.00)");
        db.execSQL("INSERT INTO " + TABLA_CUENTAS + " (" + COL_CUENTA_NOMBRE + ", " + COL_CUENTA_TIPO + ", " + COL_CUENTA_SALDO_INICIAL + ") VALUES ('Efectivo', 'Billetera', 250.00)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_DEUDAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_GASTO_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_GASTOS_COMPARTIDOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_COMPRAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_CONTACTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_MOVIMIENTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_CUENTAS);
        onCreate(db);
    }

    // CRUD Cuentas
    public long insertarCuenta(CuentaModel cuenta) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_CUENTA_NOMBRE, cuenta.getNombre());
        values.put(COL_CUENTA_TIPO, cuenta.getTipo());
        values.put(COL_CUENTA_SALDO_INICIAL, cuenta.getSaldoInicial());
        return db.insert(TABLA_CUENTAS, null, values);
    }

    public List<CuentaModel> obtenerCuentas() {
        List<CuentaModel> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLA_CUENTAS, null);
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(0);
                String nombre = cursor.getString(1);
                String tipo = cursor.getString(2);
                double saldoInicial = cursor.getDouble(3);
                lista.add(new CuentaModel(id, nombre, tipo, saldoInicial));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    public double calcularSaldoTotalGlobal() {
        double total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cCuentas = db.rawQuery("SELECT SUM(" + COL_CUENTA_SALDO_INICIAL + ") FROM " + TABLA_CUENTAS, null);
        if (cCuentas.moveToFirst()) {
            total += cCuentas.getDouble(0);
        }
        cCuentas.close();

        Cursor cIngresos = db.rawQuery("SELECT SUM(" + COL_MOV_MONTO + ") FROM " + TABLA_MOVIMIENTOS + " WHERE " + COL_MOV_TIPO + " = 'ingreso'", null);
        if (cIngresos.moveToFirst()) {
            total += cIngresos.getDouble(0);
        }
        cIngresos.close();

        Cursor cEgresos = db.rawQuery("SELECT SUM(" + COL_MOV_MONTO + ") FROM " + TABLA_MOVIMIENTOS + " WHERE " + COL_MOV_TIPO + " = 'egreso'", null);
        if (cEgresos.moveToFirst()) {
            total -= cEgresos.getDouble(0);
        }
        cEgresos.close();

        return total;
    }

    // CRUD Movimientos
    public long insertarMovimiento(MovimientoModel mov) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_MOV_CUENTA_ID, mov.getCuentaId());
        values.put(COL_MOV_TIPO, mov.getTipo());
        values.put(COL_MOV_MONTO, mov.getMonto());
        values.put(COL_MOV_DESCRIPCION, mov.getDescripcion());
        values.put(COL_MOV_FECHA, mov.getFecha());
        values.put(COL_MOV_LAT, mov.getLatitud());
        values.put(COL_MOV_LNG, mov.getLongitud());
        values.put(COL_MOV_CATEGORIA, mov.getCategoria());
        return db.insert(TABLA_MOVIMIENTOS, null, values);
    }

    public List<MovimientoModel> obtenerMovimientos(String filtroBusqueda) {
        List<MovimientoModel> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLA_MOVIMIENTOS;
        String[] args = null;

        if (filtroBusqueda != null && !filtroBusqueda.trim().isEmpty()) {
            query += " WHERE " + COL_MOV_DESCRIPCION + " LIKE ? OR " + COL_MOV_CATEGORIA + " LIKE ?";
            String pattern = "%" + filtroBusqueda.trim() + "%";
            args = new String[]{pattern, pattern};
        }
        query += " ORDER BY " + COL_MOV_ID + " DESC";

        Cursor cursor = db.rawQuery(query, args);
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(0);
                long cuentaId = cursor.getLong(1);
                String tipo = cursor.getString(2);
                double monto = cursor.getDouble(3);
                String descripcion = cursor.getString(4);
                String fecha = cursor.getString(5);
                Double lat = cursor.isNull(6) ? null : cursor.getDouble(6);
                Double lng = cursor.isNull(7) ? null : cursor.getDouble(7);
                String cat = cursor.getString(8);

                lista.add(new MovimientoModel(id, cuentaId, tipo, monto, descripcion, fecha, lat, lng, cat));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    // CRUD Contactos
    public long insertarContacto(ContactoModel contacto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_CONTACTO_NOMBRE, contacto.getNombre());
        values.put(COL_CONTACTO_TELEFONO, contacto.getTelefono());
        values.put(COL_CONTACTO_EMAIL, contacto.getEmail());
        values.put(COL_CONTACTO_NOTAS, contacto.getNotas());
        return db.insert(TABLA_CONTACTOS, null, values);
    }

    public List<ContactoModel> obtenerContactos() {
        List<ContactoModel> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLA_CONTACTOS + " ORDER BY " + COL_CONTACTO_NOMBRE + " ASC", null);
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(0);
                String nombre = cursor.getString(1);
                String tel = cursor.getString(2);
                String email = cursor.getString(3);
                String notas = cursor.getString(4);
                lista.add(new ContactoModel(id, nombre, tel, email, notas));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    public int obtenerCantidadContactos() {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLA_CONTACTOS, null);
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    // CRUD Compras
    public long insertarCompra(CompraModel compra) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_COMPRA_TITULO, compra.getTitulo());
        values.put(COL_COMPRA_MONTO, compra.getMonto());
        values.put(COL_COMPRA_FECHA, compra.getFecha());
        values.put(COL_COMPRA_ES_REALIZADA, compra.isEsRealizada() ? 1 : 0);
        values.put(COL_COMPRA_CONTACTO_ID, compra.getContactoId());
        values.put(COL_COMPRA_CATEGORIA, compra.getCategoria());
        return db.insert(TABLA_COMPRAS, null, values);
    }

    public List<CompraModel> obtenerComprasPorEstado(boolean esRealizada) {
        List<CompraModel> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLA_COMPRAS + " WHERE " + COL_COMPRA_ES_REALIZADA + " = ? ORDER BY " + COL_COMPRA_ID + " DESC", new String[]{esRealizada ? "1" : "0"});
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(0);
                String titulo = cursor.getString(1);
                double monto = cursor.getDouble(2);
                String fecha = cursor.getString(3);
                boolean realizada = cursor.getInt(4) == 1;
                Long contactoId = cursor.isNull(5) ? null : cursor.getLong(5);
                String cat = cursor.getString(6);
                lista.add(new CompraModel(id, titulo, monto, fecha, realizada, contactoId, cat));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    public double obtenerTotalCompras() {
        double total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COL_COMPRA_MONTO + ") FROM " + TABLA_COMPRAS, null);
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }

    // Gastos Compartidos & Deudas
    public long registrarGastoCompartido(String concepto, double montoTotal, String tipoDivision, String pagador, String fecha, List<String> listaItems, List<String> contactosParticipantes, List<Double> montosCustom) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues gastoValues = new ContentValues();
        gastoValues.put(COL_GASTO_CONCEPTO, concepto);
        gastoValues.put(COL_GASTO_MONTO_TOTAL, montoTotal);
        gastoValues.put(COL_GASTO_TIPO_DIVISION, tipoDivision);
        gastoValues.put(COL_GASTO_PAGADOR, pagador);
        gastoValues.put(COL_GASTO_FECHA, fecha);

        long gastoId = db.insert(TABLA_GASTOS_COMPARTIDOS, null, gastoValues);

        if (listaItems != null) {
            for (String item : listaItems) {
                ContentValues itemValues = new ContentValues();
                itemValues.put(COL_ITEM_GASTO_ID, gastoId);
                itemValues.put(COL_ITEM_NOMBRE, item);
                itemValues.put(COL_ITEM_PRECIO, montoTotal / Math.max(1, listaItems.size()));
                db.insert(TABLA_GASTO_ITEMS, null, itemValues);
            }
        }

        boolean usuarioEsPagador = pagador == null || pagador.contains("Yo") || pagador.contains("Usuario");
        int numPersonas = contactosParticipantes != null ? contactosParticipantes.size() + 1 : 1;
        double cuotaEquitativa = montoTotal / Math.max(1, numPersonas);

        if (contactosParticipantes != null) {
            for (int i = 0; i < contactosParticipantes.size(); i++) {
                String contacto = contactosParticipantes.get(i);
                double cuotaContacto = cuotaEquitativa;
                if ("personalizada".equalsIgnoreCase(tipoDivision) && montosCustom != null && i < montosCustom.size()) {
                    cuotaContacto = montosCustom.get(i);
                }

                ContentValues deudaValues = new ContentValues();
                deudaValues.put(COL_DEUDA_GASTO_ID, gastoId);
                deudaValues.put(COL_DEUDA_CONTACTO, contacto);
                deudaValues.put(COL_DEUDA_MONTO, cuotaContacto);
                deudaValues.put(COL_DEUDA_ES_PAGADA, 0);
                deudaValues.put(COL_DEUDA_TIPO, usuarioEsPagador ? "me_deben" : "yo_debo");

                db.insert(TABLA_DEUDAS, null, deudaValues);
            }
        }

        return gastoId;
    }

    public double obtenerTotalMeDeben() {
        double total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COL_DEUDA_MONTO + ") FROM " + TABLA_DEUDAS + " WHERE " + COL_DEUDA_TIPO + " = 'me_deben' AND " + COL_DEUDA_ES_PAGADA + " = 0", null);
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }

    public double obtenerTotalYoDebo() {
        double total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COL_DEUDA_MONTO + ") FROM " + TABLA_DEUDAS + " WHERE " + COL_DEUDA_TIPO + " = 'yo_debo' AND " + COL_DEUDA_ES_PAGADA + " = 0", null);
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }

    public List<DeudaModel> obtenerListaDeudas() {
        List<DeudaModel> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT d." + COL_DEUDA_ID + ", g." + COL_GASTO_CONCEPTO + ", d." + COL_DEUDA_CONTACTO + ", d." + COL_DEUDA_MONTO + ", d." + COL_DEUDA_ES_PAGADA + ", d." + COL_DEUDA_TIPO + ", g." + COL_GASTO_FECHA +
                " FROM " + TABLA_DEUDAS + " d LEFT JOIN " + TABLA_GASTOS_COMPARTIDOS + " g ON d." + COL_DEUDA_GASTO_ID + " = g." + COL_GASTO_ID +
                " ORDER BY d." + COL_DEUDA_ID + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(0);
                String concepto = cursor.getString(1);
                String contacto = cursor.getString(2);
                double monto = cursor.getDouble(3);
                boolean esPagada = cursor.getInt(4) == 1;
                String tipo = cursor.getString(5);
                String fecha = cursor.getString(6);

                lista.add(new DeudaModel(id, concepto != null ? concepto : "Gasto Compartido", contacto, monto, esPagada, tipo, fecha != null ? fecha : "Reciente"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    public void marcarDeudaComoPagada(long idDeuda) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_DEUDA_ES_PAGADA, 1);
        db.update(TABLA_DEUDAS, values, COL_DEUDA_ID + " = ?", new String[]{String.valueOf(idDeuda)});
    }
}