package com.example.arnedo_perezmedrano_practica_android_m08uf1;

import java.util.Date;

public class Puntuacion {
    private String nombre;
    private int puntuacion;
    private Date fecha;

    public Puntuacion(String nombre, int puntuacion, Date fecha){
        this.nombre = nombre;
        this.puntuacion = puntuacion;
        this.fecha = fecha;
    }

    public String getNombre(){
        return this.nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public int getPuntuacion(){
        return this.puntuacion;
    }

    public void setPuntuacion(int puntuacion){
        this.puntuacion = puntuacion;
    }

    public Date getFecha(){
        return this.fecha;
    }

    public void setFecha(Date fecha){
        this.fecha = fecha;
    }
}
