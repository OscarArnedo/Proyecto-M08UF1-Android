package com.example.arnedo_perezmedrano_practica_android_m08uf1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class GestorBD {
    private ArrayList<String> resultadosBD = new ArrayList<String>();
    private SQLiteDatabase bd = null;
    private final String BASE_DATOS = "ahorcado";
    private final String TABLA = "puntuaciones";
    public static final int MODE_PRIVATE = 0x0000;

    /*public void crearBD(){
        bd = bd.openOrCreateDatabase(BASE_DATOS, MODE_PRIVATE, null);
        bd.execSQL("CREATE TABLE IF NOT EXISTS "
                + TABLA
                + " (nombre VARCHAR, puntuacion INT(3),"
                + " fecha DATE);");
    }*/

    public List<Puntuacion> obtenerPuntuaciones(){
        Cursor c = bd.rawQuery("SELECT *"
                        + " FROM " + TABLA
                        + " ORDER BY puntuacion ASC LIMIT 5;",
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
}
