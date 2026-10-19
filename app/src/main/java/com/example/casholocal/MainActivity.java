package com.example.casholocal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    private boolean estaAutenticado = false;

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

        if (savedInstanceState != null && savedInstanceState.getBoolean("estaAutenticado", false)) {
            estaAutenticado = true;
            inicializarAplicacion(savedInstanceState);
        } else {
            verificarBiometria(savedInstanceState);
        }
    }

    private void verificarBiometria(Bundle savedInstanceState) {
        BiometricManager biometricManager = BiometricManager.from(this);
        int estadoBiometria = biometricManager.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL
        );

        if (estadoBiometria == BiometricManager.BIOMETRIC_SUCCESS) {
            Executor executor = ContextCompat.getMainExecutor(this);
            BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    estaAutenticado = true;
                    inicializarAplicacion(savedInstanceState);
                }

                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    mostrarDialogoReintentar(savedInstanceState);
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(MainActivity.this, "Huella no reconocida", Toast.LENGTH_SHORT).show();
                }
            });

            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Acceso LocalCash")
                    .setSubtitle("Confirma tu identidad para ingresar a tus finanzas")
                    .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                    .build();

            biometricPrompt.authenticate(promptInfo);
        } else {
            estaAutenticado = true;
            inicializarAplicacion(savedInstanceState);
        }
    }

    private void mostrarDialogoReintentar(Bundle savedInstanceState) {
        new AlertDialog.Builder(this)
                .setTitle("Autenticación requerida")
                .setMessage("Es necesario autenticarse con huella o credencial del dispositivo para acceder a LocalCash.")
                .setPositiveButton("Reintentar", (dialog, which) -> verificarBiometria(savedInstanceState))
                .setNegativeButton("Salir", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    private void inicializarAplicacion(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
        configurarNavegacionInferior();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("estaAutenticado", estaAutenticado);
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