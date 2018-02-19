package com.example.rachel.vetApp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText etUsername,etPasswrd;
    Button btnSignIn,btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername=(EditText)findViewById(R.id.etUsername);
        etPasswrd=(EditText)findViewById(R.id.etPasswrd);
        btnSignIn=(Button)findViewById(R.id.btnSignIn);
        btnSignUp=(Button)findViewById(R.id.btnSignUp);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });
    }


    private void signup() {
        Intent intent=new Intent(getApplicationContext(),SignUp.class);
        startActivity(intent);
    }

    private void registerUser() {
        String username=etUsername.getText().toString().toLowerCase();
        String password=etPasswrd.getText().toString().toLowerCase();


        TareaWSObtener1 tareaEntrar = new TareaWSObtener1();
        if(!username.equals("")&& !password.equals("")){
            password=Encriptacio.md5(password);//encriptamos
            tareaEntrar.execute("http://vetapplove.xyz/index.php", username, password);
        }
        else{
            CharSequence text = "Por favor rellene todos los campos.";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
            int offsetX = 50;
            int offsetY = 25;
            toast.setGravity(Gravity.CENTER| Gravity.CENTER, offsetX, offsetY);
            toast.show();
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
        }
    }

    public class TareaWSObtener1 extends AsyncTask<String, Void, String> {
        boolean existeUsuario=false;
        Bitmap profileImagenBitmap = null;
        String username="";
        protected String doInBackground(String... params) {
            String respStr = ConexionHTTP(params[0], params[1], params[2]);
            JSONObject jsonObject = null;
            String resultadoMensaje = null;
            //String datos[]=new String[2];
            String rutaImagen="",resultadoUsuario="" ;

            try {
                jsonObject = new JSONObject(respStr);
                resultadoMensaje = jsonObject.getString("mensaje");
                if (resultadoMensaje.equals("OK")) {
                    existeUsuario = true;
                    resultadoUsuario = jsonObject.getString("usuario");
                    //rutaImagen = jsonObject.getString("rutaImagen");
                    username= resultadoUsuario;
                    //String datos= resultadoUsuario;
                    //datos[1] = rutaImagen;

                   URL urlImagen = new URL("http://vetapplove.xyz/imgUsers/" + username+".jpg");//abro coneexión para esta ruta de imagen
                    HttpURLConnection connImagen = (HttpURLConnection) urlImagen.openConnection();
                    connImagen.connect();
                    profileImagenBitmap = BitmapFactory.decodeStream(connImagen.getInputStream());


                    connImagen.disconnect();


                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException exep) {
                exep.printStackTrace();
            } catch (IOException exe) {
                exe.printStackTrace();
            }

            return username;
        }

        protected void onPostExecute(String username) {

            if(existeUsuario==true){
                //Convertimos la imagen de formato Bitmap a String para enviarla a otra activity
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                profileImagenBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] imagen = stream.toByteArray();
                String profileImagenString = Base64.encodeToString(imagen, Base64.DEFAULT);
                Bundle parametros = new Bundle();
                parametros.putString("usuario",username);//pasamos al Navigation el nombre del usuario y su imagen
                //parametros.putString("rutaImagen",datos[1]);
                parametros.putString("profileImagenString",profileImagenString);

                //Define la actividad
                Intent i = new Intent(getApplicationContext(),Navigation.class);

                i.putExtras(parametros);

                //Inicia la actividad
                startActivity(i);
            }else{
                CharSequence text = "Usuario/password incorrectos";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                int offsetX = 50;
                int offsetY = 25;
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, offsetX, offsetY);
                toast.show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);

            }

        }

        private String ConexionHTTP(String urll, String usuario, String password) {
            URL url = null;
            HttpURLConnection con = null;
            String response = "";
            try {
                url = new URL(urll);
                //String data = "body=" + URLEncoder.encode(comment, "UTF-8");

                con = (HttpURLConnection) url.openConnection();

                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                con.setReadTimeout(10000);
                con.setConnectTimeout(15000);
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", usuario)
                        .appendQueryParameter("password", password);
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

}

