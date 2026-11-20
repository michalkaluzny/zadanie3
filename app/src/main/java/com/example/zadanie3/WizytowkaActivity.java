package com.example.zadanie3;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WizytowkaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_wizytowka);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ekranWizytowki), (widok, wciecia) -> {
            Insets paskiSystemowe = wciecia.getInsets(WindowInsetsCompat.Type.systemBars());
            widok.setPadding(paskiSystemowe.left, paskiSystemowe.top, paskiSystemowe.right, paskiSystemowe.bottom);
            return wciecia;
        });
    }
}
