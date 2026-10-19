package com.example.casholocal;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
    private String fechaSeleccionadaCompra = "Hoy";

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
        if (getContext() == null || dbHelper == null) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(mostrandoRealizadas ? "Registrar Nueva Compra" : "Planificar Compra Futura");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 16, 32, 16);

        final EditText etTitulo = new EditText(getContext());
        etTitulo.setHint("Nombre de la compra o producto");
        layout.addView(etTitulo);

        final EditText etMonto = new EditText(getContext());
        etMonto.setHint("Monto estimado ($ CLP)");
        etMonto.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(etMonto);

        final EditText etFecha = new EditText(getContext());
        etFecha.setHint("Fecha de compra (Toca para seleccionar)");
        etFecha.setFocusable(false);
        etFecha.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog dpd = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
                fechaSeleccionadaCompra = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month + 1, year);
                etFecha.setText(fechaSeleccionadaCompra);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dpd.show();
        });
        layout.addView(etFecha);

        // Spinner para vincular un contacto
        TextView tvContacto = new TextView(getContext());
        tvContacto.setText("Vincular con Contacto (Opcional):");
        tvContacto.setPadding(0, 12, 0, 4);
        layout.addView(tvContacto);

        final Spinner spContacto = new Spinner(getContext());
        List<ContactoModel> contactos = dbHelper.obtenerContactos();
        List<String> nombresContactos = new ArrayList<>();
        nombresContactos.add("Sin contacto vinculado");
        for (ContactoModel c : contactos) {
            nombresContactos.add(c.getNombre());
        }

        ArrayAdapter<String> adapterContactos = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, nombresContactos);
        adapterContactos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spContacto.setAdapter(adapterContactos);
        layout.addView(spContacto);

        builder.setView(layout);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String titulo = etTitulo.getText().toString().trim();
            String montoStr = etMonto.getText().toString().trim();
            double monto = 0;
            try {
                monto = Double.parseDouble(montoStr);
            } catch (Exception ignored) {}

            Long contactoIdSelected = null;
            String contactoNombreSelected = null;
            int pos = spContacto.getSelectedItemPosition();
            if (pos > 0 && pos - 1 < contactos.size()) {
                contactoIdSelected = contactos.get(pos - 1).getId();
                contactoNombreSelected = contactos.get(pos - 1).getNombre();
            }

            if (!titulo.isEmpty() && monto > 0) {
                dbHelper.insertarCompra(new CompraModel(0, titulo, monto, fechaSeleccionadaCompra, mostrandoRealizadas, contactoIdSelected, contactoNombreSelected, "Compras", -33.4372, -70.6506));
                actualizarResumenYLista();
                Toast.makeText(getContext(), "Compra guardada con éxito", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Ingresa un nombre y monto válido", Toast.LENGTH_SHORT).show();
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
    public TextInputLayout getTilbuscarcompras() { return tilbuscarcompras; }
    public EditText getEtbuscarcompras() { return etbuscarcompras; }
    public RecyclerView getRvlistacompras() { return rvlistacompras; }
    public FloatingActionButton getFabagregarcompra() { return fabagregarcompra; }
}