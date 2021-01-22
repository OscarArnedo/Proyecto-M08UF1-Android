package com.example.arnedo_perezmedrano_practica_android_m08uf1;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivityV2 extends Activity {

    private Button jugar;
    private Button puntuacion;
    private Button salir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_v2);

        jugar = (Button) findViewById(R.id.jugar);
        jugar.setOnClickListener(this::jugar);
        puntuacion = (Button) findViewById(R.id.ranking);
        puntuacion.setOnClickListener(this::puntuaciones);
        salir = (Button) findViewById(R.id.salir);
        salir.setOnClickListener(this::salir);
    }



    private void jugar(View view) {
        Intent intent = new Intent(getApplicationContext(),
                GameActivityV4.class);
        startActivity(intent);
    }

    private void puntuaciones(View view) {
        Intent intent = new Intent(getApplicationContext(),
                RankingActivity.class);
        startActivity(intent);
    }

    private void salir(View view) {
        finish();
        System.exit(0);
    }

}