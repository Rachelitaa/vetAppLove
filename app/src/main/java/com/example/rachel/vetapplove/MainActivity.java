package com.example.rachel.vetapplove;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
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
            public void onClick(View view) {
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
            CharSequence text = "Fill all the fields please!!";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
            int offsetX = 50;
            int offsetY = 25;
            toast.setGravity(Gravity.CENTER| Gravity.CENTER, offsetX, offsetY);
            toast.show();
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
        }
    }

    public class TareaWSObtener1 extends AsyncTask<String, Void, JSONObject> {


        protected JSONObject doInBackground(String... params) {
            String respStr = ConexionHTTP(params[0], params[1], params[2]);
            JSONObject jsonObject=null;

            try {
                jsonObject=new JSONObject(respStr);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        protected void onPostExecute(JSONObject result) {
            String resultado1= null;

            try {
                resultado1 = result.getString("mensaje");
                if(resultado1.equals("OK")) {

                    String resultado2 = result.getString("usuario");


                    Bundle parametros = new Bundle();
                    parametros.putString("usuario", resultado2);

                    //Define la actividad
                    Intent i = new Intent(getApplicationContext(), Navigation.class);

                    i.putExtras(parametros);

                    //Inicia la actividad
                    startActivity(i);

                }
                else{

                    CharSequence text = "Invalid user or password!!";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    int offsetX = 50;
                    int offsetY = 25;
                    toast.setGravity(Gravity.CENTER| Gravity.CENTER, offsetX, offsetY);
                    toast.show();
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);


                }

            } catch (JSONException e) {
                e.printStackTrace();
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
