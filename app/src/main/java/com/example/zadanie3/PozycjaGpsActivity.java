package com.example.zadanie3;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PozycjaGpsActivity extends AppCompatActivity {

    private static final int KOD_PROSBY_O_LOKALIZACJE = 3107;
    private static final long ODSTEP_ODCZYTU_MS = 0L;
    private static final float MINIMALNY_DYSTANS_M = 0f;

    private Button przyciskStartGps, przyciskStopGps;
    private TextView wartoscSzerokosci, wartoscDlugosci, napisStatusGps;
    private LocationManager menedzerPolozenia;
    private LocationListener sluchaczPolozenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pozycja_gps);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ekranPozycjiGps), (widok, wciecia) -> {
            Insets paskiSystemowe = wciecia.getInsets(WindowInsetsCompat.Type.systemBars());
            widok.setPadding(paskiSystemowe.left, paskiSystemowe.top, paskiSystemowe.right, paskiSystemowe.bottom);
            return wciecia;
        });

        przyciskStartGps = findViewById(R.id.przyciskStartGps);
        przyciskStopGps = findViewById(R.id.przyciskStopGps);
        wartoscSzerokosci = findViewById(R.id.wartoscSzerokosci);
        wartoscDlugosci = findViewById(R.id.wartoscDlugosci);
        napisStatusGps = findViewById(R.id.napisStatusGps);

        menedzerPolozenia = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        sluchaczPolozenia = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location nowePolozenie) {
                wyswietlPolozenie(nowePolozenie);
                napisStatusGps.setText(R.string.status_odebrano);
            }

            @Override
            @SuppressWarnings("deprecation")
            public void onStatusChanged(String dostawca, int stan, Bundle dodatki) {

            }

            @Override
            public void onProviderEnabled(@NonNull String dostawca) {
                napisStatusGps.setText(R.string.status_szukam);
            }

            @Override
            public void onProviderDisabled(@NonNull String dostawca) {
                napisStatusGps.setText(R.string.status_gps_wylaczony);
            }
        };


        przyciskStartGps.setEnabled(true);
        przyciskStopGps.setEnabled(false);

        przyciskStartGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View klikniety) {
                wlaczNasluchGps();
            }
        });

        przyciskStopGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View klikniety) {
                wylaczNasluchGps();
            }
        });
    }

    private void wlaczNasluchGps() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    KOD_PROSBY_O_LOKALIZACJE);
            return;
        }

        if (menedzerPolozenia.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            napisStatusGps.setText(R.string.status_szukam);
        } else {
            napisStatusGps.setText(R.string.status_gps_wylaczony);
        }

        // Ostatnia znana pozycja – pokazujemy od razu, zanim przyjdzie świeży odczyt
        Location ostatniePolozenie = menedzerPolozenia.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (ostatniePolozenie != null) {
            wyswietlPolozenie(ostatniePolozenie);
        }

        menedzerPolozenia.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                ODSTEP_ODCZYTU_MS,
                MINIMALNY_DYSTANS_M,
                sluchaczPolozenia);

        przyciskStartGps.setEnabled(false);
        przyciskStopGps.setEnabled(true);
    }

    private void wylaczNasluchGps() {
        menedzerPolozenia.removeUpdates(sluchaczPolozenia);
        napisStatusGps.setText(R.string.status_zatrzymano);

        przyciskStartGps.setEnabled(true);
        przyciskStopGps.setEnabled(false);
    }

    private void wyswietlPolozenie(Location polozenie) {
        String szerokoscGeo = String.valueOf(polozenie.getLatitude());
        String dlugoscGeo = String.valueOf(polozenie.getLongitude());
        wartoscSzerokosci.setText(szerokoscGeo);
        wartoscDlugosci.setText(dlugoscGeo);
    }

    @Override
    public void onRequestPermissionsResult(int kodProsby, @NonNull String[] uprawnienia, @NonNull int[] wyniki) {
        super.onRequestPermissionsResult(kodProsby, uprawnienia, wyniki);
        if (kodProsby != KOD_PROSBY_O_LOKALIZACJE) {
            return;
        }
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            wlaczNasluchGps();
        } else {
            napisStatusGps.setText(R.string.status_brak_zgody);
        }
    }

    @Override
    protected void onDestroy() {

        menedzerPolozenia.removeUpdates(sluchaczPolozenia);
        super.onDestroy();
    }
}
