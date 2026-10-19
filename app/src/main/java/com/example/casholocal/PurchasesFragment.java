package com.example.casholocal;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class PurchasesFragment extends Fragment {

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
    private TextInputLayout tilbuscarcompras;
    private EditText etbuscarcompras;
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
        tilbuscarcompras = view.findViewById(R.id.tilbuscarcompras);
        etbuscarcompras = view.findViewById(R.id.etbuscarcompras);
        rvlistacompras = view.findViewById(R.id.rvlistacompras);
        fabagregarcompra = view.findViewById(R.id.fabagregarcompra);
    }

    private void configurarRecyclerView() {
        if (rvlistacompras != null && getContext() != null) {
            rvlistacompras.setLayoutManager(new LinearLayoutManager(getContext()));
            String filtro = etbuscarcompras != null && etbuscarcompras.getText() != null ? etbuscarcompras.getText().toString() : "";
            List<CompraModel> compras = dbHelper.obtenerComprasFiltradas(mostrandoRealizadas, filtro);
            compraAdapter = new CompraAdapter(compras, new CompraAdapter.OnCompraClickListener() {
                @Override
                public void onAccionCompra(CompraModel compra) {
                    if (!compra.isEsRealizada()) {
                        dbHelper.eliminarCompra(compra.getId());
                        dbHelper.insertarCompra(new CompraModel(0, compra.getTitulo(), compra.getMonto(), "Hoy", true, compra.getContactoId(), compra.getContactoNombre(), compra.getCategoria(), compra.getLatitud(), compra.getLongitud()));
                        Toast.makeText(getContext(), "Compra marcada como realizada", Toast.LENGTH_SHORT).show();
                        actualizarResumenYLista();
                    }
                }

                @Override
                public void onEliminarCompra(CompraModel compra) {
                    dbHelper.eliminarCompra(compra.getId());
                    Toast.makeText(getContext(), "Compra eliminada", Toast.LENGTH_SHORT).show();
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
                tvmontoresumencompras.setText(FormatoMoneda.aPesoChileno(total));
            }

            if (compraAdapter != null) {
                String filtro = etbuscarcompras != null && etbuscarcompras.getText() != null ? etbuscarcompras.getText().toString() : "";
                List<CompraModel> comprasFiltradas = dbHelper.obtenerComprasFiltradas(mostrandoRealizadas, filtro);
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

        if (etbuscarcompras != null) {
            etbuscarcompras.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    actualizarResumenYLista();
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    private void mostrarDialogoNuevaCompra() {
        if (getParentFragmentManager() != null) {
            AddPurchaseDialogFragment dialog = AddPurchaseDialogFragment.newInstance(mostrandoRealizadas);
            dialog.show(getParentFragmentManager(), "AddPurchaseDialog");
        }
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
    public TextInputLayout getTilbuscarcompras() { return tilbuscarcompras; }
    public EditText getEtbuscarcompras() { return etbuscarcompras; }
    public RecyclerView getRvlistacompras() { return rvlistacompras; }
    public FloatingActionButton getFabagregarcompra() { return fabagregarcompra; }
}