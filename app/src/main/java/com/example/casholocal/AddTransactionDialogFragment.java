package com.example.casholocal;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddTransactionDialogFragment extends DialogFragment {

    private ScrollView scrollnuevatransaccion;
    private LinearLayout llcontenedorraizdialogo;
    private TextView tvtituloregistrarmovimiento;
    private TextView tvsubtitulomovimiento;
    private RadioGroup rgtipomovimiento;
    private RadioButton rbgasto;
    private RadioButton rbingreso;
    private TextInputLayout tilcontenedormonto;
    private EditText etmonto;
    private TextView tvetiquetacuentaasociada;
    private Spinner spcuentaasociada;
    private TextInputLayout tilcontenedorcategoria;
    private EditText etcategoria;
    private TextInputLayout tilcontenedordescripcion;
    private EditText etdescripcionnota;
    private CardView cvpanelgps;
    private LinearLayout llcontenedorgps;
    private LinearLayout llcabeceragps;
    private ImageView imgiconogps;
    private TextView tvtituloubicacion;
    private MaterialButton btncapturargps;
    private TextView tvcoordenadasgps;
    private EditText etnombrelugar;
    private LinearLayout llaccionesdialogo;
    private MaterialButton btncancelarmovimiento;
    private MaterialButton btnguardarmovimiento;

    public interface OnTransactionSavedListener {
        void onTransactionSaved();
    }

    private static final String ARG_LAT = "arg_lat";
    private static final String ARG_LNG = "arg_lng";

    private Double latitudSeleccionada = null;
    private Double longitudSeleccionada = null;
    private OnTransactionSavedListener listener;
    private ActivityResultLauncher<String[]> requestLocationPermissionLauncher;

    private DatabaseHelper dbHelper;
    private List<CuentaModel> listaCuentas = new ArrayList<>();
    private String fechaSeleccionada = "Hoy";

    public AddTransactionDialogFragment() {
        // Constructor público vacío requerido
    }

    public static AddTransactionDialogFragment newInstance(@Nullable Double lat, @Nullable Double lng) {
        AddTransactionDialogFragment fragment = new AddTransactionDialogFragment();
        if (lat != null && lng != null) {
            Bundle args = new Bundle();
            args.putDouble(ARG_LAT, lat);
            args.putDouble(ARG_LNG, lng);
            fragment.setArguments(args);
        }
        return fragment;
    }

    public void setOnTransactionSavedListener(OnTransactionSavedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_CashoLocal_Dialog);

        if (getArguments() != null) {
            if (getArguments().containsKey(ARG_LAT) && getArguments().containsKey(ARG_LNG)) {
                latitudSeleccionada = getArguments().getDouble(ARG_LAT);
                longitudSeleccionada = getArguments().getDouble(ARG_LNG);
            }
        }

        requestLocationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Boolean fineGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean coarseGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);
                    if ((fineGranted != null && fineGranted) || (coarseGranted != null && coarseGranted)) {
                        capturarUbicacionReal();
                    } else {
                        Toast.makeText(getContext(), "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_transaction, container, false);
        initViews(view);

        if (latitudSeleccionada != null && longitudSeleccionada != null && tvcoordenadasgps != null) {
            tvcoordenadasgps.setText(String.format(Locale.getDefault(), "GPS: %.4f, %.4f (Mapa)", latitudSeleccionada, longitudSeleccionada));
        }

        if (getContext() != null) {
            dbHelper = new DatabaseHelper(getContext());
            configurarSpinner();
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
        scrollnuevatransaccion = view.findViewById(R.id.scrollnuevatransaccion);
        llcontenedorraizdialogo = view.findViewById(R.id.llcontenedorraizdialogo);
        tvtituloregistrarmovimiento = view.findViewById(R.id.tvtituloregistrarmovimiento);
        tvsubtitulomovimiento = view.findViewById(R.id.tvsubtitulomovimiento);
        rgtipomovimiento = view.findViewById(R.id.rgtipomovimiento);
        rbgasto = view.findViewById(R.id.rbgasto);
        rbingreso = view.findViewById(R.id.rbingreso);
        tilcontenedormonto = view.findViewById(R.id.tilcontenedormonto);
        etmonto = view.findViewById(R.id.etmonto);
        tvetiquetacuentaasociada = view.findViewById(R.id.tvetiquetacuentaasociada);
        spcuentaasociada = view.findViewById(R.id.spcuentaasociada);
        tilcontenedorcategoria = view.findViewById(R.id.tilcontenedorcategoria);
        etcategoria = view.findViewById(R.id.etcategoria);
        tilcontenedordescripcion = view.findViewById(R.id.tilcontenedordescripcion);
        etdescripcionnota = view.findViewById(R.id.etdescripcionnota);
        cvpanelgps = view.findViewById(R.id.cvpanelgps);
        llcontenedorgps = view.findViewById(R.id.llcontenedorgps);
        llcabeceragps = view.findViewById(R.id.llcabeceragps);
        imgiconogps = view.findViewById(R.id.imgiconogps);
        tvtituloubicacion = view.findViewById(R.id.tvtituloubicacion);
        btncapturargps = view.findViewById(R.id.btncapturargps);
        tvcoordenadasgps = view.findViewById(R.id.tvcoordenadasgps);
        etnombrelugar = view.findViewById(R.id.etnombrelugar);
        llaccionesdialogo = view.findViewById(R.id.llaccionesdialogo);
        btncancelarmovimiento = view.findViewById(R.id.btncancelarmovimiento);
        btnguardarmovimiento = view.findViewById(R.id.btnguardarmovimiento);
    }

    private void configurarSpinner() {
        if (dbHelper != null && spcuentaasociada != null && getContext() != null) {
            listaCuentas = dbHelper.obtenerCuentas();
            List<String> nombresCuentas = new ArrayList<>();
            for (CuentaModel c : listaCuentas) {
                nombresCuentas.add(c.getNombre() + " (" + c.getTipo() + ")");
            }
            if (nombresCuentas.isEmpty()) {
                nombresCuentas.add("Cuenta Principal");
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, nombresCuentas);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spcuentaasociada.setAdapter(adapter);
        }
    }

    private void configurarListeners() {
        if (rgtipomovimiento != null) {
            rgtipomovimiento.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == R.id.rbingreso) {
                    if (etcategoria != null && (etcategoria.getText() == null || etcategoria.getText().toString().isEmpty())) {
                        etcategoria.setHint("Ej. Sueldo, Venta, Regalo");
                    }
                } else {
                    if (etcategoria != null && (etcategoria.getText() == null || etcategoria.getText().toString().isEmpty())) {
                        etcategoria.setHint("Ej. Alimentación, Transporte, Salud");
                    }
                }
            });
        }

        if (etnombrelugar != null) {
            etnombrelugar.setFocusable(false);
            etnombrelugar.setOnClickListener(v -> mostrarSelectorFecha());
        }

        if (btncapturargps != null) {
            btncapturargps.setOnClickListener(v -> capturarUbicacionReal());
        }

        if (btncancelarmovimiento != null) {
            btncancelarmovimiento.setOnClickListener(v -> dismiss());
        }

        if (btnguardarmovimiento != null) {
            btnguardarmovimiento.setOnClickListener(v -> {
                v.setEnabled(false);
                guardarMovimiento();
            });
        }
    }

    private void capturarUbicacionReal() {
        if (getContext() == null) return;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermissionLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
            return;
        }

        if (tvcoordenadasgps != null) {
            tvcoordenadasgps.setText("Obteniendo GPS...");
        }

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        latitudSeleccionada = location.getLatitude();
                        longitudSeleccionada = location.getLongitude();
                        if (tvcoordenadasgps != null) {
                            tvcoordenadasgps.setText(String.format(Locale.getDefault(), "GPS: %.4f, %.4f (Actual)", latitudSeleccionada, longitudSeleccionada));
                        }
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Ubicación GPS obtenida correctamente", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        fusedLocationClient.getLastLocation().addOnSuccessListener(lastLoc -> {
                            if (lastLoc != null) {
                                latitudSeleccionada = lastLoc.getLatitude();
                                longitudSeleccionada = lastLoc.getLongitude();
                                if (tvcoordenadasgps != null) {
                                    tvcoordenadasgps.setText(String.format(Locale.getDefault(), "GPS: %.4f, %.4f (Última conocida)", latitudSeleccionada, longitudSeleccionada));
                                }
                                if (getContext() != null) {
                                    Toast.makeText(getContext(), "Ubicación GPS obtenida", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                if (tvcoordenadasgps != null) {
                                    tvcoordenadasgps.setText("No se pudo obtener señal GPS");
                                }
                                if (getContext() != null) {
                                    Toast.makeText(getContext(), "Activa tu ubicación o GPS", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(e -> {
                    if (tvcoordenadasgps != null) {
                        tvcoordenadasgps.setText("Error GPS");
                    }
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void mostrarSelectorFecha() {
        if (getContext() == null) return;
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            fechaSeleccionada = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month + 1, year);
            if (etnombrelugar != null) {
                etnombrelugar.setText(fechaSeleccionada);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void guardarMovimiento() {
        String montoStr = etmonto != null && etmonto.getText() != null ? etmonto.getText().toString().trim() : "";
        if (montoStr.isEmpty()) {
            Toast.makeText(getContext(), "Por favor ingresa un monto válido", Toast.LENGTH_SHORT).show();
            if (btnguardarmovimiento != null) btnguardarmovimiento.setEnabled(true);
            return;
        }

        double monto = 0;
        try {
            monto = Double.parseDouble(montoStr);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Monto inválido", Toast.LENGTH_SHORT).show();
            if (btnguardarmovimiento != null) btnguardarmovimiento.setEnabled(true);
            return;
        }

        boolean esIngreso = rbingreso != null && rbingreso.isChecked();
        String tipo = esIngreso ? "ingreso" : "egreso";
        String categoria = etcategoria != null && etcategoria.getText() != null && !etcategoria.getText().toString().trim().isEmpty() ?
                etcategoria.getText().toString().trim() : (esIngreso ? "Sueldo" : "Alimentación");

        String descripcion = etdescripcionnota != null && etdescripcionnota.getText() != null && !etdescripcionnota.getText().toString().trim().isEmpty() ?
                etdescripcionnota.getText().toString().trim() : (esIngreso ? "Ingreso de dinero" : "Gasto registrado");

        long cuentaId = 1;
        if (spcuentaasociada != null && !listaCuentas.isEmpty()) {
            int pos = spcuentaasociada.getSelectedItemPosition();
            if (pos >= 0 && pos < listaCuentas.size()) {
                cuentaId = listaCuentas.get(pos).getId();
            }
        }

        double latFinal = latitudSeleccionada != null ? latitudSeleccionada : -33.4372;
        double lngFinal = longitudSeleccionada != null ? longitudSeleccionada : -70.6506;

        if (dbHelper != null) {
            dbHelper.insertarMovimiento(new MovimientoModel(0, cuentaId, tipo, monto, descripcion, fechaSeleccionada, latFinal, lngFinal, categoria));
            Toast.makeText(getContext(), (esIngreso ? "Ingreso" : "Gasto") + " guardado con éxito", Toast.LENGTH_SHORT).show();

            if (listener != null) {
                listener.onTransactionSaved();
            }

            if (getParentFragment() instanceof HomeFragment) {
                ((HomeFragment) getParentFragment()).cargarDatosDesdeBaseDeDatos();
            } else if (getActivity() instanceof MapActivity) {
                ((MapActivity) getActivity()).cargarDatos();
            } else if (getActivity() != null) {
                Fragment activeFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.contenedorfragmento);
                if (activeFragment instanceof HomeFragment) {
                    ((HomeFragment) activeFragment).cargarDatosDesdeBaseDeDatos();
                }
            }
            dismiss();
        }
    }

    // Getters
    public ScrollView getScrollnuevatransaccion() { return scrollnuevatransaccion; }
    public LinearLayout getLlcontenedorraizdialogo() { return llcontenedorraizdialogo; }
    public TextView getTvtituloregistrarmovimiento() { return tvtituloregistrarmovimiento; }
    public TextView getTvsubtitulomovimiento() { return tvsubtitulomovimiento; }
    public RadioGroup getRgtipomovimiento() { return rgtipomovimiento; }
    public RadioButton getRbgasto() { return rbgasto; }
    public RadioButton getRbingreso() { return rbingreso; }
    public TextInputLayout getTilcontenedormonto() { return tilcontenedormonto; }
    public EditText getEtmonto() { return etmonto; }
    public TextView getTvetiquetacuentaasociada() { return tvetiquetacuentaasociada; }
    public Spinner getSpcuentaasociada() { return spcuentaasociada; }
    public TextInputLayout getTilcontenedorcategoria() { return tilcontenedorcategoria; }
    public EditText getEtcategoria() { return etcategoria; }
    public TextInputLayout getTilcontenedordescripcion() { return tilcontenedordescripcion; }
    public EditText getEtdescripcionnota() { return etdescripcionnota; }
    public CardView getCvpanelgps() { return cvpanelgps; }
    public LinearLayout getLlcontenedorgps() { return llcontenedorgps; }
    public LinearLayout getLlcabeceragps() { return llcabeceragps; }
    public ImageView getImgiconogps() { return imgiconogps; }
    public TextView getTvtituloubicacion() { return tvtituloubicacion; }
    public MaterialButton getBtncapturargps() { return btncapturargps; }
    public TextView getTvcoordenadasgps() { return tvcoordenadasgps; }
    public EditText getEtnombrelugar() { return etnombrelugar; }
    public LinearLayout getLlaccionesdialogo() { return llaccionesdialogo; }
    public MaterialButton getBtncancelarmovimiento() { return btncancelarmovimiento; }
    public MaterialButton getBtnguardarmovimiento() { return btnguardarmovimiento; }
}