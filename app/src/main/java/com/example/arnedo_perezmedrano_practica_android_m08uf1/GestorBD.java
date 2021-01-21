package com.example.arnedo_perezmedrano_practica_android_m08uf1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class GestorBD extends SQLiteOpenHelper {

    private static final String BASE_DATOS = "ahorcado";
    private static final String TABLA = "puntuaciones";
    public static final int DATABASE_VERSION = 1;

    private static final String SQL_CREACIO_TAULA = "CREATE TABLE IF NOT EXISTS "
            + TABLA
            + " (nombre VARCHAR, puntuacion INT(3),"
            + " fecha DATE);";

    private static final String SQL_ESBORRAT_TAULA = "DROP TABLE IF EXISTS "
            + TABLA;

    public GestorBD(Context context) {
        super(context, BASE_DATOS, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREACIO_TAULA);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_ESBORRAT_TAULA);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /*
    public void crearBD(){
        try{
            this.bd = SQLiteDatabase.openOrCreateDatabase(BASE_DATOS, , null);
            this.bd.execSQL("CREATE TABLE IF NOT EXISTS "
                    + TABLA
                    + " (nombre VARCHAR, puntuacion INT(3),"
                    + " fecha DATE);");
        } finally {
            if (this.bd != null) {
                this.bd.close();
            }
        }

    }

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
    */
}
