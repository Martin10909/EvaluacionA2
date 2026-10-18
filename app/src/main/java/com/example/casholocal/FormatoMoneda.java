package com.example.casholocal;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatoMoneda {
    private static final Locale LOCALE_CHILE = new Locale("es", "CL");

    public static String aPesoChileno(double monto) {
        try {
            NumberFormat format = NumberFormat.getCurrencyInstance(LOCALE_CHILE);
            format.setMaximumFractionDigits(0);
            return format.format(Math.round(monto));
        } catch (Exception e) {
            return String.format(Locale.getDefault(), "$%,d", Math.round(monto)).replace(',', '.');
        }
    }
}