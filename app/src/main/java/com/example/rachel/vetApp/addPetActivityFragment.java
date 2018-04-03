package com.example.rachel.vetApp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.app.Activity.RESULT_OK;

/**
 * A placeholder fragment containing a simple view.
 */
public class addPetActivityFragment extends Fragment {

    EditText etName, etSpecies, etBreed, etGender, etBirthdate;
    Button btnSavePet;
    ImageView imgImageAddPet;

    private static final int ACTIVITAT_SELECCIONAR_IMATGE = 1;
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
        btnSavePet = view.findViewById(R.id.savePet);
        imgImageAddPet = view.findViewById(R.id.imageAddPet);


        imgImageAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnSavePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameAddPet =etName.getText().toString().toLowerCase();
                String species = etSpecies.getText().toString().toLowerCase();
                String breed = etBreed.getText().toString().toLowerCase();
                String bdateAddPet = etBirthdate.getText().toString();
                String genderAddPet = etGender.getText().toString().toLowerCase();
                String imageAddPet="";
                String username = getActivity().getIntent().getExtras().getString("usuario");




                if(!nameAddPet.equals("") && !species.equals("") && !breed.equals("") && !bdateAddPet.equals("") && !genderAddPet.equals(""))
                {
                    TareaObtener tarea = new TareaObtener();

                    if(bitmap!=null) {
                        imageAddPet = convertirImgString(bitmap);
                    }else{ //Si el usuario no ha elegigo ninguna foto para la mascota que quiere dar en adopcion, se le asigna una por defecto para no enviar un null
                        try {
                            bitmap= MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.parse("android.resource://" + getActivity().getPackageName() +"/"+R.drawable.noimage));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        imageAddPet = convertirImgString(bitmap);

                    }
                    tarea.execute("http://vetapplove.xyz/addPet.php", nameAddPet, species, breed, bdateAddPet, genderAddPet, imageAddPet, username);
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


            }
        });

        return view;
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


    public class TareaObtener extends AsyncTask<String,Void,Void>
    {
        protected Void doInBackground(String... params) {
            ConexionHTTP(params[0], params[1], params[2], params[3],params[4],params[5],params[6], params[7]);

            return null;
        }

        private void ConexionHTTP(String urll,  String nameAddPet, String species, String breed, String bdateAddPet, String genderAddPet, String imageAddPet , String username) {
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

                //estructura para enviar el POST
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("nameAddPet",nameAddPet)
                        .appendQueryParameter("species",species)
                        .appendQueryParameter("breed",breed)
                        .appendQueryParameter("bdateAddPet",bdateAddPet)
                        .appendQueryParameter("genderAddPet",genderAddPet)
                        .appendQueryParameter("imageAddPet",imageAddPet)
                        .appendQueryParameter("username",username);



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

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            case ACTIVITAT_SELECCIONAR_IMATGE:
                if (resultCode == RESULT_OK) {
                    Uri seleccio = intent.getData();
                    String[] columna = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getActivity().getContentResolver().query(
                            seleccio, columna, null, null, null);
                    cursor.moveToFirst();

                    int indexColumna = cursor.getColumnIndex(columna[0]);
                    String rutaFitxer = cursor.getString(indexColumna);
                    cursor.close();

                    // foto_gallery.setImageURI(seleccio);//sí

                    try {
                        bitmap=MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),seleccio);
                        bitmap=cropBitmap(bitmap,400,400);
                        imgImageAddPet.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
        }
    }

}
