package com.example.arnedo_perezmedrano_practica_android_m08uf1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivityV3 extends AppCompatActivity {
    //Views
    private TextView tvPalabra;
    private ImageView imagen;
    private static int[] imagenes = {R.drawable.ahorcado0, R.drawable.ahorcado1, R.drawable.ahorcado2, R.drawable.ahorcado3, R.drawable.ahorcado4, R.drawable.ahorcado5, R.drawable.ahorcado6, R.drawable.ahorcado7, R.drawable.ahorcado8, R.drawable.ahorcado9, R.drawable.ahorcado10,};

    //Lógica de la aplicación
    private String[] palabras;
    List<String> arrayCompletar = new ArrayList<String>();
    private String palabraSeleccionada;
    private int intentos = 10;
    private boolean ganar = false;

    //Base de datos
    private ArrayList<String> resultadosBD = new ArrayList<String>();
    SQLiteDatabase bd = null;
    private final String BASE_DATOS = "ahorcado";
    private final String TABLA = "puntuaciones";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Botón para volver atrás

        palabras = getResources().getStringArray(R.array.words); //Obtenemos las palabras del XML

        tvPalabra = (TextView) findViewById(R.id.palabra);
        Random rand = new Random();
        int random = rand.nextInt(palabras.length);
        palabraSeleccionada = palabras[random];
        tvPalabra.setText(hideWord(palabraSeleccionada));

        imagen = (ImageView) findViewById(R.id.imagen);
        imagen.setImageResource(imagenes[10 - intentos]);

        this.crearBD();
    }

    private String hideWord(String word) {
        String guiones = "";
        for (int i = 0; i < word.length(); i++) {
            guiones += "_ ";
            arrayCompletar.add("_");
        }

        return guiones;
    }

    public void letraPulsada(View v) {

        if (intentos > 0) {
            if (!ganar) {
                Button b = (Button) v;
                //palabra.setText(b.getText());
                String stLetraPulsada = String.valueOf(b.getText());
                String p = (String) tvPalabra.getText();
                b.setEnabled(false);

                if (palabraSeleccionada.contains(stLetraPulsada)) {
                    for (int i = 0; i < palabraSeleccionada.length(); i++) {
                        char c = palabraSeleccionada.charAt(i);
                        if (stLetraPulsada.charAt(0) == c) {
                            //cambiarLetra(stLetraPulsada, i);
                            arrayCompletar.set(i, stLetraPulsada);
                            if (!arrayCompletar.contains("_")) {
                                ganar = true;
                                imagen.setImageResource(R.drawable.youwin);
                            }
                        }

                        String textoAMostrar = "";
                        String palabraComprobante = "";

                        for (int x = 0; x < arrayCompletar.size(); x++) {
                            textoAMostrar += " " + arrayCompletar.get(x);
                            palabraComprobante += arrayCompletar.get(x);
                        }
                        tvPalabra.setText(textoAMostrar);
                    }
                } else {
                    intentos--;
                    imagen.setImageResource(imagenes[10 - intentos]);
                }
            }
        } else {
            imagen.setImageResource(R.drawable.gameover);
        }

    }

    public void crearBD(){
        bd = this.openOrCreateDatabase(BASE_DATOS, MODE_PRIVATE, null);
        bd.execSQL("CREATE TABLE IF NOT EXISTS "
                + TABLA
                + " (nombre VARCHAR, puntuacion INT(3),"
                + " fecha DATE);");
    }

    public void obtenerPuntuaciones(){
        Cursor c = bd.rawQuery("SELECT *"
                        + " FROM " + TABLA
                        + " ORDER BY puntuacion ASC LIMIT 5;",
                null);

        int ColumnaNombres = c.getColumnIndex("nombres");
        int ColumnaPuntuacion = c.getColumnIndex("puntuacion");
        int ColumnaFecha = c.getColumnIndex("fecha");

    }

}