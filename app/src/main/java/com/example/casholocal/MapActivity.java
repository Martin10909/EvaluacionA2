package com.example.casholocal;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorraizmapa;
    private MapView vistemapa;
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

    private DatabaseHelper dbHelper;
    private List<MovimientoModel> listaMovimientos = new ArrayList<>();
    private GeoPoint puntoInicial = new GeoPoint(-33.4372, -70.6506);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().setUserAgentValue(getPackageName());
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        setContentView(R.layout.activity_map);

        dbHelper = new DatabaseHelper(this);

        initViews();
        configurarMapa();
        configurarListeners();
        cargarDatos();
    }

    private void initViews() {
        coordinatorraizmapa = findViewById(R.id.coordinatorraizmapa);
        vistemapa = findViewById(R.id.vistemapa);
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
        if (vistemapa != null) {
            OnlineTileSourceBase fuenteMapaLibre = new XYTileSource(
                    "CartoDB_Positron",
                    0, 19, 256, ".png",
                    new String[] {
                            "https://a.basemaps.cartocdn.com/light_all/",
                            "https://b.basemaps.cartocdn.com/light_all/",
                            "https://c.basemaps.cartocdn.com/light_all/"
                    },
                    "OpenStreetMap contributors, CARTO"
            );

            vistemapa.setTileSource(fuenteMapaLibre);
            vistemapa.setMultiTouchControls(true);
            vistemapa.getController().setZoom(15.0);
            vistemapa.getController().setCenter(puntoInicial);
        }
    }

    private void cargarDatos() {
        if (dbHelper != null) {
            listaMovimientos = dbHelper.obtenerMovimientos("");
            mostrarMarcadores("todos");
        }
    }

    private void mostrarMarcadores(String filtro) {
        if (vistemapa == null) return;

        vistemapa.getOverlays().clear();

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

            GeoPoint pos = new GeoPoint(lat, lng);

            Marker marcador = new Marker(vistemapa);
            marcador.setPosition(pos);
            marcador.setTitle(mov.getDescripcion());
            marcador.setSnippet(FormatoMoneda.aPesoChileno(mov.getMonto()));

            final MovimientoModel movActual = mov;
            marcador.setOnMarkerClickListener((marker, mapView) -> {
                mostrarDetalleMarcador(movActual);
                return true;
            });

            vistemapa.getOverlays().add(marcador);

            if (!primerMarcadorFijado) {
                mostrarDetalleMarcador(mov);
                puntoInicial = pos;
                primerMarcadorFijado = true;
            }
        }

        vistemapa.invalidate();
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
                if (vistemapa != null) {
                    vistemapa.getController().animateTo(puntoInicial);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (vistemapa != null) {
            vistemapa.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (vistemapa != null) {
            vistemapa.onPause();
        }
    }

    // Getters
    public CoordinatorLayout getCoordinatorraizmapa() { return coordinatorraizmapa; }
    public MapView getVistemapa() { return vistemapa; }
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