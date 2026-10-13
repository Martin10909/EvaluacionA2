package com.example.casholocal;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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

        BottomNavigationView navegacioninferior = findViewById(R.id.navegacioninferior);

        // Cargar HomeFragment por defecto si no hay un estado guardado previo
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        // Listener para la navegación de la barra inferior con 5 opciones
        if (navegacioninferior != null) {
            navegacioninferior.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    loadFragment(new HomeFragment());
                    return true;
                } else if (itemId == R.id.nav_compras) {
                    loadFragment(new PurchasesFragment());
                    return true;
                } else if (itemId == R.id.nav_shared) {
                    loadFragment(new SharedFragment());
                    return true;
                } else if (itemId == R.id.nav_contacts) {
                    loadFragment(new ContactsFragment());
                    return true;
                } else if (itemId == R.id.nav_map) {
                    Intent intent = new Intent(this, MapActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            });
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedorfragmento, fragment)
                .commit();
    }
}