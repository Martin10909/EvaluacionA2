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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ContactsFragment extends Fragment {

    // 7 Componentes de la Paleta
    private CoordinatorLayout coordinatorraizcontactos;
    private LinearLayout llcontenedorraizcontactos;
    private LinearLayout llencabezadocontactos;
    private TextView tvtitulodirectoriocontactos;
    private TextView tvcontadorcontactos;
    private RecyclerView rvlistacontactos;
    private FloatingActionButton fabagregarcontacto;

    public ContactsFragment() {
        // Constructor público vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        initViews(view);
        configurarListeners();
        return view;
    }

    private void initViews(View view) {
        coordinatorraizcontactos = view.findViewById(R.id.coordinatorraizcontactos);
        llcontenedorraizcontactos = view.findViewById(R.id.llcontenedorraizcontactos);
        llencabezadocontactos = view.findViewById(R.id.llencabezadocontactos);
        tvtitulodirectoriocontactos = view.findViewById(R.id.tvtitulodirectoriocontactos);
        tvcontadorcontactos = view.findViewById(R.id.tvcontadorcontactos);
        rvlistacontactos = view.findViewById(R.id.rvlistacontactos);
        fabagregarcontacto = view.findViewById(R.id.fabagregarcontacto);
    }

    private void configurarListeners() {
        if (fabagregarcontacto != null) {
            fabagregarcontacto.setOnClickListener(v -> 
                Toast.makeText(getContext(), "Agregar nuevo contacto", Toast.LENGTH_SHORT).show()
            );
        }
    }

    // Getters
    public CoordinatorLayout getCoordinatorraizcontactos() { return coordinatorraizcontactos; }
    public LinearLayout getLlcontenedorraizcontactos() { return llcontenedorraizcontactos; }
    public LinearLayout getLlencabezadocontactos() { return llencabezadocontactos; }
    public TextView getTvtitulodirectoriocontactos() { return tvtitulodirectoriocontactos; }
    public TextView getTvcontadorcontactos() { return tvcontadorcontactos; }
    public RecyclerView getRvlistacontactos() { return rvlistacontactos; }
    public FloatingActionButton getFabagregarcontacto() { return fabagregarcontacto; }
}