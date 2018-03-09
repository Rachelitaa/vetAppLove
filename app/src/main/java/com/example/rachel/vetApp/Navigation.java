package com.example.rachel.vetApp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Base64;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //we inflate the header view as it is not inflated yet.
        NavigationView navigationViewHeader = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationViewHeader.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.tvName);
        ImageView profilePhoto=(ImageView)hView.findViewById(R.id.profilePhoto);
        nav_user.setText(getIntent().getExtras().getString("usuario"));
        //String rutaImagen=getIntent().getExtras().getString("rutaImagen");
        String profileImagenString=getIntent().getExtras().getString("profileImagenString");

        //Convertimos la imagen en formato String a Bitmap.
        try {
            byte [] encodeByte= Base64.decode(profileImagenString,Base64.DEFAULT);
            Bitmap profileImagenBitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            profileImagenBitmap=getRoundedCornerBitmap(profileImagenBitmap);
            profilePhoto.setImageBitmap(profileImagenBitmap);
            
            /*/*if (profileImagenBitmap.getWidth() > profileImagenBitmap.getHeight()){
                profileImagenBitmap = Bitmap.createBitmap(profileImagenBitmap, 0, 0, profileImagenBitmap.getHeight(), profileImagenBitmap.getHeight());
            }else if (profileImagenBitmap.getWidth() < profileImagenBitmap.getHeight()) {
                profileImagenBitmap = Bitmap.createBitmap(profileImagenBitmap, 0, 0, profileImagenBitmap.getWidth(), profileImagenBitmap.getWidth());
            }*/
            //creamos el drawable redondeado
            RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(getResources(), profileImagenBitmap);
            //asignamos el CornerRadius
            /*roundedDrawable.setCornerRadius(profileImagenBitmap.getWidth());

            ImageView imageView = (ImageView) findViewById(R.id.profilePhoto);
            imageView.setImageDrawable(roundedDrawable);*/
            navigationView.getMenu().getItem(3).setChecked(true);
        } catch(Exception e) {
            e.getMessage();
        }

       /*
         long yourmilliseconds = System.currentTimeMillis();
       SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultdate = new Date(startTime);
        System.out.println(sdf.format(resultdate));
        long endTime=System.currentTimeMillis();


        ContentValues contentEvent = new ContentValues();
        contentEvent.put("calendar_id", 1);
        contentEvent.put("title", "Wedding");
        contentEvent.put("eventLocation", "New York");
        contentEvent.put("dtstart","1335432431000");
        contentEvent.put("dtend","1335436031000");


        Uri eventsUri = Uri.parse("content://com.android.calendar/events");
        getContentResolver().insert(eventsUri, contentEvent);
*/


        long startTime =0;
        long endTime=0;

        String startDate = String.valueOf(System.currentTimeMillis());
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            startTime=date.getTime();
        }
        catch(Exception e){ }

        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime",cal.getTimeInMillis()+60*60*1000);
        intent.putExtra("allDay", true);
        intent.putExtra("rrule", "FREQ=YEARLY");
        intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
        intent.putExtra("title", "A Test Event from android app");
        startActivity(intent);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_cita) {
        }else if (id == R.id.nav_adopcion) {
            Intent intent=new Intent(getApplicationContext(),AdopcionesListado.class);
            startActivity(intent);
        } else if (id == R.id.nav_vetsMap) {
            Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_perfilMascota) {
            Intent intent = new Intent(getApplicationContext(), PerfilMascotaActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        int width = 0;
        int height = 0;

        //hacemos que la imagen sea cuadrada
            if(bitmap.getWidth() < bitmap.getHeight()){
                width = bitmap.getWidth();
                height = bitmap.getWidth();
            } else {
                width = bitmap.getHeight();
                height =bitmap.getHeight();
            }


        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect;
        rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);
        //hacemos que la foto sea redonda
        final float roundPx = 360;

       paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect,paint);

        return output;
    }

}
