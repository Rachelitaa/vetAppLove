package com.example.rachel.vetApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PerfilMascotaActivityFragment extends Fragment {

    ListView petList;



    public PerfilMascotaActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil_mascota, container, false);


        FloatingActionButton addPet = view.findViewById(R.id.afegirPet);
        addPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), addPetActivity.class);
                startActivity(intent);

            }
        });

        String username = getActivity().getIntent().getExtras().getString("usuario");

        TareaObtenerPets tareaObtenerPets = new TareaObtenerPets();
        tareaObtenerPets.execute("http://vetapplove.xyz/mostrarPets.php", username);

        return view;
    }

   /* @Override
    public void onStart() {
        super.onStart();
        refresh();
    }

    private void refresh() {

        String username = getActivity().getIntent().getExtras().getString("username");

        TareaObtenerPets tareaObtenerPets = new TareaObtenerPets();
        tareaObtenerPets.execute("http://vetapplove.xyz/mostrarPets.php", username);

    }*/

    public class TareaObtenerPets extends AsyncTask<String, Void, ArrayList<AddPet>>
    {
        protected ArrayList<AddPet> doInBackground(String... params) {

            Bitmap petlistImg = null;
            int idImagen = 0;
            String petName="", petSpecie="", petRaza="", petBdate="", petSexo="";
            ArrayList<AddPet> pets = new ArrayList<AddPet>();

            String respStr = ConexionHTTP(params[0], params[1]);
            try {
                JSONArray jsonArray = new JSONArray(respStr);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = jsonArray.getJSONObject(i);
                  //      idImagen = Integer.valueOf(jsonObject.getString("id"));
                        petName = jsonObject.getString("nameAddPet");
                        petSpecie = jsonObject.getString("species");
                        petRaza = jsonObject.getString("breed");
                        petBdate=jsonObject.getString("bdateAddPet");
                        petSexo=jsonObject.getString("genderAddPet");


                        //Descargamos la imagen asociada al animal en adopción
                       // URL urlImagen = new URL("http://vetapplove.xyz/imgPet/" + idImagen + ".jpg");//abro coneexión para esta ruta de imagen
                       //  HttpURLConnection connImagen = (HttpURLConnection) urlImagen.openConnection();
                       // connImagen.connect();
                        //petlistImg = BitmapFactory.decodeStream(connImagen.getInputStream());
                        //connImagen.disconnect();
                        pets.add(new AddPet(petName, petSpecie, petRaza, petBdate, petSexo));//instanciamos el objeto.

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    } /*catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }*/
                }

            } catch (JSONException e3) {
                e3.printStackTrace();
            }
            return pets;
        }

        protected void onPostExecute(ArrayList<AddPet> pets) {
            petList = (ListView) getView().findViewById(R.id.petList);
            PetsAdapter adapter = new PetsAdapter(getContext(), R.layout.petlist, pets);
            petList.setAdapter(adapter);

        }
    }


    private String ConexionHTTP(String urll, String username) {
        URL url = null;
        HttpURLConnection con = null;
        String response = "";
        try {
            url = new URL(urll);
            //String data = "body=" + URLEncoder.encode(comment, "UTF-8");

            con = (HttpURLConnection) url.openConnection();

            // Tama�o previamente conocido
            //con.setFixedLengthStreamingMode(data.getBytes().length);
            // Establecer application/x-www-form-urlencoded debido a la simplicidad de los datos
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setReadTimeout(10000);
            con.setConnectTimeout(15000);
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("username", username);
            String query = builder.build().getEncodedQuery();


            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
            //OutputStream out = new BufferedOutputStream(con.getOutputStream());
            writer.write(query);
            writer.flush();
            writer.close();

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

        return response;
    }

}