package com.example.casholocal;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddSharedExpenseDialogFragment extends DialogFragment {

    private ScrollView scrollnuevogastocompartido;
    private LinearLayout llcontenedorraizdialogocompartido;
    private TextView tvtitulodividirgasto;
    private TextView tvsubtitulodividirgasto;
    private TextInputLayout tilcontenedorconcepto;
    private EditText etconceptoservicio;
    private TextInputLayout tilcontenedormontototal;
    private EditText etmontototaldividir;
    private TextInputLayout tilcontenedorcategoriagasto;
    private EditText etcategoriagasto;
    private TextView tvpreguntaquienpago;
    private Spinner sppagador;
    private TextView tvtituloseleccioncontactos;
    private LinearLayout llseleccionparticipantes;
    private CheckBox chkcontacto1;
    private CardView cvtarjetavistapreviadivision;
    private TextView tvvistapreviadivision;
    private CardView cvpanelgpscompartido;
    private LinearLayout llcontenedorgpscompartido;
    private LinearLayout llcabeceragpscompartido;
    private ImageView imgiconogpscompartido;
    private TextView tvtituloubicacioncompartido;
    private MaterialButton btncapturargpscompartido;
    private TextView tvcoordenadasgpscompartido;
    private EditText etnombrelugarcompartido;
    private LinearLayout llaccionesdialogocompartido;
    private MaterialButton btncancelargastocompartido;
    private MaterialButton btnguardargastocompartido;

    private DatabaseHelper dbHelper;
    private List<ContactoModel> listaContactosBaseDatos = new ArrayList<>();
    private List<CheckBox> listaCheckBoxesDinamicos = new ArrayList<>();
    private String fechaSeleccionada = "Hoy";

    public AddSharedExpenseDialogFragment() {
        // Constructor público vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_shared_expense, container, false);
        initViews(view);

        if (getContext() != null) {
            dbHelper = new DatabaseHelper(getContext());
            cargarPagadoresYContactosReales();
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
        scrollnuevogastocompartido = view.findViewById(R.id.scrollnuevogastocompartido);
        llcontenedorraizdialogocompartido = view.findViewById(R.id.llcontenedorraizdialogocompartido);
        tvtitulodividirgasto = view.findViewById(R.id.tvtitulodividirgasto);
        tvsubtitulodividirgasto = view.findViewById(R.id.tvsubtitulodividirgasto);
        tilcontenedorconcepto = view.findViewById(R.id.tilcontenedorconcepto);
        etconceptoservicio = view.findViewById(R.id.etconceptoservicio);
        tilcontenedormontototal = view.findViewById(R.id.tilcontenedormontototal);
        etmontototaldividir = view.findViewById(R.id.etmontototaldividir);
        tilcontenedorcategoriagasto = view.findViewById(R.id.tilcontenedorcategoriagasto);
        etcategoriagasto = view.findViewById(R.id.etcategoriagasto);
        tvpreguntaquienpago = view.findViewById(R.id.tvpreguntaquienpago);
        sppagador = view.findViewById(R.id.sppagador);
        tvtituloseleccioncontactos = view.findViewById(R.id.tvtituloseleccioncontactos);
        llseleccionparticipantes = view.findViewById(R.id.llseleccionparticipantes);
        chkcontacto1 = view.findViewById(R.id.chkcontacto1);
        cvtarjetavistapreviadivision = view.findViewById(R.id.cvtarjetavistapreviadivision);
        tvvistapreviadivision = view.findViewById(R.id.tvvistapreviadivision);
        cvpanelgpscompartido = view.findViewById(R.id.cvpanelgpscompartido);
        llcontenedorgpscompartido = view.findViewById(R.id.llcontenedorgpscompartido);
        llcabeceragpscompartido = view.findViewById(R.id.llcabeceragpscompartido);
        imgiconogpscompartido = view.findViewById(R.id.imgiconogpscompartido);
        tvtituloubicacioncompartido = view.findViewById(R.id.tvtituloubicacioncompartido);
        btncapturargpscompartido = view.findViewById(R.id.btncapturargpscompartido);
        tvcoordenadasgpscompartido = view.findViewById(R.id.tvcoordenadasgpscompartido);
        etnombrelugarcompartido = view.findViewById(R.id.etnombrelugarcompartido);
        llaccionesdialogocompartido = view.findViewById(R.id.llaccionesdialogocompartido);
        btncancelargastocompartido = view.findViewById(R.id.btncancelargastocompartido);
        btnguardargastocompartido = view.findViewById(R.id.btnguardargastocompartido);
    }

    private void cargarPagadoresYContactosReales() {
        if (dbHelper != null && getContext() != null) {
            listaContactosBaseDatos = dbHelper.obtenerContactos();

            // Cargar Spinner de Pagadores
            List<String> opcionesPagador = new ArrayList<>();
            opcionesPagador.add("Yo (Usuario)");
            for (ContactoModel c : listaContactosBaseDatos) {
                opcionesPagador.add(c.getNombre());
            }

            if (sppagador != null) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, opcionesPagador);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sppagador.setAdapter(adapter);
            }

            // Cargar lista de contactos participantes (sólo el primero marcado por defecto)
            if (llseleccionparticipantes != null) {
                llseleccionparticipantes.removeAllViews();
                listaCheckBoxesDinamicos.clear();

                if (!listaContactosBaseDatos.isEmpty()) {
                    for (int i = 0; i < listaContactosBaseDatos.size(); i++) {
                        ContactoModel contacto = listaContactosBaseDatos.get(i);
                        CheckBox chk = new CheckBox(getContext());
                        chk.setText(contacto.getNombre());
                        chk.setTextColor(0xFF212121);
                        chk.setChecked(i == 0); // Sólo 1 marcado por defecto
                        llseleccionparticipantes.addView(chk);
                        listaCheckBoxesDinamicos.add(chk);
                    }
                } else {
                    if (chkcontacto1 != null) {
                        chkcontacto1.setText("Pablo");
                        chkcontacto1.setChecked(true);
                        llseleccionparticipantes.addView(chkcontacto1);
                        listaCheckBoxesDinamicos.add(chkcontacto1);
                    }
                }
            }
        }
    }

    private void configurarListeners() {
        if (etnombrelugarcompartido != null) {
            etnombrelugarcompartido.setFocusable(false);
            etnombrelugarcompartido.setOnClickListener(v -> mostrarSelectorFecha());
        }

        if (btncapturargpscompartido != null) {
            btncapturargpscompartido.setOnClickListener(v -> {
                if (tvcoordenadasgpscompartido != null) {
                    tvcoordenadasgpscompartido.setText("GPS: -33.4372, -70.6506 (Fijado)");
                }
                Toast.makeText(getContext(), "Ubicación GPS registrada correctamente", Toast.LENGTH_SHORT).show();
            });
        }

        if (btncancelargastocompartido != null) {
            btncancelargastocompartido.setOnClickListener(v -> dismiss());
        }

        if (btnguardargastocompartido != null) {
            btnguardargastocompartido.setOnClickListener(v -> {
                v.setEnabled(false); // Prevenir doble clic
                guardarGastoCompartido();
            });
        }
    }

    private void mostrarSelectorFecha() {
        if (getContext() == null) return;
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            fechaSeleccionada = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month + 1, year);
            if (etnombrelugarcompartido != null) {
                etnombrelugarcompartido.setText(fechaSeleccionada);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void guardarGastoCompartido() {
        if (getContext() == null) return;

        String concepto = etconceptoservicio != null && etconceptoservicio.getText() != null ? etconceptoservicio.getText().toString().trim() : "";
        if (concepto.isEmpty()) {
            concepto = "Compra supermercado";
        }

        String montoStr = etmontototaldividir != null && etmontototaldividir.getText() != null ? etmontototaldividir.getText().toString().trim() : "0";
        double montoTotal = 0;
        try {
            montoTotal = Double.parseDouble(montoStr);
        } catch (Exception ignored) {}

        if (montoTotal <= 0) {
            Toast.makeText(getContext(), "Por favor ingresa un monto total válido", Toast.LENGTH_SHORT).show();
            if (btnguardargastocompartido != null) btnguardargastocompartido.setEnabled(true);
            return;
        }

        String pagador = sppagador != null && sppagador.getSelectedItem() != null ? sppagador.getSelectedItem().toString() : "Yo (Usuario)";

        List<String> participantesSeleccionados = new ArrayList<>();
        for (CheckBox chk : listaCheckBoxesDinamicos) {
            if (chk.isChecked()) {
                participantesSeleccionados.add(chk.getText().toString());
            }
        }

        if (participantesSeleccionados.isEmpty()) {
            participantesSeleccionados.add("Pablo");
        }

        List<String> items = new ArrayList<>();
        items.add(concepto);

        if (dbHelper != null) {
            dbHelper.registrarGastoCompartido(concepto, montoTotal, "equitativa", pagador, fechaSeleccionada, items, participantesSeleccionados, null);
            Toast.makeText(getContext(), "Gasto compartido registrado", Toast.LENGTH_SHORT).show();

            // Refrescar fragmento visible inmediatamente
            if (getParentFragment() instanceof SharedFragment) {
                ((SharedFragment) getParentFragment()).cargarDatosReales();
            } else if (getActivity() != null) {
                Fragment activeFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.contenedorfragmento);
                if (activeFragment instanceof SharedFragment) {
                    ((SharedFragment) activeFragment).cargarDatosReales();
                } else if (activeFragment instanceof HomeFragment) {
                    ((HomeFragment) activeFragment).cargarDatosDesdeBaseDeDatos();
                }
            }

            dismiss();
        }
    }

    // Getters
    public ScrollView getScrollnuevogastocompartido() { return scrollnuevogastocompartido; }
    public LinearLayout getLlcontenedorraizdialogocompartido() { return llcontenedorraizdialogocompartido; }
    public TextView getTvtitulodividirgasto() { return tvtitulodividirgasto; }
    public TextView getTvsubtitulodividirgasto() { return tvsubtitulodividirgasto; }
    public TextInputLayout getTilcontenedorconcepto() { return tilcontenedorconcepto; }
    public EditText getEtconceptoservicio() { return etconceptoservicio; }
    public TextInputLayout getTilcontenedormontototal() { return tilcontenedormontototal; }
    public EditText getEtmontototaldividir() { return etmontototaldividir; }
    public TextInputLayout getTilcontenedorcategoriagasto() { return tilcontenedorcategoriagasto; }
    public EditText getEtcategoriagasto() { return etcategoriagasto; }
    public TextView getTvpreguntaquienpago() { return tvpreguntaquienpago; }
    public Spinner getSppagador() { return sppagador; }
    public TextView getTvtituloseleccioncontactos() { return tvtituloseleccioncontactos; }
    public LinearLayout getLlseleccionparticipantes() { return llseleccionparticipantes; }
    public CheckBox getChkcontacto1() { return chkcontacto1; }
    public CardView getCvtarjetavistapreviadivision() { return cvtarjetavistapreviadivision; }
    public TextView getTvvistapreviadivision() { return tvvistapreviadivision; }
    public CardView getCvpanelgpscompartido() { return cvpanelgpscompartido; }
    public LinearLayout getLlcontenedorgpscompartido() { return llcontenedorgpscompartido; }
    public LinearLayout getLlcabeceragpscompartido() { return llcabeceragpscompartido; }
    public ImageView getImgiconogpscompartido() { return imgiconogpscompartido; }
    public TextView getTvtituloubicacioncompartido() { return tvtituloubicacioncompartido; }
    public MaterialButton getBtncapturargpscompartido() { return btncapturargpscompartido; }
    public TextView getTvcoordenadasgpscompartido() { return tvcoordenadasgpscompartido; }
    public EditText getEtnombrelugarcompartido() { return etnombrelugarcompartido; }
    public LinearLayout getLlaccionesdialogocompartido() { return llaccionesdialogocompartido; }
    public MaterialButton getBtncancelargastocompartido() { return btncancelargastocompartido; }
    public MaterialButton getBtnguardargastocompartido() { return btnguardargastocompartido; }
}