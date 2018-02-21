package com.example.rachel.vetApp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class addPetActivityFragment extends Fragment {

    EditText etName, etSpecies, etBreed, etGender, etBirthdate;
    Button btnSavePet;

    Bitmap bitmap=null;

    public addPetActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_add_pet, container, false);

        etName = view.findViewById(R.id.nameAddPet);
        etSpecies = view.findViewById(R.id.species);
        etBreed = view.findViewById(R.id.breed);
        etGender = view.findViewById(R.id.genderAddPet);
        etBirthdate = view.findViewById(R.id.bdateAddPet);

        btnSavePet = view.findViewById(R.id.imageAddPet);

        btnSavePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameAddPet =etName.getText().toString().toLowerCase();
                String species = etSpecies.getText().toString().toLowerCase();
                String breed = etBreed.getText().toString().toLowerCase();
                String bdateAddPet = etBirthdate.getText().toString();
                String genderAddPet = etGender.getText().toString().toLowerCase();


                if(!nameAddPet.equals("") && !species.equals("") && !breed.equals("") && !bdateAddPet.equals("") && !genderAddPet.equals(""))
                {
                    TareaObtener tarea = new TareaObtener();
                    tarea.execute("http://vetapplove.xyz/addPet.php", nameAddPet, species, breed, bdateAddPet, genderAddPet);
                }
                else{
                    CharSequence text = "Fill all the fields please!!";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(getContext(), text, duration);
                    int offsetX = 50;
                    int offsetY = 25;
                    toast.setGravity(Gravity.CENTER| Gravity.CENTER, offsetX, offsetY);
                    toast.show();
                    Intent i = new Intent(getContext(),addPetActivity.class);
                }
                //mostramos toast
                CharSequence text = "Mascota añadida correctamente. Gracias";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(getContext(), text, duration);
                int offsetX = 50;
                int offsetY = 25;
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, offsetX, offsetY);
                toast.show();
                Intent i = new Intent(getContext(),addPetActivity.class);

            }
        });

        return view;
    }


    public class TareaObtener extends AsyncTask<String,Void,Void>
    {
        protected Void doInBackground(String... params) {
            ConexionHTTP(params[0], params[1], params[2], params[3],params[4],params[5]);

            return null;
        }

        private void ConexionHTTP(String urll, String nameAddPet, String species, String breed, String bdateAddPet, String genderAddPet ) {
            URL url = null;
            HttpURLConnection con = null;
            String response = "";
            try {
                url = new URL(urll);


                con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                con.setReadTimeout(10000);
                con.setConnectTimeout(15000);
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("nameAddPet",nameAddPet) //estructura para enviar el POST
                        .appendQueryParameter("species",species)
                        .appendQueryParameter("breed",breed)
                        .appendQueryParameter("bdateAddPet",bdateAddPet)
                        .appendQueryParameter("genderAddPet",genderAddPet);



                String query = builder.build().getEncodedQuery();

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
                //OutputStream out = new BufferedOutputStream(con.getOutputStream());
                writer.write(query);
                writer.flush();
                writer.close();

                //writer.write(getPostDataString(postDataParams));

                int responseCode = con.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                } else {
                    response = "";
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (con != null)
                    con.disconnect();
            }


        }
    }
}
