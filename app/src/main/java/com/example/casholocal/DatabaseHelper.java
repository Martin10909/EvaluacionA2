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
    private static final int DATABASE_VERSION = 1;

    public static final String TABLA_COMPRAS = "compras";
    public static final String COL_ID = "id";
    public static final String COL_TITULO = "titulo";
    public static final String COL_MONTO = "monto";
    public static final String COL_FECHA = "fecha";
    public static final String COL_ES_REALIZADA = "es_realizada";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLA_COMPRAS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITULO + " TEXT, " +
                COL_MONTO + " REAL, " +
                COL_FECHA + " TEXT, " +
                COL_ES_REALIZADA + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_COMPRAS);
        onCreate(db);
    }

    public long insertarCompra(String titulo, double monto, String fecha, boolean esRealizada) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITULO, titulo);
        values.put(COL_MONTO, monto);
        values.put(COL_FECHA, fecha);
        values.put(COL_ES_REALIZADA, esRealizada ? 1 : 0);
        long id = db.insert(TABLA_COMPRAS, null, values);
        db.close();
        return id;
    }

    public double obtenerTotalCompras() {
        double total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COL_MONTO + ") FROM " + TABLA_COMPRAS, null);
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return total;
    }
}