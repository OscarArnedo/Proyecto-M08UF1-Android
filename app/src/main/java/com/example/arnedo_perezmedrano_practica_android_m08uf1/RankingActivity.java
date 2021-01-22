package com.example.arnedo_perezmedrano_practica_android_m08uf1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import java.util.List;

public class RankingActivity extends AppCompatActivity {

    private WebView navegador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        Toolbar toolbar = findViewById(R.id.actionbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GestorBD utilidadBD = new GestorBD(getBaseContext());
        SQLiteDatabase bd = utilidadBD.getReadableDatabase();
        List<String> resultados = new ArrayList<>();
        ArrayAdapter<String> adapter;

        ListView lv = (ListView) findViewById(R.id.list);
        try{

            Cursor c = bd.query(
                    "puntuaciones",
                    new String[]{"nombre", "puntuacion", "fecha"},
                    null,
                    null,
                    null,
                    null,
                    "puntuacion ASC",
                    "5"
            );

            int columnaNombres = c.getColumnIndexOrThrow("nombre");
            int columnaPuntuaciones = c.getColumnIndexOrThrow("puntuacion");
            int columnaFechas = c.getColumnIndexOrThrow("fecha");

            if (c != null) {
                if (c.isBeforeFirst()) {
                    c.moveToFirst();
                    int i = 0;

                    do {
                        i++;

                        String nombre = c.getString(columnaNombres);
                        int puntuacion = c.getInt(columnaPuntuaciones);
                        String fecha = c.getString(columnaFechas);

                        Puntuacion p = new Puntuacion(nombre, puntuacion, fecha);
                        resultados.add(i+". "+p.toString());

                    } while (c.moveToNext());
                }
            }

            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, resultados);

            lv.setAdapter(adapter);

        } catch(Exception e) {
            List<String> l = new ArrayList<>();
            l.add("Todavía no se ha registrado ninguna puntuación.");
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, l);

        }

        lv.setAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.item1:
                intent = new Intent(getApplicationContext(), MainActivityV2.class);
                startActivity(intent);
                return true;
            case R.id.item2:
                intent = new Intent(getApplicationContext(), GameActivityV4.class);
                startActivity(intent);
                return true;
            case R.id.item3:
                intent = new Intent(getApplicationContext(), RankingActivity.class);
                startActivity(intent);
                return true;
            case R.id.item4:
                Toast.makeText(this, "Inicia una partida para obtener una pista.", Toast.LENGTH_LONG).show();
                return true;
            case R.id.item5:
                intent = new Intent(getApplicationContext(), HowToPlayActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}