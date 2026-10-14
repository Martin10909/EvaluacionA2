package com.example.casholocal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.coordinatorraizprincipal), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Cargar HomeFragment por defecto si no hay un estado guardado previo
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        // Configurar los botones de navegación de la barra inferior personalizada
        configurarNavegacionInferior();
    }

    private void configurarNavegacionInferior() {
        View btnInicio = findViewById(R.id.btnnavegacioninicio);
        View btnCompras = findViewById(R.id.btnnavegacioncompras);
        View btnCompartidos = findViewById(R.id.btnnavegacioncompartidos);
        View btnContactos = findViewById(R.id.btnnavegacioncontactos);
        View btnMapa = findViewById(R.id.btnnavegacionmapa);

        if (btnInicio != null) {
            btnInicio.setOnClickListener(v -> loadFragment(new HomeFragment()));
        }

        if (btnCompras != null) {
            btnCompras.setOnClickListener(v -> loadFragment(new PurchasesFragment()));
        }

        if (btnCompartidos != null) {
            btnCompartidos.setOnClickListener(v -> loadFragment(new SharedFragment()));
        }

        if (btnContactos != null) {
            btnContactos.setOnClickListener(v -> loadFragment(new ContactsFragment()));
        }

        if (btnMapa != null) {
            btnMapa.setOnClickListener(v -> {
                Intent intent = new Intent(this, MapActivity.class);
                startActivity(intent);
            });
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedorfragmento, fragment)
                .commit();
    }
}