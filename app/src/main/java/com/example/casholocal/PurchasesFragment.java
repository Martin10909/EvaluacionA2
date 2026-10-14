package com.example.casholocal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
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
    private CompraAdapter compraAdapter;
    private boolean mostrandoRealizadas = true;

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
            configurarRecyclerView();
            actualizarResumenYLista();
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

    private void configurarRecyclerView() {
        if (rvlistacompras != null && getContext() != null) {
            rvlistacompras.setLayoutManager(new LinearLayoutManager(getContext()));
            List<CompraModel> compras = dbHelper.obtenerComprasPorEstado(mostrandoRealizadas);
            compraAdapter = new CompraAdapter(compras, compra -> {
                if (!compra.isEsRealizada()) {
                    dbHelper.insertarCompra(new CompraModel(compra.getId(), compra.getTitulo(), compra.getMonto(), "Hoy", true, compra.getContactoId(), compra.getCategoria()));
                    Toast.makeText(getContext(), "Compra marcada como realizada", Toast.LENGTH_SHORT).show();
                    actualizarResumenYLista();
                }
            });
            rvlistacompras.setAdapter(compraAdapter);
        }
    }

    public void actualizarResumenYLista() {
        if (dbHelper != null) {
            double total = dbHelper.obtenerTotalCompras();
            if (tvmontoresumencompras != null) {
                tvmontoresumencompras.setText(String.format(Locale.getDefault(), "$%.2f", total));
            }

            if (compraAdapter != null) {
                List<CompraModel> comprasFiltradas = dbHelper.obtenerComprasPorEstado(mostrandoRealizadas);
                compraAdapter.actualizarLista(comprasFiltradas);
            }
        }
    }

    private void configurarListeners() {
        if (fabagregarcompra != null) {
            fabagregarcompra.setOnClickListener(v -> mostrarDialogoNuevaCompra());
        }

        if (pestanascompras != null) {
            pestanascompras.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    mostrandoRealizadas = tab.getPosition() == 0;
                    actualizarResumenYLista();
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {}

                @Override
                public void onTabReselected(TabLayout.Tab tab) {}
            });
        }
    }

    private void mostrarDialogoNuevaCompra() {
        if (getContext() == null) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(mostrandoRealizadas ? "Nueva Compra Realizada" : "Agendar Compra Futura");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 16, 32, 16);

        final EditText etTitulo = new EditText(getContext());
        etTitulo.setHint("Título de la compra (ej. Supermercado)");
        layout.addView(etTitulo);

        final EditText etMonto = new EditText(getContext());
        etMonto.setHint("Monto estimado ($)");
        etMonto.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(etMonto);

        builder.setView(layout);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String titulo = etTitulo.getText().toString().trim();
            String montoStr = etMonto.getText().toString().trim();
            double monto = 0;
            try {
                monto = Double.parseDouble(montoStr);
            } catch (Exception ignored) {}

            if (!titulo.isEmpty()) {
                dbHelper.insertarCompra(new CompraModel(0, titulo, monto, "Hoy", mostrandoRealizadas, null, "Compras"));
                actualizarResumenYLista();
                Toast.makeText(getContext(), "Compra registrada", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        actualizarResumenYLista();
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