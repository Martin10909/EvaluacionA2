package com.example.casholocal;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION_PERMISSION = 1001;

    private CoordinatorLayout coordinatorraizmapa;
    private LinearLayout llencabezadomapa;
    private ImageButton btnatrasmapa;
    private LinearLayout lltitulosmapa;
    private TextView tvtitulomapa;
    private TextView tvsubtitulomapa;
    private MaterialButton btnrecentrarmapa;
    private View scrollfichasmapa;
    private ChipGroup grupofichasmapa;
    private Chip fichatodos;
    private Chip fichagastos;
    private Chip fichaingresos;
    private CardView tarjetamarcadorseleccionado;
    private LinearLayout llcontenedortarjeta;
    private LinearLayout llfilasuperiormarcador;
    private ImageView imgiconomarcador;
    private LinearLayout lldetallesmarcador;
    private TextView tvtitulomarcador;
    private TextView tvsubtitulomarcador;
    private TextView tvmontomarcador;
    private View vdivisormarcador;
    private LinearLayout llfilainferiormarcador;
    private TextView tvcoordenadasmarcador;
    private MaterialButton btncompartirmarcador;
    private FrameLayout flcapacargamapa;
    private ProgressBar pbcargando;

    private GoogleMap googleMap;
    private DatabaseHelper dbHelper;
    private List<MovimientoModel> listaMovimientos = new ArrayList<>();
    private LatLng puntoInicial = new LatLng(-33.4372, -70.6506);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        dbHelper = new DatabaseHelper(this);

        initViews();
        configurarMapa();
        configurarListeners();
    }

    private void initViews() {
        coordinatorraizmapa = findViewById(R.id.coordinatorraizmapa);
        llencabezadomapa = findViewById(R.id.llencabezadomapa);
        btnatrasmapa = findViewById(R.id.btnatrasmapa);
        lltitulosmapa = findViewById(R.id.lltitulosmapa);
        tvtitulomapa = findViewById(R.id.tvtitulomapa);
        tvsubtitulomapa = findViewById(R.id.tvsubtitulomapa);
        btnrecentrarmapa = findViewById(R.id.btnrecentrarmapa);
        scrollfichasmapa = findViewById(R.id.scrollfichasmapa);
        grupofichasmapa = findViewById(R.id.grupofichasmapa);
        fichatodos = findViewById(R.id.fichatodos);
        fichagastos = findViewById(R.id.fichagastos);
        fichaingresos = findViewById(R.id.fichaingresos);
        tarjetamarcadorseleccionado = findViewById(R.id.tarjetamarcadorseleccionado);
        llcontenedortarjeta = findViewById(R.id.llcontenedortarjeta);
        llfilasuperiormarcador = findViewById(R.id.llfilasuperiormarcador);
        imgiconomarcador = findViewById(R.id.imgiconomarcador);
        lldetallesmarcador = findViewById(R.id.lldetallesmarcador);
        tvtitulomarcador = findViewById(R.id.tvtitulomarcador);
        tvsubtitulomarcador = findViewById(R.id.tvsubtitulomarcador);
        tvmontomarcador = findViewById(R.id.tvmontomarcador);
        vdivisormarcador = findViewById(R.id.vdivisormarcador);
        llfilainferiormarcador = findViewById(R.id.llfilainferiormarcador);
        tvcoordenadasmarcador = findViewById(R.id.tvcoordenadasmarcador);
        btncompartirmarcador = findViewById(R.id.btncompartirmarcador);
        flcapacargamapa = findViewById(R.id.flcapacargamapa);
        pbcargando = findViewById(R.id.pbcargando);
    }

    private void configurarMapa() {
        try {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapafragmentogotable);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Iniciando servicio de mapas...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        this.googleMap = map;
        try {
            this.googleMap.getUiSettings().setZoomControlsEnabled(true);
            this.googleMap.getUiSettings().setCompassEnabled(true);
            this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(puntoInicial, 13f));

            habilitarUbicacionUsuario();

            // Mantener presionado para crear ícono / marcador en las coordenadas tocadas
            this.googleMap.setOnMapLongClickListener(this::mostrarDialogoCrearMarcadorEnCoordenadas);

        } catch (Exception ignored) {}

        cargarDatos();
    }

    private void mostrarDialogoCrearMarcadorEnCoordenadas(LatLng latLng) {
        if (latLng == null) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nuevo Punto de Gasto / Ingreso");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 16, 32, 16);

        final EditText etNombre = new EditText(this);
        etNombre.setHint("Nombre del lugar o gasto (ej. Café)");
        layout.addView(etNombre);

        final EditText etMonto = new EditText(this);
        etMonto.setHint("Monto ($ CLP)");
        etMonto.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(etMonto);

        final EditText etCat = new EditText(this);
        etCat.setHint("Categoría (ej. Alimentación, Compras)");
        layout.addView(etCat);

        builder.setView(layout);

        builder.setPositiveButton("Crear Ícono en Mapa", (dialog, which) -> {
            String nombre = etNombre.getText().toString().trim();
            String montoStr = etMonto.getText().toString().trim();
            String cat = etCat.getText().toString().trim();

            if (nombre.isEmpty()) nombre = "Gasto Ubicación";
            if (cat.isEmpty()) cat = "General";

            double monto = 0;
            try {
                monto = Double.parseDouble(montoStr);
            } catch (Exception ignored) {}

            if (dbHelper != null) {
                Calendar c = Calendar.getInstance();
                String fechaHoy = String.format(Locale.getDefault(), "%02d/%02d/%d", c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR));
                dbHelper.insertarMovimiento(new MovimientoModel(0, 1, "egreso", monto, nombre, fechaHoy, latLng.latitude, latLng.longitude, cat));
                cargarDatos();
                Toast.makeText(this, "Ícono y marcador creado en el mapa", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void habilitarUbicacionUsuario() {
        if (googleMap == null) return;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            try {
                googleMap.setMyLocationEnabled(true);
            } catch (SecurityException ignored) {}
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (googleMap != null) {
                    try {
                        googleMap.setMyLocationEnabled(true);
                    } catch (SecurityException ignored) {}
                }
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void cargarDatos() {
        if (dbHelper != null) {
            listaMovimientos = dbHelper.obtenerMovimientos("");
            mostrarMarcadores("todos");
        }
    }

    private void mostrarMarcadores(String filtro) {
        if (googleMap == null) return;

        googleMap.clear();

        boolean primerMarcadorFijado = false;

        for (MovimientoModel mov : listaMovimientos) {
            if ("gastos".equals(filtro) && !"egreso".equalsIgnoreCase(mov.getTipo())) {
                continue;
            }
            if ("ingresos".equals(filtro) && !"ingreso".equalsIgnoreCase(mov.getTipo())) {
                continue;
            }

            double lat = mov.getLatitud();
            double lng = mov.getLongitud();

            if (lat == 0 && lng == 0) {
                lat = -33.4372;
                lng = -70.6506;
            }

            LatLng pos = new LatLng(lat, lng);
            boolean esIngreso = "ingreso".equalsIgnoreCase(mov.getTipo());

            MarkerOptions options = new MarkerOptions()
                    .position(pos)
                    .title(mov.getDescripcion())
                    .snippet(FormatoMoneda.aPesoChileno(mov.getMonto()))
                    .icon(BitmapDescriptorFactory.defaultMarker(esIngreso ? BitmapDescriptorFactory.HUE_GREEN : BitmapDescriptorFactory.HUE_RED));

            Marker marker = googleMap.addMarker(options);
            if (marker != null) {
                marker.setTag(mov);
            }

            if (!primerMarcadorFijado) {
                mostrarDetalleMarcador(mov);
                puntoInicial = pos;
                primerMarcadorFijado = true;
            }
        }

        googleMap.setOnMarkerClickListener(marker -> {
            Object tag = marker.getTag();
            if (tag instanceof MovimientoModel) {
                mostrarDetalleMarcador((MovimientoModel) tag);
            }
            return false;
        });
    }

    private void mostrarDetalleMarcador(MovimientoModel mov) {
        if (mov == null) return;

        if (tvtitulomarcador != null) {
            tvtitulomarcador.setText(mov.getDescripcion());
        }
        if (tvsubtitulomarcador != null) {
            tvsubtitulomarcador.setText(mov.getCategoria() + " - " + mov.getFecha());
        }
        if (tvmontomarcador != null) {
            tvmontomarcador.setText(FormatoMoneda.aPesoChileno(mov.getMonto()));
            if ("ingreso".equalsIgnoreCase(mov.getTipo())) {
                tvmontomarcador.setTextColor(0xFF2E7D32);
            } else {
                tvmontomarcador.setTextColor(0xFFC62828);
            }
        }
        if (tvcoordenadasmarcador != null) {
            tvcoordenadasmarcador.setText("GPS: " + mov.getLatitud() + ", " + mov.getLongitud());
        }
        if (tarjetamarcadorseleccionado != null) {
            tarjetamarcadorseleccionado.setVisibility(View.VISIBLE);
        }
    }

    private void configurarListeners() {
        if (btnatrasmapa != null) {
            btnatrasmapa.setOnClickListener(v -> finish());
        }

        if (btnrecentrarmapa != null) {
            btnrecentrarmapa.setOnClickListener(v -> {
                if (googleMap != null) {
                    try {
                        if (googleMap.getMyLocation() != null) {
                            LatLng miUbicacion = new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude());
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(miUbicacion, 16f));
                            Toast.makeText(this, "Navegando a tu ubicación actual", Toast.LENGTH_SHORT).show();
                        } else {
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(puntoInicial, 15f));
                            Toast.makeText(this, "Centrando mapa en transacciones", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(puntoInicial, 15f));
                    }
                }
            });
        }

        if (fichatodos != null) {
            fichatodos.setOnClickListener(v -> mostrarMarcadores("todos"));
        }
        if (fichagastos != null) {
            fichagastos.setOnClickListener(v -> mostrarMarcadores("gastos"));
        }
        if (fichaingresos != null) {
            fichaingresos.setOnClickListener(v -> mostrarMarcadores("ingresos"));
        }

        if (btncompartirmarcador != null) {
            btncompartirmarcador.setOnClickListener(v -> {
                String desc = tvtitulomarcador != null ? tvtitulomarcador.getText().toString() : "Gasto";
                String monto = tvmontomarcador != null ? tvmontomarcador.getText().toString() : "";
                String coords = tvcoordenadasmarcador != null ? tvcoordenadasmarcador.getText().toString() : "";
                Toast.makeText(this, desc + " (" + monto + ") - " + coords, Toast.LENGTH_SHORT).show();
            });
        }
    }

    // Getters
    public CoordinatorLayout getCoordinatorraizmapa() { return coordinatorraizmapa; }
    public LinearLayout getLlencabezadomapa() { return llencabezadomapa; }
    public ImageButton getBtnatrasmapa() { return btnatrasmapa; }
    public LinearLayout getLltitulosmapa() { return lltitulosmapa; }
    public TextView getTvtitulomapa() { return tvtitulomapa; }
    public TextView getTvsubtitulomapa() { return tvsubtitulomapa; }
    public MaterialButton getBtnrecentrarmapa() { return btnrecentrarmapa; }
    public View getScrollfichasmapa() { return scrollfichasmapa; }
    public ChipGroup getGrupofichasmapa() { return grupofichasmapa; }
    public Chip getFichatodos() { return fichatodos; }
    public Chip getFichagastos() { return fichagastos; }
    public Chip getFichaingresos() { return fichaingresos; }
    public CardView getTarjetamarcadorseleccionado() { return tarjetamarcadorseleccionado; }
    public LinearLayout getLlcontenedortarjeta() { return llcontenedortarjeta; }
    public LinearLayout getLlfilasuperiormarcador() { return llfilasuperiormarcador; }
    public ImageView getImgiconomarcador() { return imgiconomarcador; }
    public LinearLayout getLldetallesmarcador() { return lldetallesmarcador; }
    public TextView getTvtitulomarcador() { return tvtitulomarcador; }
    public TextView getTvsubtitulomarcador() { return tvsubtitulomarcador; }
    public TextView getTvmontomarcador() { return tvmontomarcador; }
    public View getVdivisormarcador() { return vdivisormarcador; }
    public LinearLayout getLlfilainferiormarcador() { return llfilainferiormarcador; }
    public TextView getTvcoordenadasmarcador() { return tvcoordenadasmarcador; }
    public MaterialButton getBtncompartirmarcador() { return btncompartirmarcador; }
    public FrameLayout getFlcapacargamapa() { return flcapacargamapa; }
    public ProgressBar getPbcargando() { return pbcargando; }
}