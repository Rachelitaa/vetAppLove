package com.example.rachel.vetApp;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rachel.vetApp.Adopcion;
import com.example.rachel.vetApp.R;

import java.util.ArrayList;
import java.util.List;

public class AdopcionesAdapter extends ArrayAdapter<Adopcion> {
    Context context;
    int resourceIcon;
    ArrayList<Adopcion>adopciones;

    public AdopcionesAdapter(@NonNull Context context,int resourceIcon,ArrayList<Adopcion>adopciones) {
        super(context,resourceIcon,adopciones);
        this.context=context;
        this.resourceIcon=resourceIcon;
        this.adopciones=adopciones;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Adopcion adopcion=adopciones.get(position);
        if (convertView == null) {
            convertView=LayoutInflater.from(context).inflate(R.layout.entrada_adopciones,parent,false);
        }
        TextView nombre=(TextView)convertView.findViewById(R.id.tvNombre);
        TextView ciudad=(TextView)convertView.findViewById(R.id.tvCiudad);
        TextView pais=(TextView)convertView.findViewById(R.id.tvPais);
        ImageView iconoLocation=(ImageView)convertView.findViewById(R.id.imageViewIcono);
        ImageView imageViewAdopciones=(ImageView)convertView.findViewById(R.id.imageViewAdopciones);

        nombre.setText(adopcion.getNombre());
        ciudad.setText(adopcion.getCiudad());
        pais.setText(adopcion.getPais());
        imageViewAdopciones.setImageBitmap(adopcion.getAdopcionImagenBitmap());
        iconoLocation.setImageResource(adopcion.getIdImagen());

        return convertView;
    }
}




