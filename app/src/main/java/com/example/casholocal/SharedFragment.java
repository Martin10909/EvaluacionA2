package com.example.casholocal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class SharedFragment extends Fragment {

    // Componentes
    private CoordinatorLayout coordinatorraizcompartido;
    private LinearLayout llcontenedorraizcompartido;
    private LinearLayout llencabezadocompartido;
    private TextView tvtitulogastoscompartidos;
    private TextView tvsubtitulogastoscompartidos;
    private LinearLayout llcontenedorsumaconceptos;
    private LinearLayout llcolumnacobrar;
    private TextView tvtitulocobrar;
    private TextView tvmedebencompartido;
    private View vdivisorvertical;
    private LinearLayout llcolumnapagar;
    private TextView tvtitulopagar;
    private TextView tvyodebo;
    private TabLayout pestanasdeudas;
    private TabItem pestanapendientes;
    private TabItem pestanasaldadas;
    private RecyclerView rvgastoscompartidos;
    private FloatingActionButton fabagregargastocompartido;

    private DatabaseHelper dbHelper;
    private SharedExpenseAdapter adapter;
    private boolean mostrandoSaldadas = false;

    public SharedFragment() {
        // Constructor público vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shared, container, false);
        initViews(view);

        if (getContext() != null) {
            dbHelper = new DatabaseHelper(getContext());
            configurarRecyclerView();
            cargarDatosReales();
        }

        configurarListeners();
        return view;
    }

    private void initViews(View view) {
        coordinatorraizcompartido = view.findViewById(R.id.coordinatorraizcompartido);
        llcontenedorraizcompartido = view.findViewById(R.id.llcontenedorraizcompartido);
        llencabezadocompartido = view.findViewById(R.id.llencabezadocompartido);
        tvtitulogastoscompartidos = view.findViewById(R.id.tvtitulogastoscompartidos);
        tvsubtitulogastoscompartidos = view.findViewById(R.id.tvsubtitulogastoscompartidos);
        llcontenedorsumaconceptos = view.findViewById(R.id.llcontenedorsumaconceptos);
        llcolumnacobrar = view.findViewById(R.id.llcolumnacobrar);
        tvtitulocobrar = view.findViewById(R.id.tvtitulocobrar);
        tvmedebencompartido = view.findViewById(R.id.tvmedebencompartido);
        vdivisorvertical = view.findViewById(R.id.vdivisorvertical);
        llcolumnapagar = view.findViewById(R.id.llcolumnapagar);
        tvtitulopagar = view.findViewById(R.id.tvtitulopagar);
        tvyodebo = view.findViewById(R.id.tvyodebo);
        pestanasdeudas = view.findViewById(R.id.pestanasdeudas);
        pestanapendientes = view.findViewById(R.id.pestanapendientes);
        pestanasaldadas = view.findViewById(R.id.pestanasaldadas);
        rvgastoscompartidos = view.findViewById(R.id.rvgastoscompartidos);
        fabagregargastocompartido = view.findViewById(R.id.fabagregargastocompartido);
    }

    private void configurarRecyclerView() {
        if (rvgastoscompartidos != null && getContext() != null) {
            rvgastoscompartidos.setLayoutManager(new LinearLayoutManager(getContext()));
            List<DeudaModel> deudas = dbHelper.obtenerListaDeudasPorEstado(mostrandoSaldadas);
            adapter = new SharedExpenseAdapter(deudas, new SharedExpenseAdapter.OnDeudaActionListener() {
                @Override
                public void onMarcarPagada(DeudaModel deuda) {
                    dbHelper.marcarDeudaComoPagada(deuda.getId());
                    Toast.makeText(getContext(), "Deuda marcada como saldada", Toast.LENGTH_SHORT).show();
                    cargarDatosReales();
                }

                @Override
                public void onEliminarDeuda(DeudaModel deuda) {
                    dbHelper.eliminarDeuda(deuda.getId());
                    Toast.makeText(getContext(), "Deuda eliminada", Toast.LENGTH_SHORT).show();
                    cargarDatosReales();
                }
            });
            rvgastoscompartidos.setAdapter(adapter);
        }
    }

    public void cargarDatosReales() {
        if (dbHelper != null) {
            double totalMeDeben = dbHelper.obtenerTotalMeDeben();
            double totalYoDebo = dbHelper.obtenerTotalYoDebo();

            if (tvmedebencompartido != null) {
                tvmedebencompartido.setText(FormatoMoneda.aPesoChileno(totalMeDeben));
            }

            if (tvyodebo != null) {
                tvyodebo.setText(FormatoMoneda.aPesoChileno(totalYoDebo));
            }

            if (adapter != null) {
                adapter.actualizarLista(dbHelper.obtenerListaDeudasPorEstado(mostrandoSaldadas));
            }
        }
    }

    private void configurarListeners() {
        if (fabagregargastocompartido != null) {
            fabagregargastocompartido.setOnClickListener(v -> {
                if (getParentFragmentManager() != null) {
                    AddSharedExpenseDialogFragment dialog = new AddSharedExpenseDialogFragment();
                    dialog.show(getParentFragmentManager(), "AddSharedExpenseDialog");
                }
            });
        }

        if (pestanasdeudas != null) {
            pestanasdeudas.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    mostrandoSaldadas = tab.getPosition() == 1; // 0 = Pendientes, 1 = Saldadas
                    cargarDatosReales();
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {}

                @Override
                public void onTabReselected(TabLayout.Tab tab) {}
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarDatosReales();
    }

    // Getters
    public CoordinatorLayout getCoordinatorraizcompartido() { return coordinatorraizcompartido; }
    public LinearLayout getLlcontenedorraizcompartido() { return llcontenedorraizcompartido; }
    public LinearLayout getLlencabezadocompartido() { return llencabezadocompartido; }
    public TextView getTvtitulogastoscompartidos() { return tvtitulogastoscompartidos; }
    public TextView getTvsubtitulogastoscompartidos() { return tvsubtitulogastoscompartidos; }
    public LinearLayout getLlcontenedorsumaconceptos() { return llcontenedorsumaconceptos; }
    public LinearLayout getLlcolumnacobrar() { return llcolumnacobrar; }
    public TextView getTvtitulocobrar() { return tvtitulocobrar; }
    public TextView getTvmedebencompartido() { return tvmedebencompartido; }
    public View getVdivisorvertical() { return vdivisorvertical; }
    public LinearLayout getLlcolumnapagar() { return llcolumnapagar; }
    public TextView getTvtitulopagar() { return tvtitulopagar; }
    public TextView getTvyodebo() { return tvyodebo; }
    public TabLayout getPestanasdeudas() { return pestanasdeudas; }
    public TabItem getPestanapendientes() { return pestanapendientes; }
    public TabItem getPestanasaldadas() { return pestanasaldadas; }
    public RecyclerView getRvgastoscompartidos() { return rvgastoscompartidos; }
    public FloatingActionButton getFabagregargastocompartido() { return fabagregargastocompartido; }
}