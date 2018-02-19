package com.example.rachel.vetApp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class PerfilMascotaActivityFragment extends Fragment {

    private ArrayAdapter<String> adapter;
    private ArrayList<String> items;

    public PerfilMascotaActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil_mascota, container, false);

        ListView mascotaList = view.findViewById(R.id.petList);

        String[] data = {
                "Rachelita"
        };

        items = new ArrayList<>(Arrays.asList(data));

        adapter = new ArrayAdapter<String>(
                getContext(),
                R.layout.petlist,
                R.id.pets,
                items
        );


        mascotaList.setAdapter(adapter);

        ImageButton addPet = view.findViewById(R.id.addPet);
        addPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), addPetActivity.class);
                startActivity(intent);

            }
        });

        return view;
    }
}