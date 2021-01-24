package com.example.arnedo_perezmedrano_practica_android_m08uf1;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivityV4 extends AppCompatActivity {
    //Views
    private TextView tvPalabra;
    private ImageView imagen;
    private static int[] imagenes = {R.drawable.ahorcado0, R.drawable.ahorcado1, R.drawable.ahorcado2, R.drawable.ahorcado3, R.drawable.ahorcado4, R.drawable.ahorcado5, R.drawable.ahorcado6, R.drawable.ahorcado7, R.drawable.ahorcado8, R.drawable.ahorcado9, R.drawable.ahorcado10,};
    private TextView tvTiempo;

    //Lógica de la aplicación
    private String[] palabras;
    List<String> arrayCompletar = new ArrayList<String>();
    private String palabraSeleccionada;
    private int intentos = 10;
    private boolean ganar = false;
    public Timer temp = new Timer();
    public int contador = 0;
    private String nombreJugador = "";
    private Puntuacion puntuacionJugador;
    public ArrayList<String> contactos = new ArrayList<String>();
    private String mensajePista;

    //Base de datos
    SQLiteDatabase bd;
    private final String BASE_DATOS = "ahorcado";
    private final String TABLA = "puntuaciones";
    ContentValues valors;
    Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = findViewById(R.id.actionbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Botón para volver atrás

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        100);
            }

        }

        GestorBD utilidadBD = new GestorBD(getBaseContext());
        bd = utilidadBD.getWritableDatabase();
        valors = new ContentValues();

        obtenerContactos();
        if (contactos.size() == 0){
            palabras = getResources().getStringArray(R.array.words); //Obtenemos las palabras del XML
            mensajePista = "Es un vegetal.";
        }
        else {
            palabras = contactos.toArray(new String[0]);
            mensajePista = "Es un contacto de tu móvil";
        }

        tvPalabra = (TextView) findViewById(R.id.palabra);

        Random rand = new Random();
        int random = rand.nextInt(palabras.length);
        palabraSeleccionada = palabras[random];
        tvPalabra.setText(hideWord(palabraSeleccionada));

        imagen = (ImageView) findViewById(R.id.imagen);
        imagen.setImageResource(imagenes[10 - intentos]);

        tvTiempo = (TextView) findViewById(R.id.tiempo);
        tvTiempo.setText("Tiempo: "+contador);

        temp.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        tvTiempo.setText("Tiempo: "+contador);
                        contador++;
                    }
                });
            }
        }, 0, 1000);

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
                Toast.makeText(this, mensajePista, Toast.LENGTH_LONG).show();
                return true;
            case R.id.item5:
                intent = new Intent(getApplicationContext(), HowToPlayActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void obtenerContactos() {
        Cursor managedCursor = getContentResolver()
                .query(Phone.CONTENT_URI,
                        new String[] {Phone._ID, Phone.DISPLAY_NAME, Phone.NUMBER}, null, null,  Phone.DISPLAY_NAME + " ASC");
        try {
            while (managedCursor.moveToNext()) {
                int index;

                index = managedCursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
                contactos.add(managedCursor.getString(index).toUpperCase().replace(" ","").replace("À", "A").replace("Á","A").replace("È", "E").replace("É", "E").replace("Í", "I").replace("Ò", "O").replace("Ó", "O").replace("Ú", "U"));
            }
        } finally {
            managedCursor.close();
        }
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
                String stLetraPulsada = String.valueOf(b.getText());
                String p = (String) tvPalabra.getText();
                b.setEnabled(false);

                if (palabraSeleccionada.contains(stLetraPulsada)) {
                    for (int i = 0; i < palabraSeleccionada.length(); i++) {
                        char c = palabraSeleccionada.charAt(i);
                        if (stLetraPulsada.charAt(0) == c) {
                            arrayCompletar.set(i, stLetraPulsada);
                            if (!arrayCompletar.contains("_")) {
                                ganar = true;
                                imagen.setImageResource(R.drawable.youwin);
                                temp.cancel();

                                Date fecha = new Date();
                                String now = (fecha.getYear() + 1900) + "-" + ((fecha.getMonth() + 1) < 10 ? "0" + (fecha.getMonth() + 1) : (fecha.getMonth() + 1)) + "-" + (fecha.getDate() < 10 ? "0" + fecha.getDate() : fecha.getDate());

                                openDialog(now);
                            }

                        }

                        String textoAMostrar = "";

                        for (int x = 0; x < arrayCompletar.size(); x++) {
                            textoAMostrar += " " + arrayCompletar.get(x);
                        }
                        tvPalabra.setText(textoAMostrar);
                    }
                } else {
                    intentos--;
                    contador += 5;
                    imagen.setImageResource(imagenes[10 - intentos]);
                }
            }
        } else {
            imagen.setImageResource(R.drawable.gameover);
            temp.cancel();
        }

    }

    public void openDialog(String now){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nombre del jugador:");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nombreJugador = input.getText().toString();
                puntuacionJugador = new Puntuacion(nombreJugador, contador, now);
                valors.put("nombre", puntuacionJugador.getNombre());
                valors.put("puntuacion", puntuacionJugador.getPuntuacion());
                valors.put("fecha", puntuacionJugador.getFecha());

                id = bd.insert(TABLA, null, valors);

                valors.clear();

                Intent intent = new Intent(getApplicationContext(),
                        RankingActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}