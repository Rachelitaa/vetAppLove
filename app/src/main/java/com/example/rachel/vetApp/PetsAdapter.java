package com.example.rachel.vetApp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lenovo on 2/21/2018.
 */

public class PetsAdapter extends ArrayAdapter<AddPet>
{

    public PetsAdapter(@NonNull Context context, int resource, @NonNull List<AddPet> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       AddPet pets = getItem(position);
       if(convertView==null)
       {
           LayoutInflater inflater = LayoutInflater.from(getContext());
           convertView = inflater.inflate(R.layout.petlist, parent, false);

       }

      TextView petName = convertView.findViewById(R.id.petlistName);
       TextView petSpecie = convertView.findViewById(R.id.petSpecie);
        TextView petBreed = convertView.findViewById(R.id.petBreed);
        TextView petBdate = convertView.findViewById(R.id.petBdate);
        TextView petGender = convertView.findViewById(R.id.petGender);
       ImageView image = convertView.findViewById(R.id.petlistImg);

       petName.setText(pets.getNameAddPet());
       petSpecie.setText(pets.getSpecies());
       petBdate.setText(pets.getBdateAddPet());
       petBreed.setText(pets.getBreed());
       petGender.setText(pets.getGenderAddPet());
       image.setImageBitmap(pets.getPetlistImg());

       return convertView;
    }
}
