package com.example.rachel.vetApp;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by lenovo on 2/21/2018.
 */

public class AddPet implements Serializable
{
    private Bitmap petlistImg;
    private String nameAddPet;
    private String species;
    private String breed;
    private String bdateAddPet;
    private String genderAddPet;

    public AddPet(Bitmap petlistImg, String nameAddPet, String species)
    {
        this.petlistImg = petlistImg;
        this.nameAddPet = nameAddPet;
        this.species = species;
    }

    public AddPet(Bitmap petlistImg, String nameAddPet, String species, String breed, String bdateAddPet, String genderAddPet) {
        this.petlistImg = petlistImg;
        this.nameAddPet = nameAddPet;
        this.species = species;
        this.breed = breed;
        this.bdateAddPet = bdateAddPet;
        this.genderAddPet = genderAddPet;
    }

    public AddPet(String nameAddPet, String species, String breed, String bdateAddPet, String genderAddPet) {
        this.nameAddPet = nameAddPet;
        this.species = species;
        this.breed = breed;
        this.bdateAddPet = bdateAddPet;
        this.genderAddPet = genderAddPet;
    }

    public Bitmap getPetlistImg() {
        return petlistImg;
    }

    public void setPetlistImg(Bitmap petlistImg) {
        this.petlistImg = petlistImg;
    }

    public String getNameAddPet() {
        return nameAddPet;
    }

    public void setNameAddPet(String nameAddPet) {
        this.nameAddPet = nameAddPet;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getBdateAddPet() {
        return bdateAddPet;
    }

    public void setBdateAddPet(String bdateAddPet) {
        this.bdateAddPet = bdateAddPet;
    }

    public String getGenderAddPet() {
        return genderAddPet;
    }

    public void setGenderAddPet(String genderAddPet) {
        this.genderAddPet = genderAddPet;
    }
}
