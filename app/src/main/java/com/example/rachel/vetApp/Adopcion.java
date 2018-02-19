package com.example.rachel.vetApp;

public class Adopcion {
    private int idImagen;
    private String tipoAnimal;
    private String nombre;
    private String ciudad;
    private String pais;

    public Adopcion(int idImagen,String tipoAnimal, String nombre, String ciudad, String pais) {
        this.tipoAnimal=tipoAnimal;
        this.idImagen = idImagen;
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
        return idImagen;
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

    public void setIdImagen(int idImagen) {
        this.idImagen = idImagen;
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
}
