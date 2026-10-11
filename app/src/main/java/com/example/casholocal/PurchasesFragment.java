package com.example.casholocal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

public class PurchasesFragment extends Fragment {

    // 16 Componentes de la Paleta
    private CoordinatorLayout coordinatorraizcompras;
    private LinearLayout llcontenedorprincipal;
    private CardView cvtarjetaresumen;
    private LinearLayout llseccionresumen;
    private ImageView imgiconoresumen;
    private LinearLayout lldetallemonto;
    private TextView tvtituloresumencompras;
    private TextView tvsubtituloresumen;
    private TextView tvmontoresumencompras;
    private TabLayout pestanascompras;
    private TabItem pestanarealizadas;
    private TabItem pestanaagendadas;
    private LinearLayout llcontenedorlista;
    private TextView tvencabezadocompras;
    private RecyclerView rvlistacompras;
    private FloatingActionButton fabagregarcompra;

    private DatabaseHelper dbHelper;

    public PurchasesFragment() {
        // Constructor público vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchases, container, false);
        initViews(view);

        if (getContext() != null) {
            dbHelper = new DatabaseHelper(getContext());
            actualizarResumen();
        }

        configurarListeners();

        return view;
    }

    private void initViews(View view) {
        coordinatorraizcompras = view.findViewById(R.id.coordinatorraizcompras);
        llcontenedorprincipal = view.findViewById(R.id.llcontenedorprincipal);
        cvtarjetaresumen = view.findViewById(R.id.cvtarjetaresumen);
        llseccionresumen = view.findViewById(R.id.llseccionresumen);
        imgiconoresumen = view.findViewById(R.id.imgiconoresumen);
        lldetallemonto = view.findViewById(R.id.lldetallemonto);
        tvtituloresumencompras = view.findViewById(R.id.tvtituloresumencompras);
        tvsubtituloresumen = view.findViewById(R.id.tvsubtituloresumen);
        tvmontoresumencompras = view.findViewById(R.id.tvmontoresumencompras);
        pestanascompras = view.findViewById(R.id.pestanascompras);
        pestanarealizadas = view.findViewById(R.id.pestanarealizadas);
        pestanaagendadas = view.findViewById(R.id.pestanaagendadas);
        llcontenedorlista = view.findViewById(R.id.llcontenedorlista);
        tvencabezadocompras = view.findViewById(R.id.tvencabezadocompras);
        rvlistacompras = view.findViewById(R.id.rvlistacompras);
        fabagregarcompra = view.findViewById(R.id.fabagregarcompra);
    }

    private void configurarListeners() {
        if (fabagregarcompra != null) {
            fabagregarcompra.setOnClickListener(v -> {
                if (dbHelper != null) {
                    dbHelper.insertarCompra("Compra de prueba", 150.00, "2023-10-25", true);
                    actualizarResumen();
                    Toast.makeText(getContext(), "Compra agregada exitosamente", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (pestanascompras != null) {
            pestanascompras.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    // Cambiar entre pestañas de compras realizadas y agendadas
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {}

                @Override
                public void onTabReselected(TabLayout.Tab tab) {}
            });
        }
    }

    private void actualizarResumen() {
        if (dbHelper != null && tvmontoresumencompras != null) {
            double total = dbHelper.obtenerTotalCompras();
            tvmontoresumencompras.setText(String.format(Locale.getDefault(), "$%.2f", total));
        }
    }

    // Getters
    public CoordinatorLayout getCoordinatorraizcompras() { return coordinatorraizcompras; }
    public LinearLayout getLlcontenedorprincipal() { return llcontenedorprincipal; }
    public CardView getCvtarjetaresumen() { return cvtarjetaresumen; }
    public LinearLayout getLlseccionresumen() { return llseccionresumen; }
    public ImageView getImgiconoresumen() { return imgiconoresumen; }
    public LinearLayout getLldetallemonto() { return lldetallemonto; }
    public TextView getTvtituloresumencompras() { return tvtituloresumencompras; }
    public TextView getTvsubtituloresumen() { return tvsubtituloresumen; }
    public TextView getTvmontoresumencompras() { return tvmontoresumencompras; }
    public TabLayout getPestanascompras() { return pestanascompras; }
    public TabItem getPestanarealizadas() { return pestanarealizadas; }
    public TabItem getPestanaagendadas() { return pestanaagendadas; }
    public LinearLayout getLlcontenedorlista() { return llcontenedorlista; }
    public TextView getTvencabezadocompras() { return tvencabezadocompras; }
    public RecyclerView getRvlistacompras() { return rvlistacompras; }
    public FloatingActionButton getFabagregarcompra() { return fabagregarcompra; }
}