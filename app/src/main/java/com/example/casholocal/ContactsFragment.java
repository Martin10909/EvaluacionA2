package com.example.casholocal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ContactsFragment extends Fragment {

    // 7 Componentes de la Paleta
    private CoordinatorLayout coordinatorraizcontactos;
    private LinearLayout llcontenedorraizcontactos;
    private LinearLayout llencabezadocontactos;
    private TextView tvtitulodirectoriocontactos;
    private TextView tvcontadorcontactos;
    private RecyclerView rvlistacontactos;
    private FloatingActionButton fabagregarcontacto;

    private DatabaseHelper dbHelper;
    private ContactoAdapter adapter;

    public ContactsFragment() {
        // Constructor público vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        initViews(view);

        if (getContext() != null) {
            dbHelper = new DatabaseHelper(getContext());
            configurarRecyclerView();
            cargarContactosReales();
        }

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

    private void configurarRecyclerView() {
        if (rvlistacontactos != null && getContext() != null) {
            rvlistacontactos.setLayoutManager(new LinearLayoutManager(getContext()));
            List<ContactoModel> contactos = dbHelper.obtenerContactos();
            adapter = new ContactoAdapter(contactos);
            rvlistacontactos.setAdapter(adapter);
        }
    }

    public void cargarContactosReales() {
        if (dbHelper != null) {
            List<ContactoModel> contactos = dbHelper.obtenerContactos();
            int cantidad = dbHelper.obtenerCantidadContactos();

            if (tvcontadorcontactos != null) {
                tvcontadorcontactos.setText(cantidad + " contactos registrados");
            }

            if (adapter != null) {
                adapter.actualizarLista(contactos);
            }
        }
    }

    private void configurarListeners() {
        if (fabagregarcontacto != null) {
            fabagregarcontacto.setOnClickListener(v -> {
                if (getParentFragmentManager() != null) {
                    AddContactDialogFragment dialog = new AddContactDialogFragment();
                    dialog.show(getParentFragmentManager(), "AddContactDialog");
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarContactosReales();
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