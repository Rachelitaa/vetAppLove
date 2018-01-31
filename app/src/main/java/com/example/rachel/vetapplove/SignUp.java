package com.example.rachel.vetapplove;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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


public class SignUp extends AppCompatActivity {
    EditText etUsername,etPasswrd,etEmail;
    Button btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPasswrd = (EditText) findViewById(R.id.etPasswrd);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);


        btnSignUp.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Context signup = getApplicationContext();
                String username = etUsername.getText().toString().toLowerCase();
                String email = etEmail.getText().toString().toLowerCase();
                String password = etPasswrd.getText().toString().toLowerCase();

                TareaObtener tarea = new TareaObtener();
                //tarea.execute("http://vetapplove.000webhostapp.com/registroVetApp.php", username, email, passwrd);
                tarea.execute("http://vetapplove.xyz/registroVetApp.php", username, email, password);
            }
        });
    }


public class TareaObtener extends AsyncTask<String, Void,JSONObject> {
    Context signup=getApplicationContext();
    protected JSONObject doInBackground(String... params) {
        String respStr = ConexionHTTP(params[0], params[1], params[2], params[3]);
        JSONObject jsonObject=null;

        try {
            jsonObject=new JSONObject(respStr);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    protected void onPostExecute(JSONObject jsonObject) {

        String resultado;
        try {
            resultado = jsonObject.getString("mensaje");
            if (resultado.equals("existe")) {
                ToastsVarios("This username already exists.Type another username please",signup);

            } else {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

            }
        }catch(JSONException e){
            e.printStackTrace();
        }

    }

    private String ConexionHTTP(String urll, String username,String password,String email) {
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
                    .appendQueryParameter("username",username) //estructura para enviar el POST
                    .appendQueryParameter("password",password)
                    .appendQueryParameter("email",email);


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

        return response;
    }
}
    public static void ToastsVarios(String textoAlerta,Context context){

        CharSequence text = textoAlerta;
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        int offsetX = 50;
        int offsetY = 25;
        toast.setGravity(Gravity.CENTER| Gravity.CENTER, offsetX, offsetY);
        toast.show();

    }


}
