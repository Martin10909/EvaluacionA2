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

import java.util.List;
import java.util.Locale;

public class SharedFragment extends Fragment {

    // 15 Componentes de la Paleta
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
    private RecyclerView rvgastoscompartidos;
    private FloatingActionButton fabagregargastocompartido;

    private DatabaseHelper dbHelper;
    private SharedExpenseAdapter adapter;

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
        rvgastoscompartidos = view.findViewById(R.id.rvgastoscompartidos);
        fabagregargastocompartido = view.findViewById(R.id.fabagregargastocompartido);
    }

    private void configurarRecyclerView() {
        if (rvgastoscompartidos != null && getContext() != null) {
            rvgastoscompartidos.setLayoutManager(new LinearLayoutManager(getContext()));
            List<DeudaModel> deudas = dbHelper.obtenerListaDeudas();
            adapter = new SharedExpenseAdapter(deudas, deuda -> {
                dbHelper.marcarDeudaComoPagada(deuda.getId());
                Toast.makeText(getContext(), "Deuda marcada como pagada", Toast.LENGTH_SHORT).show();
                cargarDatosReales();
            });
            rvgastoscompartidos.setAdapter(adapter);
        }
    }

    public void cargarDatosReales() {
        if (dbHelper != null) {
            double totalMeDeben = dbHelper.obtenerTotalMeDeben();
            double totalYoDebo = dbHelper.obtenerTotalYoDebo();

            if (tvmedebencompartido != null) {
                tvmedebencompartido.setText(String.format(Locale.getDefault(), "$%.2f", totalMeDeben));
            }

            if (tvyodebo != null) {
                tvyodebo.setText(String.format(Locale.getDefault(), "$%.2f", totalYoDebo));
            }

            if (adapter != null) {
                adapter.actualizarLista(dbHelper.obtenerListaDeudas());
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
    public RecyclerView getRvgastoscompartidos() { return rvgastoscompartidos; }
    public FloatingActionButton getFabagregargastocompartido() { return fabagregargastocompartido; }
}