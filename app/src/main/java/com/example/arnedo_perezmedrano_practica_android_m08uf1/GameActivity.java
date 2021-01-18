package com.example.arnedo_perezmedrano_practica_android_m08uf1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private static String[] palabras = {"MANZANA","PIMIENTO", "COLIFLOR", "BERENJENA", "PATATA", "CALABAZA", "ACELGAS", "TOMATE"};
    private TextView palabra;
    private static int[] imagenes = {R.drawable.ahorcado0,R.drawable.ahorcado1,R.drawable.ahorcado2,R.drawable.ahorcado3,R.drawable.ahorcado4,R.drawable.ahorcado5,R.drawable.ahorcado6,R.drawable.ahorcado7,R.drawable.ahorcado8,R.drawable.ahorcado9,R.drawable.ahorcado10, };
    private ImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        palabra = (TextView) findViewById(R.id.palabra);
        Random rand = new Random();
        int random = rand.nextInt(palabras.length);
        palabra.setText(hideWord(palabras[random]));

        imagen = (ImageView) findViewById(R.id.imagen);
    }

    private String hideWord(String word) {
        String guiones = "";
        for (int i = 0; i < word.length(); i++) {
            guiones += "_ ";
        }

        return guiones;
    }

    public void letraPulsada(View v) {
        Button b = (Button) v;
        palabra.setText(b.getText());

    }
}