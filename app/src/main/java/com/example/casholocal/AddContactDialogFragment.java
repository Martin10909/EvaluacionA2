package com.example.casholocal;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class AddContactDialogFragment extends DialogFragment {

    // 14 Componentes de la Paleta
    private LinearLayout llnuevocontactoraiz;
    private TextView tvtitulonuevocontacto;
    private TextView tvsubtitulonuevocontacto;
    private TextInputLayout tilcontenedornombrecontacto;
    private EditText etnombrecontactocompleto;
    private TextInputLayout tilcontenedortelefonocontacto;
    private EditText ettelefonowhatsapp;
    private TextInputLayout tilcontenedorcorreocontacto;
    private EditText etcorreoelectronico;
    private TextInputLayout tilcontenedornotascontacto;
    private EditText etnotasrelacion;
    private LinearLayout llaccionesdialogocontacto;
    private MaterialButton btncancelarcontacto;
    private MaterialButton btnguardarcontacto;

    public AddContactDialogFragment() {
        // Constructor público vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_contact, container, false);
        initViews(view);
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
        llnuevocontactoraiz = view.findViewById(R.id.llnuevocontactoraiz);
        tvtitulonuevocontacto = view.findViewById(R.id.tvtitulonuevocontacto);
        tvsubtitulonuevocontacto = view.findViewById(R.id.tvsubtitulonuevocontacto);
        tilcontenedornombrecontacto = view.findViewById(R.id.tilcontenedornombrecontacto);
        etnombrecontactocompleto = view.findViewById(R.id.etnombrecontactocompleto);
        tilcontenedortelefonocontacto = view.findViewById(R.id.tilcontenedortelefonocontacto);
        ettelefonowhatsapp = view.findViewById(R.id.ettelefonowhatsapp);
        tilcontenedorcorreocontacto = view.findViewById(R.id.tilcontenedorcorreocontacto);
        etcorreoelectronico = view.findViewById(R.id.etcorreoelectronico);
        tilcontenedornotascontacto = view.findViewById(R.id.tilcontenedornotascontacto);
        etnotasrelacion = view.findViewById(R.id.etnotasrelacion);
        llaccionesdialogocontacto = view.findViewById(R.id.llaccionesdialogocontacto);
        btncancelarcontacto = view.findViewById(R.id.btncancelarcontacto);
        btnguardarcontacto = view.findViewById(R.id.btnguardarcontacto);
    }

    private void configurarListeners() {
        if (btncancelarcontacto != null) {
            btncancelarcontacto.setOnClickListener(v -> dismiss());
        }

        if (btnguardarcontacto != null) {
            btnguardarcontacto.setOnClickListener(v -> {
                Toast.makeText(getContext(), "Contacto guardado exitosamente", Toast.LENGTH_SHORT).show();
                dismiss();
            });
        }
    }

    // Getters
    public LinearLayout getLlnuevocontactoraiz() { return llnuevocontactoraiz; }
    public TextView getTvtitulonuevocontacto() { return tvtitulonuevocontacto; }
    public TextView getTvsubtitulonuevocontacto() { return tvsubtitulonuevocontacto; }
    public TextInputLayout getTilcontenedornombrecontacto() { return tilcontenedornombrecontacto; }
    public EditText getEtnombrecontactocompleto() { return etnombrecontactocompleto; }
    public TextInputLayout getTilcontenedortelefonocontacto() { return tilcontenedortelefonocontacto; }
    public EditText getEttelefonowhatsapp() { return ettelefonowhatsapp; }
    public TextInputLayout getTilcontenedorcorreocontacto() { return tilcontenedorcorreocontacto; }
    public EditText getEtcorreoelectronico() { return etcorreoelectronico; }
    public TextInputLayout getTilcontenedornotascontacto() { return tilcontenedornotascontacto; }
    public EditText getEtnotasrelacion() { return etnotasrelacion; }
    public LinearLayout getLlaccionesdialogocontacto() { return llaccionesdialogocontacto; }
    public MaterialButton getBtncancelarcontacto() { return btncancelarcontacto; }
    public MaterialButton getBtnguardarcontacto() { return btnguardarcontacto; }
}