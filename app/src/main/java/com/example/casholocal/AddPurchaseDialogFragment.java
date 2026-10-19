package com.example.casholocal;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddPurchaseDialogFragment extends DialogFragment {

    private ScrollView scrollnuevacompra;
    private LinearLayout llcontenedorraizdialogocompra;
    private TextView tvtituloregistrarcompra;
    private TextView tvsubtitulocompra;
    private TextInputLayout tilcontenedortitulocompra;
    private EditText ettitulocompra;
    private TextInputLayout tilcontenedormontocompra;
    private EditText etmontocompra;
    private TextInputLayout tilcontenedorcategoriacompra;
    private EditText etcategoriacompra;
    private TextInputLayout tilcontenedorfechacompra;
    private EditText etfechacompra;
    private TextView tvlabelcontactocompra;
    private Spinner spcontactocompra;
    private LinearLayout llaccionesdialogocompra;
    private MaterialButton btncancelarcompra;
    private MaterialButton btnguardarcompra;

    private DatabaseHelper dbHelper;
    private List<ContactoModel> listaContactos = new ArrayList<>();
    private String fechaSeleccionadaCompra = "Hoy";
    private boolean mostrandoRealizadas = true;

    public AddPurchaseDialogFragment() {
        // Constructor público vacío requerido
    }

    public static AddPurchaseDialogFragment newInstance(boolean mostrandoRealizadas) {
        AddPurchaseDialogFragment fragment = new AddPurchaseDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean("mostrandoRealizadas", mostrandoRealizadas);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_CashoLocal_Dialog);
        if (getArguments() != null) {
            mostrandoRealizadas = getArguments().getBoolean("mostrandoRealizadas", true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_purchase, container, false);
        initViews(view);

        if (getContext() != null) {
            dbHelper = new DatabaseHelper(getContext());
            configurarSpinnerContactos();
        }

        if (tvtituloregistrarcompra != null) {
            tvtituloregistrarcompra.setText(mostrandoRealizadas ? "Registrar Nueva Compra" : "Planificar Compra Futura");
        }

        configurarListeners();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void initViews(View view) {
        scrollnuevacompra = view.findViewById(R.id.scrollnuevacompra);
        llcontenedorraizdialogocompra = view.findViewById(R.id.llcontenedorraizdialogocompra);
        tvtituloregistrarcompra = view.findViewById(R.id.tvtituloregistrarcompra);
        tvsubtitulocompra = view.findViewById(R.id.tvsubtitulocompra);
        tilcontenedortitulocompra = view.findViewById(R.id.tilcontenedortitulocompra);
        ettitulocompra = view.findViewById(R.id.ettitulocompra);
        tilcontenedormontocompra = view.findViewById(R.id.tilcontenedormontocompra);
        etmontocompra = view.findViewById(R.id.etmontocompra);
        tilcontenedorcategoriacompra = view.findViewById(R.id.tilcontenedorcategoriacompra);
        etcategoriacompra = view.findViewById(R.id.etcategoriacompra);
        tilcontenedorfechacompra = view.findViewById(R.id.tilcontenedorfechacompra);
        etfechacompra = view.findViewById(R.id.etfechacompra);
        tvlabelcontactocompra = view.findViewById(R.id.tvlabelcontactocompra);
        spcontactocompra = view.findViewById(R.id.spcontactocompra);
        llaccionesdialogocompra = view.findViewById(R.id.llaccionesdialogocompra);
        btncancelarcompra = view.findViewById(R.id.btncancelarcompra);
        btnguardarcompra = view.findViewById(R.id.btnguardarcompra);
    }

    private void configurarSpinnerContactos() {
        if (dbHelper != null && spcontactocompra != null && getContext() != null) {
            listaContactos = dbHelper.obtenerContactos();
            List<String> nombresContactos = new ArrayList<>();
            nombresContactos.add("Sin contacto vinculado");
            for (ContactoModel c : listaContactos) {
                nombresContactos.add(c.getNombre());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, nombresContactos);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spcontactocompra.setAdapter(adapter);
        }
    }

    private void configurarListeners() {
        if (etfechacompra != null) {
            etfechacompra.setFocusable(false);
            etfechacompra.setOnClickListener(v -> mostrarSelectorFecha());
        }

        if (btncancelarcompra != null) {
            btncancelarcompra.setOnClickListener(v -> dismiss());
        }

        if (btnguardarcompra != null) {
            btnguardarcompra.setOnClickListener(v -> {
                v.setEnabled(false);
                guardarCompra();
            });
        }
    }

    private void mostrarSelectorFecha() {
        if (getContext() == null) return;
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            fechaSeleccionadaCompra = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month + 1, year);
            if (etfechacompra != null) {
                etfechacompra.setText(fechaSeleccionadaCompra);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void guardarCompra() {
        String titulo = ettitulocompra != null && ettitulocompra.getText() != null ? ettitulocompra.getText().toString().trim() : "";
        String montoStr = etmontocompra != null && etmontocompra.getText() != null ? etmontocompra.getText().toString().trim() : "";
        String categoria = etcategoriacompra != null && etcategoriacompra.getText() != null && !etcategoriacompra.getText().toString().trim().isEmpty() ?
                etcategoriacompra.getText().toString().trim() : "Compras";

        double monto = 0;
        try {
            monto = Double.parseDouble(montoStr);
        } catch (Exception ignored) {}

        if (titulo.isEmpty() || monto <= 0) {
            Toast.makeText(getContext(), "Ingresa un nombre y monto válido", Toast.LENGTH_SHORT).show();
            if (btnguardarcompra != null) btnguardarcompra.setEnabled(true);
            return;
        }

        Long contactoIdSelected = null;
        String contactoNombreSelected = null;
        if (spcontactocompra != null && !listaContactos.isEmpty()) {
            int pos = spcontactocompra.getSelectedItemPosition();
            if (pos > 0 && pos - 1 < listaContactos.size()) {
                contactoIdSelected = listaContactos.get(pos - 1).getId();
                contactoNombreSelected = listaContactos.get(pos - 1).getNombre();
            }
        }

        if (dbHelper != null) {
            dbHelper.insertarCompra(new CompraModel(0, titulo, monto, fechaSeleccionadaCompra, mostrandoRealizadas, contactoIdSelected, contactoNombreSelected, categoria, -33.4372, -70.6506));
            Toast.makeText(getContext(), "Compra guardada con éxito", Toast.LENGTH_SHORT).show();

            if (getParentFragment() instanceof PurchasesFragment) {
                ((PurchasesFragment) getParentFragment()).actualizarResumenYLista();
            } else if (getParentFragment() instanceof HomeFragment) {
                ((HomeFragment) getParentFragment()).cargarDatosDesdeBaseDeDatos();
            }

            if (getActivity() != null) {
                Fragment activeFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.contenedorfragmento);
                if (activeFragment instanceof PurchasesFragment) {
                    ((PurchasesFragment) activeFragment).actualizarResumenYLista();
                } else if (activeFragment instanceof HomeFragment) {
                    ((HomeFragment) activeFragment).cargarDatosDesdeBaseDeDatos();
                }
            }
            dismiss();
        }
    }

    // Getters
    public ScrollView getScrollnuevacompra() { return scrollnuevacompra; }
    public LinearLayout getLlcontenedorraizdialogocompra() { return llcontenedorraizdialogocompra; }
    public TextView getTvtituloregistrarcompra() { return tvtituloregistrarcompra; }
    public TextView getTvsubtitulocompra() { return tvsubtitulocompra; }
    public TextInputLayout getTilcontenedortitulocompra() { return tilcontenedortitulocompra; }
    public EditText getEttitulocompra() { return ettitulocompra; }
    public TextInputLayout getTilcontenedormontocompra() { return tilcontenedormontocompra; }
    public EditText getEtmontocompra() { return etmontocompra; }
    public TextInputLayout getTilcontenedorcategoriacompra() { return tilcontenedorcategoriacompra; }
    public EditText getEtcategoriacompra() { return etcategoriacompra; }
    public TextInputLayout getTilcontenedorfechacompra() { return tilcontenedorfechacompra; }
    public EditText getEtfechacompra() { return etfechacompra; }
    public TextView getTvlabelcontactocompra() { return tvlabelcontactocompra; }
    public Spinner getSpcontactocompra() { return spcontactocompra; }
    public LinearLayout getLlaccionesdialogocompra() { return llaccionesdialogocompra; }
    public MaterialButton getBtncancelarcompra() { return btncancelarcompra; }
    public MaterialButton getBtnguardarcompra() { return btnguardarcompra; }
}
