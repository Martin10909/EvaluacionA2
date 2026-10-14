package com.example.casholocal;

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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class AddSharedExpenseDialogFragment extends DialogFragment {

    // 28 Componentes de la Paleta
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

    public AddSharedExpenseDialogFragment() {
        // Constructor público vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_shared_expense, container, false);
        initViews(view);
        configurarSpinner();
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

    private void configurarSpinner() {
        if (getContext() != null && sppagador != null) {
            String[] pagadores = {"Yo (Usuario)", "Carlos Mendoza", "Juan Pérez", "Ana Gómez"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, pagadores);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sppagador.setAdapter(adapter);
        }
    }

    private void configurarListeners() {
        if (btncapturargpscompartido != null) {
            btncapturargpscompartido.setOnClickListener(v -> {
                if (getActivity() != null) {
                    Intent intent = new Intent(getActivity(), MapActivity.class);
                    startActivity(intent);
                }
            });
        }

        if (btncancelargastocompartido != null) {
            btncancelargastocompartido.setOnClickListener(v -> dismiss());
        }

        if (btnguardargastocompartido != null) {
            btnguardargastocompartido.setOnClickListener(v -> {
                if (getContext() != null) {
                    String concepto = etconceptoservicio != null && etconceptoservicio.getText() != null ? etconceptoservicio.getText().toString().trim() : "Gasto Compartido";
                    String montoStr = etmontototaldividir != null && etmontototaldividir.getText() != null ? etmontototaldividir.getText().toString().trim() : "0";
                    double montoTotal = 0;
                    try {
                        montoTotal = Double.parseDouble(montoStr);
                    } catch (Exception ignored) {}

                    String pagador = sppagador != null && sppagador.getSelectedItem() != null ? sppagador.getSelectedItem().toString() : "Yo (Usuario)";

                    List<String> items = new ArrayList<>();
                    items.add(concepto.isEmpty() ? "Servicio / Producto" : concepto);

                    List<String> participantes = new ArrayList<>();
                    if (chkcontacto1 != null && chkcontacto1.isChecked()) {
                        participantes.add("Carlos Mendoza");
                    } else {
                        participantes.add("Juan Pérez");
                    }

                    DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                    dbHelper.registrarGastoCompartido(concepto.isEmpty() ? "Gasto Grupal" : concepto, montoTotal, "equitativa", pagador, "Hoy", items, participantes, null);

                    Toast.makeText(getContext(), "Gasto compartido registrado exitosamente", Toast.LENGTH_SHORT).show();

                    if (getParentFragment() instanceof SharedFragment) {
                        ((SharedFragment) getParentFragment()).cargarDatosReales();
                    }

                    dismiss();
                }
            });
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