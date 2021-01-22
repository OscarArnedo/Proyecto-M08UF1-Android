package com.example.arnedo_perezmedrano_practica_android_m08uf1;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

        //palabras = getResources().getStringArray(R.array.words); //Obtenemos las palabras del XML
        obtenerContactos();
        palabras = contactos.toArray(new String[0]);
        tvPalabra = (TextView) findViewById(R.id.palabra);

        Random rand = new Random();
        int random = rand.nextInt(palabras.length);
        palabraSeleccionada = palabras[random];
        tvPalabra.setText(hideWord(palabraSeleccionada));

        imagen = (ImageView) findViewById(R.id.imagen);
        imagen.setImageResource(imagenes[10 - intentos]);

        tvTiempo = (TextView) findViewById(R.id.tiempo);
        tvTiempo.setText("Tiempo: "+contador);

        Log.i("TAG", "contador: " + contador);
        temp.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        tvTiempo.setText("Tiempo: "+contador);
                        Log.i("TAG:", "contador: " + contador);
                        contador++;
                    }
                });
            }
        }, 0, 1000);

    }

    public void obtenerContactos() {
        Cursor managedCursor = getContentResolver()
                .query(Phone.CONTENT_URI,
                        new String[] {Phone._ID, Phone.DISPLAY_NAME, Phone.NUMBER}, null, null,  Phone.DISPLAY_NAME + " ASC");
        try {
            while (managedCursor.moveToNext()) {
                int index;

                index = managedCursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
                contactos.add(managedCursor.getString(index).toUpperCase().replace(" ","").replace("À", "A").replace("Á","A").replace("È", "E").replace("É", "E").replace("Ó", "O").replace("Í", "I").replace("Ò", "O").replace("Ú", "U"));

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
                                temp.cancel();

                                Date fecha = new Date();
                                String now = (fecha.getYear() + 1900) + "-" + ((fecha.getMonth() + 1) < 10 ? "0" + (fecha.getMonth() + 1) : (fecha.getMonth() + 1)) + "-" + (fecha.getDate() < 10 ? "0" + fecha.getDate() : fecha.getDate());

                                openDialog(now);

                                /*Log.i("TAG", "nombre: " + nombreJugador);
                                puntuacionJugador = new Puntuacion(nombreJugador, contador, now);
                                valors.put("nombre", puntuacionJugador.getNombre());
                                valors.put("puntuacion", puntuacionJugador.getPuntuacion());
                                valors.put("fecha", puntuacionJugador.getFecha());

                                id = bd.insert(TABLA, null, valors);

                                valors.clear();*/
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


    /*
    public void crearBD() {
        bd = this.openOrCreateDatabase(BASE_DATOS, MODE_PRIVATE, null);
        bd.execSQL("CREATE TABLE IF NOT EXISTS "
                + TABLA
                + " (nombre VARCHAR, puntuacion INT(3),"
                + " fecha DATE);");
    }

    public List<Puntuacion> obtenerPuntuaciones(){
        Cursor c = bd.rawQuery("SELECT *"
                        + " FROM " + TABLA
                        + " ORDER BY puntuacion DSC LIMIT 5;",
                null);

        int columnaNombres = c.getColumnIndex("nombres");
        int columnaPuntuaciones = c.getColumnIndex("puntuacion");
        int columnaFechas = c.getColumnIndex("fecha");

        List<Puntuacion> puntuaciones = new ArrayList<Puntuacion>();

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
                    puntuaciones.add(p);

                } while (c.moveToNext());
            }
        }

        return puntuaciones;
    }

    public void insertarPuntuacion(String nombre, int puntuacion) {
        Date fecha = new Date();
        String now = fecha.getYear() + "-" + ((fecha.getMonth() + 1) < 10 ? "0" + (fecha.getMonth() + 1) : (fecha.getMonth() + 1)) + (fecha.getDate() < 10 ? "0" + fecha.getDate() : fecha.getDate());

        bd.execSQL("INSERT INTO "
                + TABLA
                + " (nombre, puntuacion, fecha)"
                + " VALUES ('" + nombre + "', " + puntuacion + ", '" + now + "');"
        );

    }

     */

}