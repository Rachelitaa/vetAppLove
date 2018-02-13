package com.example.rachel.vetapplove;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class SignUp extends AppCompatActivity {
    EditText etUsername,etPasswrd,etEmail;
    Button btnSignUp;
    ImageView foto_gallery;
    private static final int ACTIVITAT_SELECCIONAR_IMATGE = 1;
    Uri imageUri;
    Bitmap bitmap=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPasswrd = (EditText) findViewById(R.id.etPasswrd);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        foto_gallery = (ImageView)findViewById(R.id.foto_gallery);

        foto_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        

        btnSignUp.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Context signup = getApplicationContext();
                String username = etUsername.getText().toString().toLowerCase();
                String email = etEmail.getText().toString().toLowerCase();
                String password = etPasswrd.getText().toString().toLowerCase();
                String imagen="";
                if(!username.equals("")&&!email.equals("")&&!password.equals("")){
                    password=Encriptacio.md5(password);//encriptamos
                    TareaObtener tarea = new TareaObtener();
                    if(bitmap!=null) {
                        imagen = convertirImgString(bitmap);
                    }else{ //Si el usuario no ha elegigo ninguna foto de perfil de su galería, se le asigna una por defecto para no enviar un null
                        try {
                            bitmap=MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),Uri.parse("android.resource://" + getPackageName() +"/"+R.drawable.noimage));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        imagen = convertirImgString(bitmap);

                    }
                    tarea.execute("http://vetapplove.xyz/registroVetApp.php", username, password, email,imagen);
                }else{
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
        });
    }

    private String convertirImgString(Bitmap bitmap) { //función que convierte una imagen bitmap en String
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, array);//comprimimos el bitmap
        byte[] imagenByte = array.toByteArray();
        String imagenString = Base64.encodeToString(imagenByte, Base64.DEFAULT);//lo codificamos en base64 string

        return imagenString;
    }

    private void openGallery(){
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
        startActivityForResult(i, ACTIVITAT_SELECCIONAR_IMATGE);
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            case ACTIVITAT_SELECCIONAR_IMATGE:
                if (resultCode == RESULT_OK) {
                    Uri seleccio = intent.getData();
                    String[] columna = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(
                            seleccio, columna, null, null, null);
                    cursor.moveToFirst();

                    int indexColumna = cursor.getColumnIndex(columna[0]);
                    String rutaFitxer = cursor.getString(indexColumna);
                    cursor.close();

                   // foto_gallery.setImageURI(seleccio);//sí

                    try {
                        bitmap=MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),seleccio);
                        bitmap=cropBitmap(bitmap,400,400);
                        foto_gallery.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
        }
    }



    public class TareaObtener extends AsyncTask<String, Void,JSONObject> {
    Context signup=getApplicationContext();
    protected JSONObject doInBackground(String... params) {
        String respStr = ConexionHTTP(params[0], params[1], params[2], params[3],params[4]);
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
                Intent i = new Intent(getApplicationContext(),Navigation.class);
                startActivity(i);
               //***

            }
        }catch(JSONException e){
            e.printStackTrace();
        }

    }

    private String ConexionHTTP(String urll, String username,String password,String email,String imagen) {
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
                    .appendQueryParameter("email",email)
                    .appendQueryParameter("imagen",imagen);


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
    public static Bitmap cropBitmap(Bitmap original, int height, int width) {
        Bitmap croppedImage = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(croppedImage);

        Rect srcRect = new Rect(0, 0, original.getWidth(), original.getHeight());
        Rect dstRect = new Rect(0, 0, width, height);

        int dx = (srcRect.width() - dstRect.width()) / 2;
        int dy = (srcRect.height() - dstRect.height()) / 2;

// If the srcRect is too big, use the center part of it.
        srcRect.inset(Math.max(0, dx), Math.max(0, dy));

// If the dstRect is too big, use the center part of it.
        dstRect.inset(Math.max(0, -dx), Math.max(0, -dy));

// Draw the cropped bitmap in the center
        canvas.drawBitmap(original, srcRect, dstRect, null);

        original.recycle();

        return croppedImage;
    }



}
