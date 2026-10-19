package com.example.casholocal;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private NestedScrollView nsvprincipal;
    private ConstraintLayout clraizhome;
    private ConstraintLayout clseccionencabezado;
    private CardView cvtarjetabalance;
    private ConstraintLayout clcontenedorbalance;
    private LinearLayout lldetallesbalance;
    private LinearLayout lldetallesdeuda;
    private LinearLayout llaccionesrapidas;
    private LinearLayout btningresorapido;
    private LinearLayout btngastorapido;
    private LinearLayout btndividirrapido;
    private CardView cvseccioncuentas;
    private ConstraintLayout clcontenedorcuentas;
    private CardView cvsecciontransacciones;
    private ConstraintLayout clcontenedortransacciones;
    private FrameLayout flcapacarga;

    private TextView tvbienvenida;
    private TextView tvnombreusuario;
    private TextView tvtitulobalance;
    private TextView tvbalancetotal;
    private TextView tvtitulodeuda;
    private TextView tvmedeben;
    private TextView tvtituloingreso;
    private TextView tvtitulogasto;
    private TextView tvtitulodividir;
    private TextView tvencabezadocuentas;
    private TextView tvvertodaslascuentas;
    private TextView tvencabezadotransacciones;
    private TextView tvvermastransacciones;
    private TextView tvsintransacciones;

    private ImageButton btnnotificaciones;
    private ImageButton btnperfilusuario;
    private MaterialButton btnagregarcuenta;
    private ImageButton btnfiltrartransacciones;

    private ImageView imgavatar;
    private View vdivisor1;
    private ImageView imgiconoingreso;
    private ImageView imgiconogasto;
    private ImageView imgiconodividir;
    private TextInputLayout tilbusquedatransacciones;
    private EditText etbuscartransacciones;
    private RecyclerView rvcuentas;
    private RecyclerView rvtransaccionesrecientes;
    private ProgressBar pbcargando;

    private DatabaseHelper dbHelper;
    private CuentaAdapter cuentaAdapter;
    private MovimientoAdapter movimientoAdapter;

    public HomeFragment() {
        // Constructor público vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);

        if (getContext() != null) {
            dbHelper = new DatabaseHelper(getContext());
            configurarRecyclerViews();
            cargarDatosDesdeBaseDeDatos();
        }

        configurarListeners();
        return view;
    }

    private void initViews(View view) {
        nsvprincipal = view.findViewById(R.id.nsvprincipal);
        clraizhome = view.findViewById(R.id.clraizhome);
        clseccionencabezado = view.findViewById(R.id.clseccionencabezado);
        cvtarjetabalance = view.findViewById(R.id.cvtarjetabalance);
        clcontenedorbalance = view.findViewById(R.id.clcontenedorbalance);
        lldetallesbalance = view.findViewById(R.id.lldetallesbalance);
        lldetallesdeuda = view.findViewById(R.id.lldetallesdeuda);
        llaccionesrapidas = view.findViewById(R.id.llaccionesrapidas);
        btningresorapido = view.findViewById(R.id.btningresorapido);
        btngastorapido = view.findViewById(R.id.btngastorapido);
        btndividirrapido = view.findViewById(R.id.btndividirrapido);
        cvseccioncuentas = view.findViewById(R.id.cvseccioncuentas);
        clcontenedorcuentas = view.findViewById(R.id.clcontenedorcuentas);
        cvsecciontransacciones = view.findViewById(R.id.cvsecciontransacciones);
        clcontenedortransacciones = view.findViewById(R.id.clcontenedortransacciones);
        flcapacarga = view.findViewById(R.id.flcapacarga);

        tvbienvenida = view.findViewById(R.id.tvbienvenida);
        tvnombreusuario = view.findViewById(R.id.tvnombreusuario);
        tvtitulobalance = view.findViewById(R.id.tvtitulobalance);
        tvbalancetotal = view.findViewById(R.id.tvbalancetotal);
        tvtitulodeuda = view.findViewById(R.id.tvtitulodeuda);
        tvmedeben = view.findViewById(R.id.tvmedeben);
        tvtituloingreso = view.findViewById(R.id.tvtituloingreso);
        tvtitulogasto = view.findViewById(R.id.tvtitulogasto);
        tvtitulodividir = view.findViewById(R.id.tvtitulodividir);
        tvencabezadocuentas = view.findViewById(R.id.tvencabezadocuentas);
        tvvertodaslascuentas = view.findViewById(R.id.tvvertodaslascuentas);
        tvencabezadotransacciones = view.findViewById(R.id.tvencabezadotransacciones);
        tvvermastransacciones = view.findViewById(R.id.tvvermastransacciones);
        tvsintransacciones = view.findViewById(R.id.tvsintransacciones);

        btnnotificaciones = view.findViewById(R.id.btnnotificaciones);
        btnperfilusuario = view.findViewById(R.id.btnperfilusuario);
        btnagregarcuenta = view.findViewById(R.id.btnagregarcuenta);
        btnfiltrartransacciones = view.findViewById(R.id.btnfiltrartransacciones);

        imgavatar = view.findViewById(R.id.imgavatar);
        vdivisor1 = view.findViewById(R.id.vdivisor1);
        imgiconoingreso = view.findViewById(R.id.imgiconoingreso);
        imgiconogasto = view.findViewById(R.id.imgiconogasto);
        imgiconodividir = view.findViewById(R.id.imgiconodividir);
        tilbusquedatransacciones = view.findViewById(R.id.tilbusquedatransacciones);
        etbuscartransacciones = view.findViewById(R.id.etbuscartransacciones);
        pbcargando = view.findViewById(R.id.pbcargando);

        rvcuentas = view.findViewById(R.id.rvcuentas);
        rvtransaccionesrecientes = view.findViewById(R.id.rvtransaccionesrecientes);
    }

    private void configurarRecyclerViews() {
        if (getContext() != null) {
            if (rvcuentas != null) {
                rvcuentas.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                cuentaAdapter = new CuentaAdapter(dbHelper.obtenerCuentas());
                rvcuentas.setAdapter(cuentaAdapter);
            }

            if (rvtransaccionesrecientes != null) {
                rvtransaccionesrecientes.setLayoutManager(new LinearLayoutManager(getContext()));
                movimientoAdapter = new MovimientoAdapter(dbHelper.obtenerMovimientos(""));
                rvtransaccionesrecientes.setAdapter(movimientoAdapter);
            }
        }
    }

    public void cargarDatosDesdeBaseDeDatos() {
        if (dbHelper != null) {
            double saldoTotal = dbHelper.calcularSaldoTotalGlobal();
            double meDebenTotal = dbHelper.obtenerTotalMeDeben();

            if (tvbalancetotal != null) {
                tvbalancetotal.setText(FormatoMoneda.aPesoChileno(saldoTotal));
            }

            if (tvmedeben != null) {
                tvmedeben.setText(FormatoMoneda.aPesoChileno(meDebenTotal));
            }

            if (cuentaAdapter != null) {
                cuentaAdapter.actualizarLista(dbHelper.obtenerCuentas());
            }

            if (movimientoAdapter != null) {
                String filtro = etbuscartransacciones != null && etbuscartransacciones.getText() != null ? etbuscartransacciones.getText().toString() : "";
                List<MovimientoModel> movimientos = dbHelper.obtenerMovimientos(filtro);
                movimientoAdapter.actualizarLista(movimientos);

                if (tvsintransacciones != null) {
                    tvsintransacciones.setVisibility(movimientos.isEmpty() ? View.VISIBLE : View.GONE);
                }
            }
        }
    }

    private void configurarListeners() {
        if (btningresorapido != null) {
            btningresorapido.setOnClickListener(v -> abrirDialogoMovimiento());
        }

        if (btngastorapido != null) {
            btngastorapido.setOnClickListener(v -> abrirDialogoMovimiento());
        }

        if (btndividirrapido != null) {
            btndividirrapido.setOnClickListener(v -> {
                if (getParentFragmentManager() != null) {
                    AddSharedExpenseDialogFragment dialog = new AddSharedExpenseDialogFragment();
                    dialog.show(getParentFragmentManager(), "AddSharedExpenseDialog");
                }
            });
        }

        if (btnagregarcuenta != null) {
            btnagregarcuenta.setOnClickListener(v -> mostrarDialogoNuevaCuenta());
        }

        if (btnperfilusuario != null) {
            btnperfilusuario.setOnClickListener(v -> Toast.makeText(getContext(), "Perfil de Usuario: " + tvnombreusuario.getText(), Toast.LENGTH_SHORT).show());
        }

        if (btnnotificaciones != null) {
            btnnotificaciones.setOnClickListener(v -> Toast.makeText(getContext(), "Sin notificaciones pendientes", Toast.LENGTH_SHORT).show());
        }

        if (btnfiltrartransacciones != null) {
            btnfiltrartransacciones.setOnClickListener(v -> {
                if (getActivity() != null) {
                    Intent intent = new Intent(getActivity(), MapActivity.class);
                    startActivity(intent);
                }
            });
        }

        if (etbuscartransacciones != null) {
            etbuscartransacciones.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (dbHelper != null && movimientoAdapter != null) {
                        List<MovimientoModel> listaFiltrada = dbHelper.obtenerMovimientos(s.toString());
                        movimientoAdapter.actualizarLista(listaFiltrada);
                        if (tvsintransacciones != null) {
                            tvsintransacciones.setVisibility(listaFiltrada.isEmpty() ? View.VISIBLE : View.GONE);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    private void abrirDialogoMovimiento() {
        if (getParentFragmentManager() != null) {
            AddTransactionDialogFragment dialog = new AddTransactionDialogFragment();
            dialog.show(getParentFragmentManager(), "AddTransactionDialog");
        }
    }

    private void mostrarDialogoNuevaCuenta() {
        if (getContext() == null || dbHelper == null) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Nueva Cuenta Financiera");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 16, 32, 16);

        // 1. Nombre de la cuenta (Obligatorio)
        final EditText etNombre = new EditText(getContext());
        etNombre.setHint("Nombre de la cuenta (Obligatorio)");
        layout.addView(etNombre);

        // 2. Tipo de cuenta (Obligatorio)
        TextView tvTipoLabel = new TextView(getContext());
        tvTipoLabel.setText("Tipo de cuenta (Obligatorio):");
        tvTipoLabel.setPadding(0, 12, 0, 4);
        layout.addView(tvTipoLabel);

        final Spinner spTipo = new Spinner(getContext());
        ArrayAdapter<String> adapterTipos = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new String[]{"Bancaria", "Billetera / Efectivo", "Tarjeta de Crédito", "Ahorro"});
        adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipo.setAdapter(adapterTipos);
        layout.addView(spTipo);

        // 3. Saldo Inicial (Obligatorio)
        final EditText etSaldo = new EditText(getContext());
        etSaldo.setHint("Saldo inicial ($ CLP) (Obligatorio)");
        etSaldo.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(etSaldo);

        // 4. Moneda (Opcional)
        TextView tvMonedaLabel = new TextView(getContext());
        tvMonedaLabel.setText("Moneda (Opcional):");
        tvMonedaLabel.setPadding(0, 12, 0, 4);
        layout.addView(tvMonedaLabel);

        final Spinner spMoneda = new Spinner(getContext());
        ArrayAdapter<String> adapterMonedas = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new String[]{"CLP ($)", "USD ($)", "EUR (€)"});
        adapterMonedas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMoneda.setAdapter(adapterMonedas);
        layout.addView(spMoneda);

        // 5. Descripción (Opcional)
        final EditText etDesc = new EditText(getContext());
        etDesc.setHint("Descripción / Nota (Opcional)");
        layout.addView(etDesc);

        builder.setView(layout);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombre = etNombre.getText().toString().trim();
            String saldoStr = etSaldo.getText().toString().trim();
            String tipo = spTipo.getSelectedItem() != null ? spTipo.getSelectedItem().toString() : "Bancaria";
            String moneda = spMoneda.getSelectedItem() != null ? spMoneda.getSelectedItem().toString() : "CLP ($)";
            String desc = etDesc.getText().toString().trim();

            if (desc.isEmpty()) {
                desc = "Cuenta registrada";
            }

            double saldo = 0;
            try {
                saldo = Double.parseDouble(saldoStr);
            } catch (Exception ignored) {}

            if (!nombre.isEmpty() && !saldoStr.isEmpty()) {
                Calendar c = Calendar.getInstance();
                String fechaHoy = String.format(Locale.getDefault(), "%02d/%02d/%d", c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR));

                // 6. Fecha creación (automática) + 7. ID automático en SQLite PRIMARY KEY
                dbHelper.insertarCuenta(new CuentaModel(0, nombre, tipo, saldo, moneda, desc, fechaHoy));
                cargarDatosDesdeBaseDeDatos();
                Toast.makeText(getContext(), "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Por favor completa los campos obligatorios (Nombre, Tipo y Saldo)", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarDatosDesdeBaseDeDatos();
    }

    // Getters
    public NestedScrollView getNsvprincipal() { return nsvprincipal; }
    public ConstraintLayout getClraizhome() { return clraizhome; }
    public ConstraintLayout getClseccionencabezado() { return clseccionencabezado; }
    public CardView getCvtarjetabalance() { return cvtarjetabalance; }
    public ConstraintLayout getClcontenedorbalance() { return clcontenedorbalance; }
    public LinearLayout getLldetallesbalance() { return lldetallesbalance; }
    public LinearLayout getLldetallesdeuda() { return lldetallesdeuda; }
    public LinearLayout getLlaccionesrapidas() { return llaccionesrapidas; }
    public LinearLayout getBtningresorapido() { return btningresorapido; }
    public LinearLayout getBtngastorapido() { return btngastorapido; }
    public LinearLayout getBtndividirrapido() { return btndividirrapido; }
    public CardView getCvseccioncuentas() { return cvseccioncuentas; }
    public ConstraintLayout getClcontenedorcuentas() { return clcontenedorcuentas; }
    public CardView getCvsecciontransacciones() { return cvsecciontransacciones; }
    public ConstraintLayout getClcontenedortransacciones() { return clcontenedortransacciones; }
    public FrameLayout getFlcapacarga() { return flcapacarga; }

    public TextView getTvbienvenida() { return tvbienvenida; }
    public TextView getTvnombreusuario() { return tvnombreusuario; }
    public TextView getTvtitulobalance() { return tvtitulobalance; }
    public TextView getTvbalancetotal() { return tvbalancetotal; }
    public TextView getTvtitulodeuda() { return tvtitulodeuda; }
    public TextView getTvmedeben() { return tvmedeben; }
    public TextView getTvtituloingreso() { return tvtituloingreso; }
    public TextView getTvtitulogasto() { return tvtitulogasto; }
    public TextView getTvtitulodividir() { return tvtitulodividir; }
    public TextView getTvencabezadocuentas() { return tvencabezadocuentas; }
    public TextView getTvvertodaslascuentas() { return tvvertodaslascuentas; }
    public TextView getTvencabezadotransacciones() { return tvencabezadotransacciones; }
    public TextView getTvvermastransacciones() { return tvvermastransacciones; }
    public TextView getTvsintransacciones() { return tvsintransacciones; }

    public ImageButton getBtnnotificaciones() { return btnnotificaciones; }
    public ImageButton getBtnperfilusuario() { return btnperfilusuario; }
    public MaterialButton getBtnagregarcuenta() { return btnagregarcuenta; }
    public ImageButton getBtnfiltrartransacciones() { return btnfiltrartransacciones; }

    public ImageView getImgavatar() { return imgavatar; }
    public View getVdivisor1() { return vdivisor1; }
    public ImageView getImgiconoingreso() { return imgiconoingreso; }
    public ImageView getImgiconogasto() { return imgiconogasto; }
    public ImageView getImgiconodividir() { return imgiconodividir; }
    public TextInputLayout getTilbusquedatransacciones() { return tilbusquedatransacciones; }
    public EditText getEtbuscartransacciones() { return etbuscartransacciones; }
    public RecyclerView getRvcuentas() { return rvcuentas; }
    public RecyclerView getRvtransaccionesrecientes() { return rvtransaccionesrecientes; }
    public ProgressBar getPbcargando() { return pbcargando; }
}