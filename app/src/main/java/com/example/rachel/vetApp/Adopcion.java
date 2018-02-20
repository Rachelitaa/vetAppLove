package com.example.rachel.vetApp;

import android.graphics.Bitmap;

public class Adopcion {
    private int imagenIcono;
    private Bitmap adopcionImagenBitmap;
    private String tipoAnimal;
    private String nombre;
    private String ciudad;
    private String pais;

    public Adopcion(int imagenIcono,Bitmap adopcionImagenBitmap,String tipoAnimal, String nombre, String ciudad, String pais) {
        this.tipoAnimal=tipoAnimal;
        this.imagenIcono = imagenIcono;
        this.adopcionImagenBitmap=adopcionImagenBitmap;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.pais = pais;
    }
    public Adopcion(String tipoAnimal, String nombre, String ciudad, String pais) {
        this.tipoAnimal=tipoAnimal;

        this.nombre = nombre;
        this.ciudad = ciudad;
        this.pais = pais;
    }

    public Adopcion() {}

    public void setTipoAnimal(String tipoAnimal) {
        this.tipoAnimal = tipoAnimal;
    }

    public String getTipoAnimal() {
        return tipoAnimal;
    }

    public int getIdImagen() {
        return imagenIcono;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setImagenIcono(int imagenIcono) {
        this.imagenIcono = imagenIcono;
    }

    public int getImagenIcono() {

        return imagenIcono;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Bitmap getAdopcionImagenBitmap() {
        return adopcionImagenBitmap;
    }

    public void setAdopcionImagenBitmap(Bitmap adopcionImagenBitmap) {
        this.adopcionImagenBitmap = adopcionImagenBitmap;
    }
}
