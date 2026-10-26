package com.example.casholocal;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

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
    private LatLng ubicacionUsuario = null;
    private Marker marcadorTemporal = null;
    private boolean puntoEspecificoDesdeIntent = false;

    private FusedLocationProviderClient fusedLocationClient;
    private ActivityResultLauncher<String[]> locationPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        dbHelper = new DatabaseHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (getIntent() != null && getIntent().hasExtra("lat") && getIntent().hasExtra("lng")) {
            double latIntent = getIntent().getDoubleExtra("lat", -33.4372);
            double lngIntent = getIntent().getDoubleExtra("lng", -70.6506);
            puntoInicial = new LatLng(latIntent, lngIntent);
            puntoEspecificoDesdeIntent = true;
        }

        locationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Boolean fineGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean coarseGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);
                    if ((fineGranted != null && fineGranted) || (coarseGranted != null && coarseGranted)) {
                        habilitarUbicacionEnMapa();
                        obtenerUbicacionActualUsuario(!puntoEspecificoDesdeIntent);
                    } else {
                        Toast.makeText(this, "Permiso de ubicación no concedido", Toast.LENGTH_SHORT).show();
                    }
                }
        );

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
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(puntoInicial, puntoEspecificoDesdeIntent ? 16f : 14f));
        } catch (Exception ignored) {}

        configurarEventosMapa();
        verificarPermisosYUbicacion();
        cargarDatos();
    }

    private void configurarEventosMapa() {
        if (googleMap == null) return;

        // Creación de un nuevo punto al hacer clic sobre el mapa
        googleMap.setOnMapClickListener(this::manejarClicEnMapa);

        // También soportar pulsación prolongada
        googleMap.setOnMapLongClickListener(this::manejarClicEnMapa);

        // Clic en marcadores existentes
        googleMap.setOnMarkerClickListener(marker -> {
            Object tag = marker.getTag();
            if (tag instanceof MovimientoModel) {
                mostrarDetalleMarcador((MovimientoModel) tag);
            }
            return false;
        });
    }

    private void manejarClicEnMapa(LatLng latLng) {
        if (latLng == null) return;

        if (marcadorTemporal != null) {
            marcadorTemporal.remove();
        }

        marcadorTemporal = googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Punto Seleccionado")
                .snippet("Toca para registrar gasto/ingreso")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        if (marcadorTemporal != null) {
            marcadorTemporal.showInfoWindow();
        }

        mostrarDialogoConfirmarCreacionPunto(latLng);
    }

    private void mostrarDialogoConfirmarCreacionPunto(LatLng latLng) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Nuevo Punto en el Mapa")
                .setMessage(String.format(Locale.getDefault(), "¿Deseas registrar un nuevo gasto o ingreso en esta ubicación?\n\nLat: %.4f, Lng: %.4f", latLng.latitude, latLng.longitude))
                .setPositiveButton("Registrar", (dialog, which) -> {
                    abrirDialogoNuevoMovimiento(latLng.latitude, latLng.longitude);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    if (marcadorTemporal != null) {
                        marcadorTemporal.remove();
                        marcadorTemporal = null;
                    }
                })
                .show();
    }

    private void abrirDialogoNuevoMovimiento(double lat, double lng) {
        AddTransactionDialogFragment dialog = AddTransactionDialogFragment.newInstance(lat, lng);
        dialog.setOnTransactionSavedListener(() -> {
            if (marcadorTemporal != null) {
                marcadorTemporal.remove();
                marcadorTemporal = null;
            }
            cargarDatos();
            if (googleMap != null) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15f));
            }
        });
        dialog.show(getSupportFragmentManager(), "AddTransactionDialogFragment");
    }

    private void verificarPermisosYUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            habilitarUbicacionEnMapa();
            obtenerUbicacionActualUsuario(!puntoEspecificoDesdeIntent);
        } else {
            locationPermissionLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    private void habilitarUbicacionEnMapa() {
        if (googleMap == null) return;
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
        } catch (SecurityException ignored) {}
    }

    private void obtenerUbicacionActualUsuario(boolean animarCamara) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        }

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        ubicacionUsuario = new LatLng(location.getLatitude(), location.getLongitude());
                        if (animarCamara && googleMap != null) {
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacionUsuario, 15f));
                        }
                    } else {
                        fusedLocationClient.getLastLocation().addOnSuccessListener(lastLoc -> {
                            if (lastLoc != null) {
                                ubicacionUsuario = new LatLng(lastLoc.getLatitude(), lastLoc.getLongitude());
                                if (animarCamara && googleMap != null) {
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacionUsuario, 15f));
                                }
                            }
                        });
                    }
                });
    }

    public void cargarDatos() {
        if (dbHelper != null) {
            listaMovimientos = dbHelper.obtenerMovimientos("");
            mostrarMarcadores("todos");
        }
    }

    private BitmapDescriptor crearIconoMarcadorPersonalizado(boolean esIngreso) {
        int ancho = 96;
        int alto = 110;
        Bitmap bitmap = Bitmap.createBitmap(ancho, alto, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int colorFondo = esIngreso ? 0xFF2E7D32 : 0xFFC62828;
        paint.setColor(colorFondo);

        // Triángulo inferior (aguja del pin)
        Path path = new Path();
        path.moveTo(ancho / 2f - 18, 70);
        path.lineTo(ancho / 2f + 18, 70);
        path.lineTo(ancho / 2f, alto);
        path.close();
        canvas.drawPath(path, paint);

        // Círculo principal del pin
        canvas.drawCircle(ancho / 2f, 44, 40, paint);

        // Borde blanco decorativo
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xFFFFFFFF);
        paint.setStrokeWidth(4);
        canvas.drawCircle(ancho / 2f, 44, 38, paint);

        // Dibujar el icono vectorial blanco
        Drawable drawable = ContextCompat.getDrawable(this, esIngreso ? R.drawable.ic_income : R.drawable.ic_expense);
        if (drawable != null) {
            drawable.mutate();
            drawable.setTint(0xFFFFFFFF);
            int iconSize = 46;
            int left = (ancho - iconSize) / 2;
            int top = (88 - iconSize) / 2;
            drawable.setBounds(left, top, left + iconSize, top + iconSize);
            drawable.draw(canvas);
        }

        return BitmapDescriptorFactory.fromBitmap(bitmap);
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

            Double latObj = mov.getLatitud();
            Double lngObj = mov.getLongitud();

            double lat = (latObj != null && latObj != 0) ? latObj : -33.4372;
            double lng = (lngObj != null && lngObj != 0) ? lngObj : -70.6506;

            LatLng pos = new LatLng(lat, lng);
            boolean esIngreso = "ingreso".equalsIgnoreCase(mov.getTipo());

            MarkerOptions options = new MarkerOptions()
                    .position(pos)
                    .title(mov.getDescripcion())
                    .snippet(FormatoMoneda.aPesoChileno(mov.getMonto()))
                    .icon(crearIconoMarcadorPersonalizado(esIngreso))
                    .anchor(0.5f, 1.0f);

            Marker marker = googleMap.addMarker(options);
            if (marker != null) {
                marker.setTag(mov);
            }

            if (!primerMarcadorFijado && !puntoEspecificoDesdeIntent && ubicacionUsuario == null) {
                mostrarDetalleMarcador(mov);
                puntoInicial = pos;
                primerMarcadorFijado = true;
            }
        }
    }

    private void mostrarDetalleMarcador(MovimientoModel mov) {
        if (mov == null) return;

        boolean esIngreso = "ingreso".equalsIgnoreCase(mov.getTipo());

        if (imgiconomarcador != null) {
            imgiconomarcador.setImageResource(esIngreso ? R.drawable.ic_income : R.drawable.ic_expense);
            imgiconomarcador.setColorFilter(esIngreso ? 0xFF2E7D32 : 0xFFC62828);
        }

        if (tvtitulomarcador != null) {
            tvtitulomarcador.setText(mov.getDescripcion());
        }
        if (tvsubtitulomarcador != null) {
            tvsubtitulomarcador.setText(mov.getCategoria() + " - " + mov.getFecha());
        }
        if (tvmontomarcador != null) {
            tvmontomarcador.setText(FormatoMoneda.aPesoChileno(mov.getMonto()));
            tvmontomarcador.setTextColor(esIngreso ? 0xFF2E7D32 : 0xFFC62828);
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
                obtenerUbicacionActualUsuario(true);
                if (googleMap != null) {
                    LatLng destino = ubicacionUsuario != null ? ubicacionUsuario : puntoInicial;
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destino, 15f));
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
