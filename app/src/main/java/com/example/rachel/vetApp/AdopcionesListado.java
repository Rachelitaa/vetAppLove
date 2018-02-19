package com.example.rachel.vetApp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

public class AdopcionesListado extends AppCompatActivity {

    Button btnAñadirAdopcion;
    ListView lvAdopciones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopcioneslistado);
        btnAñadirAdopcion=(Button)findViewById(R.id.btnAñadirAdopcion);

        btnAñadirAdopcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),AdopcionesAnadir.class);
                startActivity(intent);
            }
        });
        TareaObtenerAdopciones tareaEntrar = new TareaObtenerAdopciones();
        tareaEntrar.execute("http://vetapplove.xyz/mostrarAdopciones.php");
    }

    public class TareaObtenerAdopciones extends AsyncTask<String, Void, JSONArray> {

        protected JSONArray doInBackground(String... params) {
            String respStr = ConexionHTTP(params[0]);
            JSONArray jsonArray = null;

            try {
                jsonArray = new JSONArray(respStr);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonArray;
        }

        protected void onPostExecute(JSONArray jsonArray) {
            String resultado[] = null;
            Adopcion adopcion = null;
            Bitmap profileImagenBitmap=null;
            int idImagen = 0;
            String tipoAnimal = "", nombreAnimal = "", ciudad = "", pais = "";
            ArrayList<Adopcion> adopciones = new ArrayList<Adopcion>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = jsonArray.getJSONObject(i);
                    idImagen = Integer.valueOf(jsonObject.getString("id"));
                    tipoAnimal = jsonObject.getString("tipoAnimal");
                    nombreAnimal = jsonObject.getString("nombreAnimal");
                    ciudad = jsonObject.getString("ciudad");
                    pais = jsonObject.getString("pais");
                    adopciones.add(new Adopcion(R.drawable.vetsmap,tipoAnimal, nombreAnimal, ciudad, pais));

                    /*//Descargamos la imagen asociada al animal en adopción
                    URL urlImagen = new URL("http://vetapplove.xyz/imgAdopciones/" + idImagen+".jpg");//abro coneexión para esta ruta de imagen
                    HttpURLConnection connImagen = (HttpURLConnection) urlImagen.openConnection();
                    connImagen.connect();
                    profileImagenBitmap = BitmapFactory.decodeStream(connImagen.getInputStream());
                    connImagen.disconnect();*/

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
            lvAdopciones = (ListView) findViewById(R.id.lvAdopciones);
            AdopcionesAdapter adapter = new AdopcionesAdapter(getApplicationContext(),R.layout.entrada_adopciones,adopciones);
            lvAdopciones.setAdapter(adapter);


        }

        private String ConexionHTTP(String urll) {
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

               /* Uri.Builder builder = new Uri.Builder();

                String query = builder.build().getEncodedQuery();


                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
                //OutputStream out = new BufferedOutputStream(con.getOutputStream());
                writer.write(query);
                writer.flush();
                writer.close();

                //writer.write(getPostDataString(postDataParams));*/

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



}
