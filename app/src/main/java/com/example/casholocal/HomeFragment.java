package com.example.casholocal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class HomeFragment extends Fragment {

    // Layouts & Containers
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

    // TextViews
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

    // Buttons
    private ImageButton btnnotificaciones;
    private ImageButton btnperfilusuario;
    private MaterialButton btnagregarcuenta;
    private ImageButton btnfiltrartransacciones;

    // Widgets & RecyclerViews
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

    public HomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
        configurarListeners();
        return view;
    }

    private void initViews(View view) {
        // Layouts
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

        // Text
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

        // Buttons
        btnnotificaciones = view.findViewById(R.id.btnnotificaciones);
        btnperfilusuario = view.findViewById(R.id.btnperfilusuario);
        btnagregarcuenta = view.findViewById(R.id.btnagregarcuenta);
        btnfiltrartransacciones = view.findViewById(R.id.btnfiltrartransacciones);

        // Widgets
        imgavatar = view.findViewById(R.id.imgavatar);
        vdivisor1 = view.findViewById(R.id.vdivisor1);
        imgiconoingreso = view.findViewById(R.id.imgiconoingreso);
        imgiconogasto = view.findViewById(R.id.imgiconogasto);
        imgiconodividir = view.findViewById(R.id.imgiconodividir);
        tilbusquedatransacciones = view.findViewById(R.id.tilbusquedatransacciones);
        etbuscartransacciones = view.findViewById(R.id.etbuscartransacciones);
        pbcargando = view.findViewById(R.id.pbcargando);

        // Containers
        rvcuentas = view.findViewById(R.id.rvcuentas);
        rvtransaccionesrecientes = view.findViewById(R.id.rvtransaccionesrecientes);
    }

    private void configurarListeners() {
        View.OnClickListener listenerAbrirDialogo = v -> {
            if (getParentFragmentManager() != null) {
                AddTransactionDialogFragment dialog = new AddTransactionDialogFragment();
                dialog.show(getParentFragmentManager(), "AddTransactionDialog");
            }
        };

        if (btningresorapido != null) btningresorapido.setOnClickListener(listenerAbrirDialogo);
        if (btngastorapido != null) btngastorapido.setOnClickListener(listenerAbrirDialogo);
        if (btndividirrapido != null) btndividirrapido.setOnClickListener(listenerAbrirDialogo);

        if (btnfiltrartransacciones != null) {
            btnfiltrartransacciones.setOnClickListener(v -> {
                if (getActivity() != null) {
                    Intent intent = new Intent(getActivity(), MapActivity.class);
                    startActivity(intent);
                }
            });
        }
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