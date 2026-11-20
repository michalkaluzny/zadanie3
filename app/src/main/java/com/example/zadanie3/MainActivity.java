package com.example.zadanie3;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button przyciskAutor, przyciskStronaWww, przyciskPolozenie;
    private EditText poleAdresuWww;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ekranGlowny), (widok, wciecia) -> {
            Insets paskiSystemowe = wciecia.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            widok.setPadding(paskiSystemowe.left, paskiSystemowe.top, paskiSystemowe.right, paskiSystemowe.bottom);
            return wciecia;
        });

        przyciskAutor = findViewById(R.id.przyciskAutor);
        przyciskStronaWww = findViewById(R.id.przyciskStronaWww);
        przyciskPolozenie = findViewById(R.id.przyciskPolozenie);
        poleAdresuWww = findViewById(R.id.poleAdresuWww);

        przyciskAutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View klikniety) {
                Intent intencjaWizytowki = new Intent(MainActivity.this, WizytowkaActivity.class);
                startActivity(intencjaWizytowki);
            }
        });

        przyciskStronaWww.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View klikniety) {
                otworzWpisanyAdres();
            }
        });

        przyciskPolozenie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View klikniety) {
                Intent intencjaGps = new Intent(MainActivity.this, PozycjaGpsActivity.class);
                startActivity(intencjaGps);
            }
        });
    }

    private void otworzWpisanyAdres() {
        String wpisanyAdres = poleAdresuWww.getText().toString().trim();

        if (wpisanyAdres.isEmpty() || wpisanyAdres.equals("http://") || wpisanyAdres.equals("https://")) {
            Toast.makeText(this, R.string.komunikat_pusty_adres, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!wpisanyAdres.startsWith("http://") && !wpisanyAdres.startsWith("https://")) {
            wpisanyAdres = "https://" + wpisanyAdres;
        }

        Intent intencjaPrzegladarki = new Intent(Intent.ACTION_VIEW, Uri.parse(wpisanyAdres));
        try {
            startActivity(intencjaPrzegladarki);
        } catch (ActivityNotFoundException brakAplikacji) {
            Toast.makeText(this, R.string.komunikat_brak_przegladarki, Toast.LENGTH_SHORT).show();
        }
    }
}
